package net.justmili.servertweaks.content.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.libs.v1.utils.common.*;
import net.justmili.libs.v1.utils.server.ServerUtil;
import net.justmili.servertweaks.content.abilities.type.*;
import net.justmili.servertweaks.registries.TagRegistry;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

import static net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil.*;

public class AbilityEvents {
    public static void registerAbilityEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : ServerUtil.getPlayers(server)) {
                var level = player.level();

                // Tick all Ticking Abilities
                tickIfTicking(getAbilities(player), player, level);
                tickIfTicking(getDebuffs(player), player, level);
                tickIfTicking(getModifiers(player), player, level);


                // Reset attribute modifiers if related ability is not applied
                removeModifier(player, AttribUtil.get(player, Attributes.MOVEMENT_SPEED), Debuffs.SLOW, Debuffs.AR_SLOW_SPEED);
                removeModifier(player, AttribUtil.get(player, Attributes.ATTACK_DAMAGE), Abilities.STRONG, Abilities.AR_STRONG_DAMAGE);
                removeModifier(player, AttribUtil.get(player, Attributes.MAX_HEALTH), Abilities.STRONG, Abilities.AR_STRONG_HP);

                if (FdaUtil.getBool(player, PlayerVars.HAS_PICKED_PRESET) && getAbilities(player).isEmpty() && getModifiers(player).isEmpty()) {

                    // Remove from file
                    clearPlayerProfile(player);
                    // Unlock preset picking
                    FdaUtil.set(player, PlayerVars.HAS_PICKED_PRESET, false);
                    // Inform player
                    CommandUtil.sendFailTo(player, "Your ability preset data was invalid or missing. Please pick your ability preset again");
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            var leaving = handler.getPlayer();
            boolean stillCovered = server.getPlayerList().getPlayers().stream().anyMatch(p -> p != leaving && has(p, Debuffs.IS_MONSTER));
            if (!stillCovered) Debuffs.restoreAllMonsterGoals(leaving.level());
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(AbilityEvents::weakToDamage);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(AbilityEvents::squishy);

        UseItemCallback.EVENT.register(AbilityEvents::pearling);
        UseBlockCallback.EVENT.register(AbilityEvents::grassEater);
        UseEntityCallback.EVENT.register(AbilityEvents::bugEaterEntities);
        UseEntityCallback.EVENT.register(AbilityEvents::bovid);
    }

    static void tickIfTicking(Iterable<? extends AnyType> traits, ServerPlayer player, ServerLevel level) {
        for (var trait : traits) {
            if (trait instanceof TickingType ticking) ticking.tick(player, level);
        }
    }

    // Remove modifiers that are related to abilities the player does not have
    public static void removeModifier(Player player, AttributeInstance instance, Ability ability, Identifier id) {
        if (!has(player, ability) && instance != null) instance.removeModifier(id);
    }

    public static void removeModifier(Player player, AttributeInstance instance, Debuff debuff, Identifier id) {
        if (!has(player, debuff) && instance != null) instance.removeModifier(id);
    }

    static boolean squishy(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof Player player)) return true;
        if (handleOtherImmunities(player, source)) return false;
        if (!has(player, Abilities.SQUISHY)) return true;

        if (!(source.is(DamageTypes.FALL) || source.is(DamageTypes.FLY_INTO_WALL))) return true;

        return recalcDamage(player, source, value, 0.25F);
    }

    static boolean weakToDamage(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof Player player)) return true;
        if (handleOtherImmunities(player, source)) return false;

        if (!has(player, Debuffs.WEAK_TO_DAMAGE)) return true;
        if (source.is(DamageTypes.FALL)) return true;

        return recalcDamage(player, source, value, 1.25F);
    }

    static InteractionResult pearling(Player player, Level level, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!has(player, Abilities.PEARLING)) return InteractionResult.PASS;

        var pearl = new ItemStack(Items.ENDER_PEARL);
        var stack = player.getItemInHand(hand);
        if (!stack.is(pearl.getItem())) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(pearl)) return InteractionResult.PASS;

        var inSlot = player.getInventory().getItem(player.getInventory().getSelectedSlot());
        if (inSlot.isEmpty()) {
            player.setItemInHand(hand, pearl);
        } else {
            player.getInventory().add(pearl); // Just in case
        }

        return InteractionResult.PASS;
    }

    static InteractionResult bovid(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!(entity instanceof Player milked)) return InteractionResult.PASS;
        if (!has(milked, Abilities.BOVID)) return InteractionResult.PASS;

        // Funny hardcoded thing
        if ((uuidMatchesString(milked, "66774f1e-99de-4f1b-8293-906ca3488549") || uuidMatchesString(milked, "3ff71c17-0edb-4e05-aded-c0bc378a05a0"))
            && !uuidMatchesString(player, "19c3c783-9359-4311-98bf-79a6d361362d")) return InteractionResult.PASS;

        var stack = player.getItemInHand(hand);
        if (!stack.is(Items.BUCKET)) return InteractionResult.PASS;

        // Prevent double processing
        int currentTick = player.tickCount;
        if (FdaUtil.getInt(player, PlayerVars.MILK_TICK) == currentTick) return InteractionResult.CONSUME;
        FdaUtil.set(player, PlayerVars.MILK_TICK, currentTick);

        EntityUtil.useStackWithResult(player, hand, NbtUtil.set(
            new ItemStack(Items.MILK_BUCKET), DataComponents.CUSTOM_NAME,
            Component.literal(milked.getName().getString() + "'s Milk").withStyle(style -> style.withItalic(false))).getItem());
        player.containerMenu.broadcastFullState();

        return InteractionResult.CONSUME;
    }

    public static InteractionResult handleDietItemCall(Player player, Level level, InteractionHand hand) { // Clicking while looking at nothing
        if (level.isClientSide()) return InteractionResult.PASS;

        bugEaterItems(player, level, hand); // Handle this first
        if (isDietBlocked(player, player.getItemInHand(hand))) return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }

    static void bugEaterItems(Player player, Level level, InteractionHand hand) {
        if (level.isClientSide()) return;
        if (!has(player, Debuffs.INSECTIVORE)) return;

        var stack = player.getItemInHand(hand);
        var food = player.getFoodData();

        if (!food.needsFood()) return;

        if (stack.is(TagRegistry.DIET_BUG_ITEMS) && !stack.has(DataComponents.FOOD)) {
            stack.shrink(1);
            food.add(3, 2.0F);
            playEatSound(player);
            sendUpdatePacket(player);
        }
        // In-tag foods with food data handle via handleDiet* methods
    }

    static InteractionResult bugEaterEntities(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!has(player, Debuffs.INSECTIVORE)) return InteractionResult.PASS;

        var food = player.getFoodData();
        if (!food.needsFood()) return InteractionResult.PASS;

        // Calculate saturation and nutrition
        int addNutrition = 0;
        float addSaturation = 0f;
        if (isType(entity, TagRegistry.DIET_BUG_ENTITY_NUTRITIOUS)) addNutrition = 2;
        if (isType(entity, TagRegistry.DIET_BUG_ENTITY_SATURATING)) addSaturation = 2f;
        int nutrition = 3 + addNutrition;
        float saturation = 2 + addSaturation;

        // Apply everything accordingly
        if (isType(entity, TagRegistry.DIET_BUG_ENTITY_GENERIC)) {
            if (!isBugLikeConsumable(entity)) return InteractionResult.PASS;

            entity.discard();
            food.add(nutrition, saturation);
            playEatSound(player);
            sendUpdatePacket(player);

            return InteractionResult.CONSUME;
        } else if (isType(entity, TagRegistry.DIET_BUG_ENTITY_FIRE)) {
            if (!isBugLikeConsumable(entity)) return InteractionResult.PASS;

            entity.discard();
            food.add(nutrition, saturation);
            playEatSound(player);
            player.hurtServer((ServerLevel) level, player.damageSources().onFire(), 2f);
            sendUpdatePacket(player);

            return InteractionResult.CONSUME;
        } else if (isType(entity, TagRegistry.DIET_BUG_ENTITY_POISON)) {
            if (!isBugLikeConsumable(entity)) return InteractionResult.PASS;

            entity.discard();
            food.add(nutrition, saturation);
            playEatSound(player);
            EntityUtil.applyEffect(player, MobEffects.POISON, 200, 0);
            sendUpdatePacket(player);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    static InteractionResult grassEater(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!has(player, Debuffs.HERBIVORE)) return InteractionResult.PASS;

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        var pos = hitResult.getBlockPos();
        if (!level.getBlockState(pos).is(TagRegistry.DIET_FOLIAGE)) return InteractionResult.PASS;

        var food = player.getFoodData();
        if (!food.needsFood()) return InteractionResult.PASS;
        level.destroyBlock(pos, false);
        food.add(2, 0.5F);

        // Update the client about eaten food
        sendUpdatePacket(player);

        return InteractionResult.SUCCESS;
    }

    // Helper methods/variables
    static final Map<ResourceKey<DamageType>, Ability> DAMAGE_IMMUNITY = Map.of(
        DamageTypes.IN_FIRE, Abilities.FIRE_IMMUNE, DamageTypes.ON_FIRE, Abilities.FIRE_IMMUNE, DamageTypes.LAVA, Abilities.LAVA_IMMUNE,
        DamageTypes.HOT_FLOOR, Abilities.HEAT_IMMUNE, DamageTypes.FREEZE, Abilities.FREEZE_IMMUNE,
        DamageTypes.FALL, Abilities.FALL_IMMUNE, DamageTypes.ENDER_PEARL, Abilities.PEARLING,
        DamageTypes.DROWN, Abilities.BREATHES_UNDERWATER
    );

    static boolean handleOtherImmunities(Player player, DamageSource source) {
        var immunity = DAMAGE_IMMUNITY.get(source.typeHolder().unwrapKey().orElse(null));
        return immunity != null && has(player, immunity);
    }

    static boolean recalcDamage(Player player, DamageSource source, float damageTaken, float multiplier) {
        if (FdaUtil.getInt(player, PlayerVars.HURT_TICK) != player.tickCount) {
            // safeguard to make sure ALLOW_DAMAGE doesn't get called again and for this to not run recursively
            FdaUtil.set(player, PlayerVars.HURT_TICK, player.tickCount);
            player.hurtServer((ServerLevel) player.level(), source, damageTaken * multiplier);

            return false;
        }
        return true;
    }

    static void playEatSound(Player player) {
        player.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F + (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.4F);
    }

    static boolean isDietBlocked(Player player, ItemStack stack) {
        if (!stack.has(DataComponents.FOOD) || stack.has(DataComponents.BUCKET_ENTITY_DATA)) return false;

        boolean carnivore = has(player, Debuffs.CARNIVORE);
        boolean vegetarian = has(player, Debuffs.VEGETARIAN);
        boolean sweetOnly = has(player, Debuffs.SACCHARIVORE);
        boolean grassEater = has(player, Debuffs.HERBIVORE);
        boolean bugEater = has(player, Debuffs.INSECTIVORE);
        boolean canConsumeGolden = has(player, Modifiers.CAN_EAT_GOLDEN_FOOD);

        boolean isMeat = stack.is(TagRegistry.DIET_CARNIVORE);
        boolean isVege = stack.is(TagRegistry.DIET_VEGETARIAN);
        boolean isSweet = stack.is(TagRegistry.DIET_SWEETS);
        boolean isBugLike = stack.is(TagRegistry.DIET_BUG_ITEMS);
        boolean isGold = stack.is(TagRegistry.DIET_MODIFIER_GOLDEN_FOODS);

        if (!carnivore && !vegetarian && !sweetOnly && !grassEater && !bugEater) return false;

        return (!canConsumeGolden || !isGold) &&
            (!carnivore || !isMeat) &&
            (!vegetarian || !isVege) &&
            (!sweetOnly || !isSweet) &&
            (!bugEater || !isBugLike);
        // No GRASS_EATER item tag for this to check. GRASS_EATER diet is handled by grassEater interaction method.
    }

    static boolean isType(Entity entity, TagKey<EntityType<?>> tag) {
        return entity.is(tag);
    }

    static boolean isBugLikeConsumable(Entity entity) {
        if (entity instanceof AbstractCubeMob slime) return slime.getSize() == 1;
        return false;
    }

    static boolean uuidMatchesString(Player player, String uuid) {
        return player.getUUID().equals(UUID.fromString(uuid));
    }

    static void sendUpdatePacket(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        var food = player.getFoodData();
        serverPlayer.connection.send(new ClientboundSetHealthPacket(player.getHealth(), food.getFoodLevel(), food.getSaturationLevel()));
    }
}