package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.content.abilities.arguments.PresetArgumentType;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

import static net.justmili.servertweaks.content.abilities.DataStorage.*;

@SuppressWarnings("unchecked")
public class DataManager {
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

    public static void applySet(UUID uuid, PresetArgumentType.AbilityPreset set, MinecraftServer server) {
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
}
