package net.justmili.serveradditions.content.abilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.justmili.serveradditions.ServerAdditions;
import net.justmili.serveradditions.config.Config;
import net.justmili.serveradditions.content.abilities.registries.AbilityRegistry;
import net.justmili.serveradditions.content.abilities.registries.ModifierRegistry;
import net.justmili.serveradditions.content.abilities.type.Ability;
import net.justmili.serveradditions.content.abilities.type.AbilityModifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataStorage {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "player_abilities.json";
    public static final Map<UUID, Set<Ability>> playerAbilities = new LinkedHashMap<>();
    public static final Map<UUID, Set<AbilityModifier>> playerModifiers = new LinkedHashMap<>();

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
                try {
                    uuid = UUID.fromString(entry.getKey());
                } catch (IllegalArgumentException e) {
                    ServerAdditions.LOGGER.warn("Invalid UUID '{}', skipping", entry.getKey());
                    continue;
                }

                JsonObject object = entry.getValue().getAsJsonObject();
                Set<Ability> abilities = new LinkedHashSet<>();
                if (object.has("abilities")) {
                    for (var element : object.getAsJsonArray("abilities")) {
                        Ability ability = AbilityRegistry.byName(element.getAsString());
                        if (ability == null) {
                            ServerAdditions.LOGGER.warn("Unknown ability '{}', skipping", element.getAsString());
                            continue;
                        }
                        abilities.add(ability);
                    }
                }
                Set<AbilityModifier> modifiers = new LinkedHashSet<>();
                if (object.has("ability_modifiers")) {
                    for (var element : object.getAsJsonArray("ability_modifiers")) {
                        AbilityModifier modifier = ModifierRegistry.byName(element.getAsString());
                        if (modifier == null) {
                            ServerAdditions.LOGGER.warn("Unknown modifier '{}', skipping", element.getAsString());
                            continue;
                        }
                        modifiers.add(modifier);
                    }
                }
                playerAbilities.put(uuid, abilities);
                playerModifiers.put(uuid, modifiers);
            }
        } catch (Exception e) {
            ServerAdditions.LOGGER.error("Failed to read config: {}", e.getMessage());
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
            ServerAdditions.LOGGER.info("Saved abilities for {} player(s)", allUuids.size());
        } catch (Exception e) {
            ServerAdditions.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    private static File getFile() {
        return new File("config/serveradditions/"+FILE_NAME);
    }
}
