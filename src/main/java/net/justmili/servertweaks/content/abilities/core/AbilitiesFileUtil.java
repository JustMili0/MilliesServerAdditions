package net.justmili.servertweaks.content.abilities.core;

import net.justmili.libs.v1.utils.CommandUtil;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.justmili.servertweaks.content.abilities.core.FileManager.*;

public class AbilitiesFileUtil {
    // Ability and Modifier management
    public static Set<Ability> getAbilities(ServerPlayer player) {
        return playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet());
    }
    public static void grantAbility(ServerPlayer player, Ability ability) {
        if (ability == null) {
            CommandUtil.sendFailTo(player, "Unknown player ability");
            return;
        }
        playerAbilities.computeIfAbsent(player.getUUID(), uuid -> new HashSet<>()).add(ability);
        saveFile(player.level().getServer());
    }
    public static void revokeAbility(ServerPlayer player, Ability ability) {
        if (ability == null) {
            CommandUtil.sendFailTo(player, "Unknown player ability");
            return;
        }
        playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet()).remove(ability);
        saveFile(player.level().getServer());
    }

    public static Set<AbilityModifier> getModifiers(ServerPlayer player) {
        return playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet());
    }
    public static void grantModifier(ServerPlayer player, AbilityModifier modifier) {
        if (modifier == null) {
            CommandUtil.sendFailTo(player, "Unknown ability modifier");
            return;
        }
        playerModifiers.computeIfAbsent(player.getUUID(), uuid -> new HashSet<>()).add(modifier);
        saveFile(player.level().getServer());
    }
    public static void revokeModifier(ServerPlayer player, AbilityModifier modifier) {
        if (modifier == null) {
            CommandUtil.sendFailTo(player, "Unknown ability modifier");
            return;
        }
        playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet()).remove(modifier);
        saveFile(player.level().getServer());
    }

    public static boolean has(ServerPlayer player, Ability ability) {
        if (ability == null) {
            CommandUtil.sendFailTo(player, "Unknown player ability");
            return false;
        }
        return getAbilities(player).contains(ability);
    }
    public static boolean has(ServerPlayer player, AbilityModifier modifier) {
        if (modifier == null) {
            CommandUtil.sendFailTo(player, "Unknown ability modifier");
            return false;
        }
        return getModifiers(player).contains(modifier);
    }

    public static void applyPreset(ServerPlayer player, MinecraftServer server, AbilityPreset preset) {
        if (preset == null) {
            CommandUtil.sendFailTo(player, "Unknown abilities preset");
            return;
        }
        var uuid = player.getUUID();
        playerAbilities.put(uuid, new HashSet<>(preset.getAbilities()));
        playerModifiers.put(uuid, new HashSet<>(preset.getModifiers()));
        saveFile(server);
    }
    public static void clearPlayerProfile(ServerPlayer player) {
        var uuid = player.getUUID();
        MinecraftServer server = player.level().getServer();
        playerAbilities.remove(uuid);
        playerModifiers.remove(uuid);
        saveFile(server);
    }
}
