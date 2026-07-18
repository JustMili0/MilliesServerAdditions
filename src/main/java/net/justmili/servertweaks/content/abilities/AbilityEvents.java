package net.justmili.servertweaks.content.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.justmili.libs.v1.utils.CommandUtil;
import net.justmili.libs.v1.utils.EntityUtil;
import net.justmili.libs.v1.utils.FdaApiUtil;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.TickingAbility;
import net.justmili.servertweaks.content.abilities.core.AbilitiesFileUtil;
import net.justmili.servertweaks.core.variables.PlayerAttachments;
import net.justmili.servertweaks.registries.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

import static net.justmili.servertweaks.content.abilities.core.AbilitiesFileUtil.has;

public class AbilityEvents {
    public static void registerAbilityEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                ServerLevel level = player.level();

                // Tick all Ticking Abilities
                for (Ability ability : AbilitiesFileUtil.getAbilities(player)) {
                    if (ability instanceof TickingAbility tickingAbility) {
                        tickingAbility.tick(player, level);
                    }
                }

                // Reset attribute modifiers if related ability is not applied
                AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED),
                    attack = player.getAttribute(Attributes.ATTACK_DAMAGE),
                    maxHp = player.getAttribute(Attributes.MAX_HEALTH);

                if (!AbilitiesFileUtil.has(player, Abilities.SLOW) && speed != null) speed.removeModifier(Abilities.AR_SLOW_SPEED);
                if (!has(player, Abilities.STRONG) && attack != null) attack.removeModifier(Abilities.AR_STRONG_DAMAGE);
                if (!has(player, Abilities.STRONG) && maxHp != null) maxHp.removeModifier(Abilities.AR_STRONG_HP);

                // Change to: Check if player has presets locked;
                // If yes but has no abilities or modifiers then clear them from the file and unlock preset picking
                // As well as inform the player that they have to pick their preset/class again
                if (FdaApiUtil.getBoolValue(player, PlayerAttachments.PICKED_PRESET)
                    && AbilitiesFileUtil.getAbilities(player).isEmpty()
                    && AbilitiesFileUtil.getModifiers(player).isEmpty()) {

                    // Remove from file
                    AbilitiesFileUtil.clearPlayerProfile(player);
                    // Unlock preset picking
                    FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, false);
                    // Inform player
                    CommandUtil.sendFailTo(player, "Your ability preset data was invalid or missing. Please pick your ability preset again");
                }
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(AbilityEvents::weakToDamage);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(AbilityEvents::squishy);

        UseItemCallback.EVENT.register(AbilityEvents::pearling);
        UseBlockCallback.EVENT.register(AbilityEvents::grassEater);
        UseItemCallback.EVENT.register(AbilityEvents::handleDietItemCall);
        UseBlockCallback.EVENT.register(AbilityEvents::handleDietBlockCall);
        UseEntityCallback.EVENT.register(AbilityEvents::bugEaterEntities);
        UseEntityCallback.EVENT.register(AbilityEvents::bovid);
    }

    private static boolean squishy(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;
        if (handleOtherImmunities(player, source)) return false;
        if (!has(player, Abilities.SQUISHY)) return true;

        if (!(source.is(DamageTypes.FALL) || source.is(DamageTypes.FLY_INTO_WALL))) return true;

        return recalcDamage(player, source, value, 0.75F);
    }
    private static boolean weakToDamage(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;
        if (handleOtherImmunities(player, source)) return false;

        if (!has(player, Abilities.WEAK_TO_DAMAGE)) return true;
        if (source.is(DamageTypes.FALL)) return true;

        return recalcDamage(player, source, value, 1.75F);
    }

    private static InteractionResult pearling(Player interacting, Level level, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!has(player, Abilities.PEARLING)) return InteractionResult.PASS;

        ItemStack pearl = new ItemStack(Items.ENDER_PEARL);
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(pearl.getItem())) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(pearl)) return InteractionResult.PASS;

        ItemStack inSlot = player.getInventory().getItem(player.getInventory().getSelectedSlot());
        if (inSlot.isEmpty()) {
            player.setItemInHand(hand, pearl);
        } else {
            player.getInventory().add(pearl); // Just in case
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult bovid(Player interacting, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer milking)) return InteractionResult.PASS;
        if (!(entity instanceof ServerPlayer milked)) return InteractionResult.PASS;
        if (!has(milked, Abilities.BOVID)) return InteractionResult.PASS;

        if (milked.getUUID().equals(UUID.fromString("66774f1e-99de-4f1b-8293-906ca3488549")) // Funny hardcoded thing
            && !milking.getUUID().equals(UUID.fromString("19c3c783-9359-4311-98bf-79a6d361362d"))) return InteractionResult.PASS;

        ItemStack stack = milking.getItemInHand(hand);
        if (!stack.is(Items.BUCKET)) return InteractionResult.PASS;

        // Prevent double processing
        int currentTick = milking.tickCount;
        if (FdaApiUtil.getIntValue(milking, PlayerAttachments.MILK_TICK) == currentTick) return InteractionResult.CONSUME;
        FdaApiUtil.setIntValue(milking, PlayerAttachments.MILK_TICK, currentTick);

        ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
        milkBucket.set(DataComponents.CUSTOM_NAME, Component.literal(milked.getName().getString() + "'s Milk").withStyle(style -> style.withItalic(false)));
        if (stack.getCount() == 1) {
            milking.setItemInHand(hand, milkBucket);
        } else {
            stack.shrink(1);
            milking.getInventory().add(milkBucket);
        }
        milking.containerMenu.broadcastFullState();

        return InteractionResult.CONSUME;
    }

    private static InteractionResult handleDietItemCall(Player interacting, Level level, InteractionHand hand) { // Clicking while looking at nothing
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;

        bugEaterItems(interacting, level, hand); // Handle this first
        if (isDietBlocked(player, player.getItemInHand(hand))) return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }
    private static InteractionResult handleDietBlockCall(Player interacting, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        var state = level.getBlockState(hitResult.getBlockPos());
        var block = state.getBlock();

        // Fuckery to properly diet-block while allowing planting and harvesting
        if (stack.getItem() instanceof BlockItem) {
            if (stack.is(ConventionalItemTags.CROPS) && state.is(BlockTags.GROWS_CROPS)) return InteractionResult.PASS;
            if (stack.is(ConventionalItemTags.BERRY_FOODS) && state.is(BlockTags.SUPPORTS_VEGETATION)) return InteractionResult.PASS;
            //
        }

        // Fix trying to RC anything with non-diet item in hand being blocked
        if (block.defaultBlockState().is(TagRegistry.DIET_ALLOW_BLOCK_INTERACTION)) return InteractionResult.PASS;

        bugEaterItems(interacting, level, hand); // Handle this first
        if (isDietBlocked(player, stack)) return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }
    private static void bugEaterItems(Player interacting, Level level, InteractionHand hand) {
        if (level.isClientSide()) return;
        if (!(interacting instanceof ServerPlayer player)) return;
        if (!has(player, Abilities.INSECTIVORE)) return;

        ItemStack stack = player.getItemInHand(hand);
        FoodData food = player.getFoodData();

        if (!food.needsFood()) return;

        if (stack.is(TagRegistry.DIET_BUG_ITEMS) && !stack.has(DataComponents.FOOD)) {
            stack.shrink(1);
            food.add(3, 2.0F);
            playEatSound(player);
            sendUpdatePacket(player);

        }
        // In-tag foods with food data handle via handleDiet* methods
    }
    private static InteractionResult bugEaterEntities(Player interacting, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!has(player, Abilities.INSECTIVORE)) return InteractionResult.PASS;

        FoodData food = player.getFoodData();
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
            player.hurt(player.damageSources().onFire(), 2f);
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

    private static InteractionResult grassEater(Player interacting, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!has(player, Abilities.HERBIVORE)) return InteractionResult.PASS;

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();
        if (!level.getBlockState(pos).is(TagRegistry.DIET_FOLIAGE)) return InteractionResult.PASS;

        FoodData food = player.getFoodData();
        if (!food.needsFood()) return InteractionResult.PASS;
        level.destroyBlock(pos, false);
        food.add(2, 0.5F);

        // Update the client about eaten food
        sendUpdatePacket(player);

        return InteractionResult.SUCCESS;
    }

    // Helper methods/variables
    private static final Map<ResourceKey<DamageType>, Ability> DAMAGE_IMMUNITY = Map.of(
        DamageTypes.IN_FIRE, Abilities.FIRE_IMMUNE, DamageTypes.ON_FIRE, Abilities.FIRE_IMMUNE, DamageTypes.LAVA, Abilities.LAVA_IMMUNE,
        DamageTypes.HOT_FLOOR, Abilities.HEAT_IMMUNE, DamageTypes.FREEZE, Abilities.FREEZE_IMMUNE,
        DamageTypes.FALL, Abilities.FALL_IMMUNE, DamageTypes.ENDER_PEARL, Abilities.PEARLING,
        DamageTypes.DROWN, Abilities.BREATHES_UNDERWATER
    );
    private static boolean handleOtherImmunities(ServerPlayer player, DamageSource source) {
        Ability immunity = DAMAGE_IMMUNITY.get(source.typeHolder().unwrapKey().orElse(null));
        return immunity != null && has(player, immunity);
    }
    private static boolean recalcDamage(ServerPlayer player, DamageSource source, float damageTaken, float multiplier) {
        if (FdaApiUtil.getIntValue(player, PlayerAttachments.HURT_TICK) != player.tickCount) {
            // safeguard to make sure ALLOW_DAMAGE doesn't get called again and for this to not run recursively
            FdaApiUtil.setIntValue(player, PlayerAttachments.HURT_TICK, player.tickCount);
            player.hurt(source, damageTaken * multiplier);

            return false;
        }
        return true;
    }

    private static void playEatSound(ServerPlayer player) {
        player.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F+(player.getRandom().nextFloat()-player.getRandom().nextFloat()) * 0.4F);
    }
    private static boolean isDietBlocked(ServerPlayer player, ItemStack stack) {
        if (!stack.has(DataComponents.FOOD) /*|| stack.has(DataComponents.BUCKET_ENTITY_DATA)*/) return false;

        boolean carnivore = has(player, Abilities.CARNIVORE),
            vegetarian = has(player, Abilities.VEGETARIAN),
            sweetOnly = has(player, Abilities.SACCHARIVORE),
            grassEater = has(player, Abilities.HERBIVORE),
            bugEater = has(player, Abilities.INSECTIVORE),
            canConsumeGolden = AbilitiesFileUtil.has(player, Modifiers.CAN_EAT_GOLDEN_FOOD),

            isMeat = stack.is(TagRegistry.DIET_CARNIVORE),
            isVege = stack.is(TagRegistry.DIET_VEGETARIAN),
            isSweet = stack.is(TagRegistry.DIET_SWEETS),
            isBugLike = stack.is(TagRegistry.DIET_BUG_ITEMS),
            isGold = stack.is(TagRegistry.DIET_MODIFIER_GOLDEN_FOODS);

        if (!carnivore && !vegetarian && !sweetOnly && !grassEater && !bugEater) return false;

        return (!canConsumeGolden || !isGold) &&
            (!carnivore || !isMeat) &&
            (!vegetarian || !isVege) &&
            (!sweetOnly || !isSweet) &&
            (!bugEater || !isBugLike);
        // No GRASS_EATER item tag for this to check. GRASS_EATER diet is handled by grassEater interaction method.
    }
    private static boolean isType(Entity entity, TagKey<EntityType<?>> tag) {
        return entity.is(tag);
    }
    private static boolean isBugLikeConsumable(Entity entity) {
        if (entity instanceof Slime slime) { /// Change to AbstractCubeMob with 26.2!
            return slime.getSize() == 1;
        }
        return true;
    }
    private static void sendUpdatePacket(ServerPlayer player) {
        FoodData food = player.getFoodData();
        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), food.getFoodLevel(), food.getSaturationLevel()));
    }
}