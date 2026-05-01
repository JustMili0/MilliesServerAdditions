package net.justmili.servertweaks.content.abilities.registry;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.AbilityUtil;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.TickingAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Set;

public class AbilityEffects {
    public static void registerAbilityEvents() {
        if (!(Config.playerAbilities.get())) return;
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                AbilityEffects.tickTickingAbilities(player);
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(AbilityEffects::specialDamageImmune);

        UseBlockCallback.EVENT.register(AbilityEffects::grassEater);
        UseItemCallback.EVENT.register(AbilityEffects::dietRestrictionsOnItem);
        UseBlockCallback.EVENT.register(AbilityEffects::dietRestrictionsOnBlock);
    }

    private static void tickTickingAbilities(Player ticking) {
        if (!(ticking instanceof ServerPlayer player)) return;
        ServerLevel level = player.level();
        Set<Ability> abilities = AbilityUtil.getAbilities(player);

        for (Ability ability : abilities) {
            if (ability instanceof TickingAbility tickingAbility) {
                tickingAbility.tick(player, level);
            }
        }
    }

    private static boolean specialDamageImmune(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;
        Set<Ability> abilities = AbilityUtil.getAbilities(player);

        if (abilities.contains(AbilitiesRegistry.FIRE_IMMUNE) && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)
            || source.is(DamageTypes.LAVA) || source.is(DamageTypes.HOT_FLOOR))) return false;
        if (abilities.contains(AbilitiesRegistry.FREEZE_IMMUNE) && source.is(DamageTypes.FREEZE)) return false;
        if (abilities.contains(AbilitiesRegistry.FALL_IMMUNE) && source.is(DamageTypes.FALL)) return false;
        if (abilities.contains(AbilitiesRegistry.BREATHES_UNDERWATER) && source.is(DamageTypes.DROWN)) return false;

        return true;
    }

    private static InteractionResult grassEater(Player interacting, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if (world.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();

        if (!AbilityUtil.has(player, AbilitiesRegistry.GRASS_EATER)) return InteractionResult.PASS;
        if (!world.getBlockState(pos).is(AbilityTags.DIET_FOLIAGE)) return InteractionResult.PASS;

        FoodData food = player.getFoodData();
        if (!(food.getFoodLevel() < 20 || food.getSaturationLevel() < 20)) return InteractionResult.PASS;
        world.destroyBlock(pos, false);
        food.eat(2, 0.5F);
        player.swing(InteractionHand.MAIN_HAND, true);

        // Update the client about eaten food
        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), food.getFoodLevel(), food.getSaturationLevel()));

        return InteractionResult.SUCCESS;
    }

    public static boolean shouldClimb(ServerPlayer player) {
        if (!(Config.playerAbilities.get())) return false;
        if (!AbilityUtil.has(player, AbilitiesRegistry.CLIMBS_WALLS)) return false;
        if (player.onGround()) return false;
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos side = pos.relative(dir);
            if (level.getBlockState(side).isSolid() || level.getBlockState(side.above()).isSolid()) return true;
        }
        return false;
    }

    /*
    Current idea:
    - Prevent player from eating what's not in their diet
    - Apply constant nausea if attempting to eat something that isn't in player's diet
     */
    // TODO: FIX - NAUSEA IS NOT GETTING APPLIED
    private static InteractionResult dietRestrictionsOnItem(Player interacting, Level level, InteractionHand hand) { // Clicking while looking at nothing
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (isDietBlocked(player, player.getItemInHand(hand))) {
            AbilityUtil.applyEffect(player, MobEffects.NAUSEA, 60, 1);
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult dietRestrictionsOnBlock(Player interacting, Level world, InteractionHand hand, BlockHitResult hitResult) { // Clicking while looking at a block
        if (world.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (isDietBlocked(player, player.getItemInHand(hand))) {
            AbilityUtil.applyEffect(player, MobEffects.NAUSEA, 60, 1);
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    private static boolean isDietBlocked(ServerPlayer player, ItemStack stack) {
        if (!stack.has(DataComponents.FOOD)) return false;
        Set<Ability> abilities = AbilityUtil.getAbilities(player);

        boolean carnivore = abilities.contains(AbilitiesRegistry.CARNIVORE);
        boolean vegetarian = abilities.contains(AbilitiesRegistry.VEGETARIAN);
        boolean sweetOnly = abilities.contains(AbilitiesRegistry.ONLY_EATS_SWEETS);
        boolean grassEater = abilities.contains(AbilitiesRegistry.GRASS_EATER);
        boolean canConsumeGolden = AbilityUtil.has(player, AbilityModifierRegistry.ADD_GOLD_FOODS_TO_DIET);

        boolean isMeat = stack.is(AbilityTags.DIET_CARNIVORE);
        boolean isVege = stack.is(AbilityTags.DIET_VEGETARIAN);
        boolean isSweet = stack.is(AbilityTags.DIET_SWEETS);
        boolean isGold = stack.is(AbilityTags.DIET_MODIFIER_GOLDEN_FOODS);

        if (!carnivore && !vegetarian && !sweetOnly && !grassEater) return false;

        if (canConsumeGolden && isGold) return false;
        if (carnivore && isMeat) return false;
        if (vegetarian && isVege) return false;
        if (sweetOnly && isSweet) return false;
        // No GRASS_EATER item tag for this to check. GRASS_EATER diet is handled by grassEater interaction method.

        return true;
    }
}