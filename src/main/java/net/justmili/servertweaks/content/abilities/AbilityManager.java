package net.justmili.servertweaks.content.abilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilitiesRegistry;
import net.justmili.servertweaks.content.abilities.registry.AbilityModifierRegistry;
import net.justmili.servertweaks.content.commands.arguments.AbilitySetArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AbilityManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "player_abilities.json";
    private static final Map<UUID, Set<Ability>> playerAbilities = new HashMap<>();
    private static final Map<UUID, Set<AbilityModifier>> playerModifiers = new HashMap<>();

    public static void loadFile(MinecraftServer server) {
        playerAbilities.clear();
        playerModifiers.clear();
        if (!(Config.playerAbilities.get())) return;

        File file = getFile();
        if (!file.exists()) {
            saveFile(server);
            return;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            for (var entry : root.entrySet()) {
                UUID uuid;
                try { uuid = UUID.fromString(entry.getKey()); }
                catch (IllegalArgumentException e) {
                    ServerTweaks.LOGGER.warn("Invalid UUID '{}', skipping", entry.getKey());
                    continue;
                }

                JsonObject object = entry.getValue().getAsJsonObject();
                Set<Ability> abilities = new LinkedHashSet<>();
                if (object.has("abilities")) {
                    for (var element : object.getAsJsonArray("abilities")) {
                        Ability ability = AbilitiesRegistry.byName(element.getAsString());
                        if (ability == null) { 
                            ServerTweaks.LOGGER.warn("Unknown ability '{}', skipping", element.getAsString());
                            continue; 
                        }
                        abilities.add(ability);
                    }
                }
                Set<AbilityModifier> modifiers = new LinkedHashSet<>();
                if (object.has("ability_modifiers")) {
                    for (var element : object.getAsJsonArray("ability_modifiers")) {
                        AbilityModifier modifier = AbilityModifierRegistry.byName(element.getAsString());
                        if (modifier == null) { 
                            ServerTweaks.LOGGER.warn("Unknown modifier '{}', skipping", element.getAsString()); continue;
                        }
                        modifiers.add(modifier);
                    }
                }
                playerAbilities.put(uuid, abilities);
                playerModifiers.put(uuid, modifiers);
            }
        } catch (Exception e) {
            ServerTweaks.LOGGER.error("Failed to read config: {}", e.getMessage());
        }
    }
    public static void saveFile(MinecraftServer server) {
        JsonObject root = new JsonObject();

        Set<UUID> allUuids = new HashSet<>(playerAbilities.keySet());
        allUuids.addAll(playerModifiers.keySet());

        for (UUID uuid : allUuids) {
            JsonObject playerObj = new JsonObject();

            ServerPlayer online = server.getPlayerList().getPlayer(uuid);
            if (online != null) playerObj.addProperty("name", online.getName().getString());

            JsonArray abilitiesArr = new JsonArray();
            Set<Ability> abilities = playerAbilities.getOrDefault(uuid, Collections.emptySet());
            abilities.stream().map(Ability::getName).sorted().forEach(abilitiesArr::add);
            playerObj.add("abilities", abilitiesArr);

            JsonArray modifiersArr = new JsonArray();
            Set<AbilityModifier> modifiers = playerModifiers.getOrDefault(uuid, Collections.emptySet());
            modifiers.stream().map(AbilityModifier::getName).sorted().forEach(modifiersArr::add);
            playerObj.add("ability_modifiers", modifiersArr);

            root.add(uuid.toString(), playerObj);
        }

        try {
            File file = getFile();
            file.getParentFile().mkdirs();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
            ServerTweaks.LOGGER.info("Saved abilities for {} player(s)", allUuids.size());
        } catch (Exception e) {
            ServerTweaks.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    public static Set<Ability> getAbilities(ServerPlayer player) {
        return playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet());
    }
    public static Set<AbilityModifier> getModifiers(ServerPlayer player) {
        return playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet());
    }
    public static void grantAbility(ServerPlayer player, Ability ability) {
        playerAbilities.computeIfAbsent(player.getUUID(), uuid -> new HashSet<>()).add(ability);
        saveFile(player.level().getServer());
    }
    public static void revokeAbility(ServerPlayer player, Ability ability) {
        playerAbilities.getOrDefault(player.getUUID(), Collections.emptySet()).remove(ability);
        saveFile(player.level().getServer());
    }
    public static void grantModifier(ServerPlayer player, AbilityModifier modifier) {
        playerModifiers.computeIfAbsent(player.getUUID(), uuid -> new HashSet<>()).add(modifier);
        saveFile(player.level().getServer());
    }
    public static void revokeModifier(ServerPlayer player, AbilityModifier modifier) {
        playerModifiers.getOrDefault(player.getUUID(), Collections.emptySet()).remove(modifier);
        saveFile(player.level().getServer());
    }
    public static void clearPlayer(ServerPlayer player) {
        UUID uuid = player.getUUID();
        MinecraftServer server = player.level().getServer();
        playerAbilities.remove(uuid);
        playerModifiers.remove(uuid);
        saveFile(server);
    }
    public static boolean has(ServerPlayer player, Ability ability) {
        return getAbilities(player).contains(ability);
    }
    public static boolean has(ServerPlayer player, AbilityModifier modifier) {
        return getModifiers(player).contains(modifier);
    }
    public static void applySet(UUID uuid, AbilitySetArgumentType.AbilitySet set, MinecraftServer server) {
        playerAbilities.put(uuid, new HashSet<>(set.abilities()));
        playerModifiers.put(uuid, new HashSet<>(set.modifiers()));
        saveFile(server);
    }

    private static File getFile() {
        return new File("config/servertweaks/" + FILE_NAME);
    }
}
