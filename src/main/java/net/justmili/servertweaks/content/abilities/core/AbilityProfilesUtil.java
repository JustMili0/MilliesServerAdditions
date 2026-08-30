package net.justmili.servertweaks.content.abilities.core;

import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.justmili.servertweaks.content.abilities.type.Modifier;
import net.justmili.servertweaks.content.abilities.type.Preset;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.justmili.servertweaks.content.abilities.core.AbilityProfiles.*;

public class AbilityProfilesUtil {
    // Ability and Modifier management
    public static void grantAbility(Player player, Ability ability) {
        if (ability == null) {
            warnUnknownType(player, "ability");
            return;
        }
        ABILITIES.computeIfAbsent(player.getUUID(), _ -> new HashSet<>()).add(ability);
        saveProfiles();
    }

    public static void grantDebuff(Player player, Debuff debuff) {
        if (debuff == null) {
            warnUnknownType(player, "debuff");
            return;
        }
        DEBUFFS.computeIfAbsent(player.getUUID(), _ -> new HashSet<>()).add(debuff);
        saveProfiles();
    }

    public static void grantModifier(ServerPlayer player, Modifier modifier) {
        if (modifier == null) {
            warnUnknownType(player, "ability/debuff modifier");
            return;
        }
        MODIFIERS.computeIfAbsent(player.getUUID(), _ -> new HashSet<>()).add(modifier);
        saveProfiles();
    }

    public static void revokeAbility(Player player, Ability ability) {
        if (ability == null) {
            warnUnknownType(player, "ability");
            return;
        }
        ABILITIES.getOrDefault(player.getUUID(), Collections.emptySet()).remove(ability);
        saveProfiles();
    }

    public static void revokeDebuff(Player player, Debuff debuff) {
        if (debuff == null) {
            warnUnknownType(player, "debuff");
            return;
        }
        DEBUFFS.getOrDefault(player.getUUID(), Collections.emptySet()).remove(debuff);
        saveProfiles();
    }

    public static void revokeModifier(Player player, Modifier modifier) {
        if (modifier == null) {
            warnUnknownType(player, "ability/debuff modifier");
            return;
        }
        MODIFIERS.getOrDefault(player.getUUID(), Collections.emptySet()).remove(modifier);
        saveProfiles();
    }

    public static Set<Ability> getAbilities(Player player) {
        return ABILITIES.getOrDefault(player.getUUID(), Collections.emptySet());
    }

    public static Set<Debuff> getDebuffs(Player player) {
        return DEBUFFS.getOrDefault(player.getUUID(), Collections.emptySet());
    }

    public static Set<Modifier> getModifiers(Player player) {
        return MODIFIERS.getOrDefault(player.getUUID(), Collections.emptySet());
    }

    public static boolean has(Player player, Ability ability) {
        if (ability == null) {
            warnUnknownType(player, "ability");
            return false;
        }
        return getAbilities(player).contains(ability);
    }

    public static boolean has(Player player, Debuff debuff) {
        if (debuff == null) {
            warnUnknownType(player, "debuff");
            return false;
        }
        return getDebuffs(player).contains(debuff);
    }

    public static boolean has(Player player, Modifier modifier) {
        if (modifier == null) {
            warnUnknownType(player, "ability/debuff modifier");
            return false;
        }
        return getModifiers(player).contains(modifier);
    }

    public static void applyPreset(Player player, Preset preset) {
        if (preset == null) {
            CommandUtil.sendFailTo((ServerPlayer) player, "Unknown abilities preset");
            return;
        }
        var uuid = player.getUUID();
        ABILITIES.put(uuid, new HashSet<>(preset.getAbilities()));
        DEBUFFS.put(uuid, new HashSet<>(preset.getDebuffs()));
        MODIFIERS.put(uuid, new HashSet<>(preset.getModifiers()));
        saveProfiles();
    }

    public static void clearPlayerProfile(Player player) {
        var uuid = player.getUUID();
        ABILITIES.remove(uuid);
        DEBUFFS.remove(uuid);
        MODIFIERS.remove(uuid);
        saveProfiles();
    }

    public static void warnUnknownType(Player player, String label) {
        CommandUtil.sendFailTo((ServerPlayer) player, "Unknown player " + label);
    }
}