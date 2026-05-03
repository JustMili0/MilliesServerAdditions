package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.commands.arguments.AbilitySetArgumentType;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

import static net.justmili.servertweaks.content.abilities.AbilityManager.*;

public class AbilityUtil {

    // Ability and Modifier management
    public static Set<Ability> getAbilities(ServerPlayer player) {
        return playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet());
    }
    public static void grantAbility(ServerPlayer player, Ability ability) {
        playerAbilities.computeIfAbsent(player.getUUID(), uuid -> new HashSet<>()).add(ability);
        saveFile(player.level().getServer());
    }
    public static void revokeAbility(ServerPlayer player, Ability ability) {
        playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet()).remove(ability);
        saveFile(player.level().getServer());
    }

    public static Set<AbilityModifier> getModifiers(ServerPlayer player) {
        return playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet());
    }
    public static void grantModifier(ServerPlayer player, AbilityModifier modifier) {
        playerModifiers.computeIfAbsent(player.getUUID(), uuid -> new HashSet<>()).add(modifier);
        saveFile(player.level().getServer());
    }
    public static void revokeModifier(ServerPlayer player, AbilityModifier modifier) {
        playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet()).remove(modifier);
        saveFile(player.level().getServer());
    }

    public static boolean has(ServerPlayer player, Ability ability) { return getAbilities(player).contains(ability); }
    public static boolean has(ServerPlayer player, AbilityModifier modifier) { return getModifiers(player).contains(modifier); }

    public static void applySet(UUID uuid, AbilitySetArgumentType.AbilityPreset set, MinecraftServer server) {
        playerAbilities.put(uuid, new HashSet<>(set.abilities()));
        playerModifiers.put(uuid, new HashSet<>(set.modifiers()));
        saveFile(server);
    }
    public static void clearPlayer(ServerPlayer player) {
        UUID uuid = player.getUUID();
        MinecraftServer server = player.level().getServer();
        playerAbilities.remove(uuid);
        playerModifiers.remove(uuid);
        saveFile(server);
    }

    // Ability and Modifier effects management
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

    public record MobData(Class<?> entityClass, double range, double speed) { }
    public static <T extends Mob> void executeForNearby(ServerPlayer player, List<MobData> dataList, BiConsumer<T, MobData> action) {
        dataList.forEach(data ->
            getNearby(player, data.entityClass().asSubclass(Mob.class), data.range())
                .forEach(mob -> action.accept((T) mob, data))
        );
    }
}
