package net.justmili.servertweaks.content.abilities.core;

import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.justmili.servertweaks.content.abilities.core.AbilityProfiles.*;

public class AbilityProfilesUtil {
    // Ability and Modifier management
    public static Set<Ability> getAbilities(Player player) {
        return playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet());
    }

    public static void grantAbility(Player player, Ability ability) {
        if (ability == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown player ability");
            return;
        }
        playerAbilities.computeIfAbsent(player.getUUID(), _ -> new HashSet<>()).add(ability);
        saveFile(player.level().getServer());
    }

    public static void revokeAbility(Player player, Ability ability) {
        if (ability == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown player ability");
            return;
        }
        playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet()).remove(ability);
        saveFile(player.level().getServer());
    }

    public static Set<AbilityModifier> getModifiers(Player player) {
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

    public static void revokeModifier(Player player, AbilityModifier modifier) {
        if (modifier == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown ability modifier");
            return;
        }
        playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet()).remove(modifier);
        saveFile(player.level().getServer());
    }

    public static boolean has(Player player, Ability ability) {
        if (ability == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown player ability");
            return false;
        }
        return getAbilities(player).contains(ability);
    }

    public static boolean has(Player player, AbilityModifier modifier) {
        if (modifier == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown ability modifier");
            return false;
        }
        return getModifiers(player).contains(modifier);
    }

    public static void applyPreset(Player player, MinecraftServer server, AbilityPreset preset) {
        if (preset == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown abilities preset");
            return;
        }
        var uuid = player.getUUID();
        playerAbilities.put(uuid, new HashSet<>(preset.getAbilities()));
        playerModifiers.put(uuid, new HashSet<>(preset.getModifiers()));
        saveFile(server);
    }

    public static void clearPlayerProfile(Player player) {
        var uuid = player.getUUID();
        var server = player.level().getServer();
        playerAbilities.remove(uuid);
        playerModifiers.remove(uuid);
        saveFile(server);
    }
}
