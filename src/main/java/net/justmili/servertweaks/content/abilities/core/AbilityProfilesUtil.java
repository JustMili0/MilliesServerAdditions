package net.justmili.servertweaks.content.abilities.core;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.justmili.corelibs.v1.utils.common.CommandUtil;
import net.justmili.corelibs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.justmili.servertweaks.content.abilities.type.Modifier;
import net.justmili.servertweaks.content.abilities.type.Preset;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

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
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    public static void grantDebuff(Player player, Debuff debuff) {
        if (debuff == null) {
            warnUnknownType(player, "debuff");
            return;
        }
        DEBUFFS.computeIfAbsent(player.getUUID(), _ -> new HashSet<>()).add(debuff);
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    public static void grantModifier(Player player, Modifier modifier) {
        if (modifier == null) {
            warnUnknownType(player, "ability/debuff modifier");
            return;
        }
        MODIFIERS.computeIfAbsent(player.getUUID(), _ -> new HashSet<>()).add(modifier);
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    public static void revokeAbility(Player player, Ability ability) {
        if (ability == null) {
            warnUnknownType(player, "ability");
            return;
        }
        ABILITIES.getOrDefault(player.getUUID(), Collections.emptySet()).remove(ability);
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    public static void revokeDebuff(Player player, Debuff debuff) {
        if (debuff == null) {
            warnUnknownType(player, "debuff");
            return;
        }
        DEBUFFS.getOrDefault(player.getUUID(), Collections.emptySet()).remove(debuff);
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    public static void revokeModifier(Player player, Modifier modifier) {
        if (modifier == null) {
            warnUnknownType(player, "ability/debuff modifier");
            return;
        }
        MODIFIERS.getOrDefault(player.getUUID(), Collections.emptySet()).remove(modifier);
        saveProfiles(getServer(player));
        syncToClient(player);
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
            if (!isClient(player)) warnUnknownType(player, "ability");
            return false;
        }
        if (isClient(player)) return getForClient(player, PlayerVars.SYNCED_ABILITIES, ability);
        return getAbilities(player).contains(ability);
    }

    public static boolean has(Player player, Debuff debuff) {
        if (debuff == null) {
            if (!isClient(player)) warnUnknownType(player, "debuff");
            return false;
        }
        if (isClient(player)) return getForClient(player, PlayerVars.SYNCED_DEBUFFS, debuff);
        return getDebuffs(player).contains(debuff);
    }

    public static boolean has(Player player, Modifier modifier) {
        if (modifier == null) {
            if (!isClient(player)) warnUnknownType(player, "ability/debuff modifier");
            return false;
        }
        if (isClient(player)) return getForClient(player, PlayerVars.SYNCED_MODIFIERS, modifier);
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
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    public static void clearPlayerProfile(Player player) {
        var uuid = player.getUUID();
        ABILITIES.remove(uuid);
        DEBUFFS.remove(uuid);
        MODIFIERS.remove(uuid);
        saveProfiles(getServer(player));
        syncToClient(player);
    }

    static void syncToClient(Player player) {
        FdaUtil.set(player, PlayerVars.SYNCED_ABILITIES, Set.copyOf(getAbilities(player)));
        FdaUtil.set(player, PlayerVars.SYNCED_DEBUFFS, Set.copyOf(getDebuffs(player)));
        FdaUtil.set(player, PlayerVars.SYNCED_MODIFIERS, Set.copyOf(getModifiers(player)));
    }

    public static void warnUnknownType(Player player, String label) {
        CommandUtil.sendFailTo((ServerPlayer) player, "Unknown player " + label);
    }

    static @Nullable MinecraftServer getServer(Player player) {
        return player.level().getServer();
    }

    static boolean isClient(Player player) {
        return player.level().isClientSide();
    }

    static <A> boolean getForClient(Player player, AttachmentType<Set<A>> variable, A type) {
        return FdaUtil.get(player, variable, Set.of()).contains(type);
    }
}