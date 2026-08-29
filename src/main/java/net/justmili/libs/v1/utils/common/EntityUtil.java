package net.justmili.libs.v1.utils.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityUtil {

    public static void applyEffect(LivingEntity entity, Holder<MobEffect> effects, int duration, int power) {
        entity.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }

    public static void tp(LivingEntity entity, ServerLevel level, double x, double y, double z, float rotY, float rotX, boolean resetCameraToBody) {
        entity.teleportTo(level, x, y, z, Relative.DELTA, rotY, rotX, resetCameraToBody);
    }

    public static void tp(Player player, double x, double y, double z, float rotY, float rotX) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        serverPlayer.connection.teleport(x, y, z, rotY, rotX);
    }

    public static void tp(LivingEntity entity, Level level, double x, double y, double z, float rotY, float rotX, boolean resetCameraToBody) {
        tp(entity, (ServerLevel) level, x, y, z, rotY, rotX, resetCameraToBody);
    }

    public static void tp(LivingEntity entity, ServerLevel level, double x, double y, double z, boolean resetCameraToBody) {
        tp(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void tp(LivingEntity entity, Level level, double x, double y, double z, boolean resetCameraToBody) {
        tp(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), resetCameraToBody);
    }

    public static void tp(LivingEntity entity, ServerLevel level, double x, double y, double z, float rotY, float rotX) {
        tp(entity, level, x, y, z, rotY, rotX, true);
    }

    public static void tp(LivingEntity entity, Level level, double x, double y, double z, float rotY, float rotX) {
        tp(entity, level, x, y, z, rotY, rotX, true);
    }

    public static void tp(LivingEntity entity, ServerLevel level, double x, double y, double z) {
        tp(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), true);
    }

    public static void tp(LivingEntity entity, Level level, double x, double y, double z) {
        tp(entity, level, x, y, z, entity.getYRot(), entity.getXRot(), true);
    }

    public static void tp(Player player, BlockPos pos, float rotY, float rotX) {
        tp(player, pos.getX(), pos.getY(), pos.getZ(), rotY, rotX);
    }

    public static void tp(Player player, Vec3 pos, float rotY, float rotX) {
        tp(player, pos.x, pos.y, pos.z, rotY, rotX);
    }

    public static void tp(Player player, double x, double y, double z) {
        tp(player, x, y, z, player.getYRot(), player.getXRot());
    }

    public static void tp(Player player, BlockPos pos) {
        tp(player, pos, player.getYRot(), player.getXRot());
    }

    public static void tp(Player player, Vec3 pos) {
        tp(player, pos, player.getYRot(), player.getXRot());
    }

    public static void useStack(Player player, InteractionHand hand, int shrinkAmount) {
        if (!player.isCreative()) player.getItemInHand(hand).shrink(shrinkAmount);
    }
    public static void useStack(Player player, InteractionHand hand) {
        useStack(player, hand, 1);
    }
    public static void useStackWithResult(Player player, InteractionHand hand, ItemLike result, boolean shrinkStack, int shrinkAmount) {
        var stack = player.getItemInHand(hand);
        var item = new ItemStack(result);

        if (shrinkStack) useStack(player, hand, shrinkAmount);
        if (stack.isEmpty()) {
            player.setItemInHand(hand, item);
        } else if (!player.getInventory().add(item)) {
            player.drop(item, false);
        }
    }
    public static void useStackWithResult(Player player, InteractionHand hand, ItemLike result, int shrinkAmount) {
        useStackWithResult(player, hand, result, true, shrinkAmount);
    }
    public static void useStackWithResult(Player player, InteractionHand hand, ItemLike result, boolean shrinkStack) {
        useStackWithResult(player, hand, result, shrinkStack, 1);
    }
    public static void useStackWithResult(Player player, InteractionHand hand, ItemLike result) {
        useStackWithResult(player, hand, result, true, 1);
    }

    public record MobData(Class<?> entityClass, double range, double runSpeed) { }

    public static <T extends Mob> List<T> getNearby(ServerPlayer player, Class<T> mob, double radius) {
        return player.level().getEntitiesOfClass(mob, player.getBoundingBox().inflate(radius));
    }

    // For a list of entities within an area of a player
    public static <T extends Mob> void executeForNearby(ServerPlayer player, List<MobData> dataList, BiConsumer<T, MobData> action) {
        dataList.forEach(data ->
            getNearby(player, data.entityClass().asSubclass(Mob.class), data.range())
                .forEach(mob -> action.accept((T) mob, data))
        );
    }

    // For a single entity
    public static <T extends Mob> void executeForNearby(ServerPlayer player, Class<?> entityClass, double range, Consumer<T> action) {
        getNearby(player, entityClass.asSubclass(Mob.class), range)
            .forEach(mob -> action.accept((T) mob));
    }

    public static <T extends Mob> void executeForNearby(ServerPlayer player, Class<?> entityClass, double range, double speed, BiConsumer<T, Double> action) {
        getNearby(player, entityClass.asSubclass(Mob.class), range)
            .forEach(mob -> action.accept((T) mob, speed));
    }
}