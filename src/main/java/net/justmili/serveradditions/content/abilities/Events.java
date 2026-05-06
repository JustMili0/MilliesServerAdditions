package net.justmili.serveradditions.content.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.justmili.serveradditions.config.Config;
import net.justmili.serveradditions.content.abilities.data.DataTags;
import net.justmili.serveradditions.content.abilities.registries.AbilityRegistry;
import net.justmili.serveradditions.content.abilities.registries.ModifierRegistry;
import net.justmili.serveradditions.content.abilities.type.Ability;
import net.justmili.serveradditions.content.abilities.type.TickingAbility;
import net.justmili.serveradditions.core.util.FdaApiUtil;
import net.justmili.serveradditions.core.variables.PlayerAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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

import static net.justmili.serveradditions.content.abilities.DataManager.has;

public class Events {
    public static void registerAbilityEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                ServerLevel level = player.level();

                // Tick all Ticking Abilities
                for (Ability ability : DataManager.getAbilities(player)) {
                    if (ability instanceof TickingAbility tickingAbility) {
                        tickingAbility.tick(player, level);
                    }
                }

                // Reset attribute modifiers if related ability is not applied
                AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED),
                    attack = player.getAttribute(Attributes.ATTACK_DAMAGE),
                    maxHp = player.getAttribute(Attributes.MAX_HEALTH);

                if (!has(player, AbilityRegistry.SLOW) && !(speed == null)) speed.removeModifier(AbilityRegistry.AM_SLOW_SPEED);
                if (!has(player, AbilityRegistry.STRONG) && !(attack == null)) attack.removeModifier(AbilityRegistry.AM_STRONG_DAMAGE);
                if (!has(player, AbilityRegistry.STRONG) && !(maxHp == null)) maxHp.removeModifier(AbilityRegistry.AM_STRONG_HP);
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Events::specialDamageImmune);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Events::weakToDamage);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Events::squishy);

        UseItemCallback.EVENT.register(Events::pearling);
        UseBlockCallback.EVENT.register(Events::grassEater);
        UseItemCallback.EVENT.register(Events::handleDietItemCall);
        UseBlockCallback.EVENT.register(Events::handleDietBlockCall);
        UseEntityCallback.EVENT.register(Events::bugEaterEntities);
    }

    private static boolean specialDamageImmune(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;

        if (has(player, AbilityRegistry.FIRE_IMMUNE) && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE))) return false;
        if (has(player, AbilityRegistry.LAVA_IMMUNE) && source.is(DamageTypes.LAVA)) return false;
        if (has(player, AbilityRegistry.HEAT_IMMUNE) && source.is(DamageTypes.HOT_FLOOR)) return false;
        if (has(player, AbilityRegistry.FREEZE_IMMUNE) && source.is(DamageTypes.FREEZE)) return false;
        if (has(player, AbilityRegistry.FALL_IMMUNE) && source.is(DamageTypes.FALL)) return false;
        if (has(player, AbilityRegistry.BREATHES_UNDERWATER) && source.is(DamageTypes.DROWN)) return false;
        if (has(player, AbilityRegistry.PEARLING) && source.is(DamageTypes.ENDER_PEARL)) return false;

        return true;
    }
    private static boolean weakToDamage(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;
        if (!has(player, AbilityRegistry.WEAK_TO_DAMAGE)) return true;
        if (!source.is(DamageTypes.FALL)) return true;

        if (FdaApiUtil.getIntValue(player, PlayerAttachments.HURT_TICK) != entity.tickCount) {
            // safeguard to make sure ALLOW_DAMAGE doesn't get called again and for this to not run recursively
            FdaApiUtil.setIntValue(player, PlayerAttachments.HURT_TICK, entity.tickCount);
            entity.hurt(source, value * 1.25f);

            return false;
        }

        return true;
    }
    private static boolean squishy(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;
        if (!has(player, AbilityRegistry.WEAK_TO_DAMAGE)) return true;
        if (!(source.is(DamageTypes.FALL) || source.is(DamageTypes.FLY_INTO_WALL))) return true;

        if (FdaApiUtil.getIntValue(player, PlayerAttachments.HURT_TICK) != entity.tickCount) {
            // safeguard to make sure ALLOW_DAMAGE doesn't get called again and for this to not run recursively
            FdaApiUtil.setIntValue(player, PlayerAttachments.HURT_TICK, entity.tickCount);
            entity.hurt(source, value * 0.75f);

            return false;
        }

        return true;
    }

    private static InteractionResult pearling(Player interacting, Level level, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!has(player, AbilityRegistry.PEARLING)) return InteractionResult.PASS;

        ItemStack pearl = new ItemStack(Items.ENDER_PEARL);
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(pearl.getItem())) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(pearl)) return InteractionResult.PASS;

        int slot = player.getInventory().getSelectedSlot();
        ItemStack inSlot = player.getInventory().getItem(slot);
        if (inSlot.isEmpty()) {
            player.getInventory().setItem(slot, pearl);
        } else {
            player.getInventory().add(pearl); // Just in case
        }

        return InteractionResult.PASS;
    }

    private static void bugEaterItems(Player interacting, Level level, InteractionHand hand) {
        if (level.isClientSide()) return;
        if (!(interacting instanceof ServerPlayer player)) return;
        if (!has(player, AbilityRegistry.BUG_EATER)) return;

        ItemStack stack = player.getItemInHand(hand);
        FoodData food = player.getFoodData();

        if (isFull(food)) return;

        if (stack.is(DataTags.DIET_BUG_ITEMS) && !stack.has(DataComponents.FOOD)) {
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
        if (!has(player, AbilityRegistry.BUG_EATER)) return InteractionResult.PASS;

        FoodData food = player.getFoodData();
        if (isFull(food)) return InteractionResult.PASS;

        // Calculate saturation and nutrition
        int addNutrition = 0;
        float addSaturation = 0f;
        if (isType(entity, DataTags.DIET_BUG_ENTITY_NUTRITIOUS)) addNutrition = 2;
        if (isType(entity, DataTags.DIET_BUG_ENTITY_SATURATING)) addSaturation = 2f;
        int nutrition = 3 + addNutrition;
        float saturation = 2 + addSaturation;

        // Apply everything accordingly
        if (isType(entity, DataTags.DIET_BUG_ENTITY_GENERIC)) {
            if (!isBugLikeConsumable(entity)) return InteractionResult.PASS;

            entity.discard();
            food.add(nutrition, saturation);
            playEatSound(player);
            sendUpdatePacket(player);

            return InteractionResult.CONSUME;
        } else if (isType(entity, DataTags.DIET_BUG_ENTITY_FIRE)) {
            if (!isBugLikeConsumable(entity)) return InteractionResult.PASS;

            entity.discard();
            food.add(nutrition, saturation);
            playEatSound(player);
            player.hurt(player.damageSources().onFire(), 2f);
            sendUpdatePacket(player);

            return InteractionResult.CONSUME;
        } else if (isType(entity, DataTags.DIET_BUG_ENTITY_POISON)) {
            if (!isBugLikeConsumable(entity)) return InteractionResult.PASS;

            entity.discard();
            food.add(nutrition, saturation);
            playEatSound(player);
            DataManager.applyEffect(player, MobEffects.POISON, 200, 0);
            sendUpdatePacket(player);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
    private static InteractionResult grassEater(Player interacting, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!has(player, AbilityRegistry.GRASS_EATER)) return InteractionResult.PASS;

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();

        if (!level.getBlockState(pos).is(DataTags.DIET_FOLIAGE)) return InteractionResult.PASS;

        FoodData food = player.getFoodData();
        if (isFull(food)) return InteractionResult.PASS;
        level.destroyBlock(pos, false);
        food.add(2, 0.5F);

        // Update the client about eaten food
        sendUpdatePacket(player);

        return InteractionResult.SUCCESS;
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
        if (stack.getItem() instanceof BlockItem && !stack.has(DataComponents.FOOD)) return InteractionResult.PASS;

        bugEaterItems(interacting, level, hand); // Handle this first
        if (isDietBlocked(player, stack)) return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }

    // Helper methods
    private static boolean isDietBlocked(ServerPlayer player, ItemStack stack) {
        if (!stack.has(DataComponents.FOOD)) return false;

        boolean carnivore = has(player, AbilityRegistry.CARNIVORE),
            vegetarian = has(player, AbilityRegistry.VEGETARIAN),
            sweetOnly = has(player, AbilityRegistry.ONLY_EATS_SWEETS),
            grassEater = has(player, AbilityRegistry.GRASS_EATER),
            bugEater = has(player, AbilityRegistry.BUG_EATER),
            canConsumeGolden = has(player, ModifierRegistry.ADD_GOLD_FOODS_TO_DIET),

            isMeat = stack.is(DataTags.DIET_CARNIVORE),
            isVege = stack.is(DataTags.DIET_VEGETARIAN),
            isSweet = stack.is(DataTags.DIET_SWEETS),
            isBugLike = stack.is(DataTags.DIET_BUG_ITEMS),
            isGold = stack.is(DataTags.DIET_MODIFIER_GOLDEN_FOODS);

        if (!carnivore && !vegetarian && !sweetOnly && !grassEater && !bugEater) return false;

        if (canConsumeGolden && isGold) return false;
        if (carnivore && isMeat) return false;
        if (vegetarian && isVege) return false;
        if (sweetOnly && isSweet) return false;
        if (bugEater && isBugLike) return false;
        // No GRASS_EATER item tag for this to check. GRASS_EATER diet is handled by grassEater interaction method.

        return true;
    }
    private static boolean isFull(FoodData playerFoodData) {
        return !playerFoodData.needsFood();
    }
    private static void playEatSound(ServerPlayer player) {
        player.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F+(player.getRandom().nextFloat()-player.getRandom().nextFloat()) * 0.4F);
    }
    private static void sendUpdatePacket(ServerPlayer player) {
        FoodData food = player.getFoodData();
        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), food.getFoodLevel(), food.getSaturationLevel()));
    }
    private static boolean isType(Entity entity, TagKey<EntityType<?>> tag) {
        return entity.getType().is(tag);
    }
    private static boolean isBugLikeConsumable(Entity entity) {
        if (entity instanceof Slime slime) { /// Change to AbstractCubeMob with 26.2!
            return slime.getSize() == 1;
        }
        return true;
    }

    public static boolean shouldClimb(ServerPlayer player) {
        if (!(Config.playerAbilities.get())) return false;
        if (!has(player, AbilityRegistry.CLIMBS_WALLS)) return false;
        if (player.onGround()) return false;

        Level level = player.level();
        BlockPos pos = player.blockPosition();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos side = pos.relative(dir);
            if (level.getBlockState(side).isSolid() || level.getBlockState(side.above()).isSolid()) return true;
        }

        return false;
    }
}