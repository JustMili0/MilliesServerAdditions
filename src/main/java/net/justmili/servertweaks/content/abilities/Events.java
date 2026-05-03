package net.justmili.servertweaks.content.abilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.data.DataTags;
import net.justmili.servertweaks.content.abilities.registries.AbilityRegistry;
import net.justmili.servertweaks.content.abilities.registries.ModifierRegistry;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.TickingAbility;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Set;

public class Events {
    public static void registerAbilityEvents() {
        if (!(Config.playerAbilities.get())) return;
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ServerLevel level = player.level();
                Set<Ability> abilities = DataManager.getAbilities(player);

                // Tick all Ticking Abilities
                for (Ability ability : abilities) {
                    if (ability instanceof TickingAbility tickingAbility) {
                        tickingAbility.tick(player, level);
                    }
                }

                // Reset attribute modifiers if related ability is not applied
                AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
                AttributeInstance attack = player.getAttribute(Attributes.ATTACK_DAMAGE);
                AttributeInstance maxHp = player.getAttribute(Attributes.MAX_HEALTH);

                if (!abilities.contains(AbilityRegistry.SLOW)) speed.removeModifier(AbilityRegistry.AM_SLOW_SPEED);
                if (!abilities.contains(AbilityRegistry.STRONG)) attack.removeModifier(AbilityRegistry.AM_STRONG_DAMAGE);
                if (!abilities.contains(AbilityRegistry.STRONG)) maxHp.removeModifier(AbilityRegistry.AM_STRONG_HP);
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Events::specialDamageImmune);

        UseItemCallback.EVENT.register(Events::pearling);
        UseBlockCallback.EVENT.register(Events::grassEater);
        UseItemCallback.EVENT.register(Events::dietRestrictionsOnItem);
        UseBlockCallback.EVENT.register(Events::dietRestrictionsOnBlock);
    }

    private static boolean specialDamageImmune(LivingEntity entity, DamageSource source, float value) {
        if (!(entity instanceof ServerPlayer player)) return true;
        Set<Ability> abilities = DataManager.getAbilities(player);

        if (abilities.contains(AbilityRegistry.FIRE_IMMUNE) && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE))) return false;
        if (abilities.contains(AbilityRegistry.LAVA_IMMUNE) && source.is(DamageTypes.LAVA)) return false;
        if (abilities.contains(AbilityRegistry.HEAT_IMMUNE) && source.is(DamageTypes.HOT_FLOOR)) return false;
        if (abilities.contains(AbilityRegistry.FREEZE_IMMUNE) && source.is(DamageTypes.FREEZE)) return false;
        if (abilities.contains(AbilityRegistry.FALL_IMMUNE) && source.is(DamageTypes.FALL)) return false;
        if (abilities.contains(AbilityRegistry.BREATHES_UNDERWATER) && source.is(DamageTypes.DROWN)) return false;
        if (abilities.contains(AbilityRegistry.PEARLING) && source.is(DamageTypes.ENDER_PEARL)) return false;

        return true;
    }

    private static InteractionResult pearling(Player interacting, Level level, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;

        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        ItemStack pearl = new ItemStack(Items.ENDER_PEARL);
        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.is(pearl.getItem())) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(pearl)) return InteractionResult.PASS;
        if (!DataManager.has(player, AbilityRegistry.PEARLING)) return InteractionResult.PASS;

        int slot = player.getInventory().getSelectedSlot();
        ItemStack inSlot = player.getInventory().getItem(slot);
        if (inSlot.isEmpty()) {
            player.getInventory().setItem(slot, pearl);
        } else {
            player.getInventory().add(pearl); // Just in case
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult grassEater(Player interacting, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();

        if (!DataManager.has(player, AbilityRegistry.GRASS_EATER)) return InteractionResult.PASS;
        if (!level.getBlockState(pos).is(DataTags.DIET_FOLIAGE)) return InteractionResult.PASS;

        FoodData food = player.getFoodData();
        if (!(food.getFoodLevel() < 20 || food.getSaturationLevel() < 20)) return InteractionResult.PASS;
        level.destroyBlock(pos, false);
        food.eat(2, 0.5F);
        player.swing(InteractionHand.MAIN_HAND, true);

        // Update the client about eaten food
        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), food.getFoodLevel(), food.getSaturationLevel()));

        return InteractionResult.SUCCESS;
    }
    private static InteractionResult dietRestrictionsOnItem(Player interacting, Level level, InteractionHand hand) { // Clicking while looking at nothing
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;

        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (isDietBlocked(player, player.getItemInHand(hand))) return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }
    private static InteractionResult dietRestrictionsOnBlock(Player interacting, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;

        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof BlockItem && !stack.has(DataComponents.FOOD)) return InteractionResult.PASS;

        if (isDietBlocked(player, stack)) return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }
    private static boolean isDietBlocked(ServerPlayer player, ItemStack stack) {
        if (!stack.has(DataComponents.FOOD)) return false;
        Set<Ability> abilities = DataManager.getAbilities(player);

        boolean carnivore = abilities.contains(AbilityRegistry.CARNIVORE);
        boolean vegetarian = abilities.contains(AbilityRegistry.VEGETARIAN);
        boolean sweetOnly = abilities.contains(AbilityRegistry.ONLY_EATS_SWEETS);
        boolean grassEater = abilities.contains(AbilityRegistry.GRASS_EATER);
        boolean canConsumeGolden = DataManager.has(player, ModifierRegistry.ADD_GOLD_FOODS_TO_DIET);

        boolean isMeat = stack.is(DataTags.DIET_CARNIVORE);
        boolean isVege = stack.is(DataTags.DIET_VEGETARIAN);
        boolean isSweet = stack.is(DataTags.DIET_SWEETS);
        boolean isGold = stack.is(DataTags.DIET_MODIFIER_GOLDEN_FOODS);

        if (!carnivore && !vegetarian && !sweetOnly && !grassEater) return false;

        if (canConsumeGolden && isGold) return false;
        if (carnivore && isMeat) return false;
        if (vegetarian && isVege) return false;
        if (sweetOnly && isSweet) return false;
        // No GRASS_EATER item tag for this to check. GRASS_EATER diet is handled by grassEater interaction method.

        return true;
    }

    public static boolean shouldClimb(ServerPlayer player) {
        if (!(Config.playerAbilities.get())) return false;
        if (!DataManager.has(player, AbilityRegistry.CLIMBS_WALLS)) return false;
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