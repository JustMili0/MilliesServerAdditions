package net.justmili.libs.v1.utils;

import net.justmili.libs.v1.data.MobData;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;
import java.util.function.BiConsumer;

public class EntityUtil {
    // Player
    public static void applyEffect(ServerPlayer player, Holder<MobEffect> effects, int duration, int power) {
        player.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }

    // Non-player
    public static void applyEffect(LivingEntity entity, Holder<MobEffect> effects, int duration, int power) {
        entity.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }

    // Generic
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
}