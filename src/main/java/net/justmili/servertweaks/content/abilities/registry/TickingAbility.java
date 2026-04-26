package net.justmili.servertweaks.content.abilities.registry;

import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TickingAbility extends Ability {

    public TickingAbility(String name) {
        super(name);
    }

    public abstract void tick(ServerPlayer player, ServerLevel level);

    // Ticking abilities helper methods
    public static void applyEffect(ServerPlayer player, Holder<@NotNull MobEffect> effects, int duration, int power) {
        player.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }
    public static void applyEffect(ServerPlayer player, Holder<@NotNull MobEffect> effects) {
        player.addEffect(new MobEffectInstance(effects, 100, 0, false, false, false));
    }
    public static void applyEffect(ServerPlayer player, Holder<@NotNull MobEffect> effects, int power) {
        player.addEffect(new MobEffectInstance(effects, 100, power, false, false, false));
    }
    public static <T extends Mob> List<T> getNearby(ServerPlayer player, Class<T> mob, double radius) {
        return player.level().getEntitiesOfClass(mob, player.getBoundingBox().inflate(radius));
    }
}