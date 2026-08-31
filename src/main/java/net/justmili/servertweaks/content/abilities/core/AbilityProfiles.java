package net.justmili.servertweaks.content.abilities.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.justmili.libs.v1.utils.server.ServerUtil;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.justmili.servertweaks.content.abilities.type.Modifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class AbilityProfiles {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Map<UUID, Set<Ability>> ABILITIES = new LinkedHashMap<>();
    public static final Map<UUID, Set<Debuff>> DEBUFFS = new LinkedHashMap<>();
    public static final Map<UUID, Set<Modifier>> MODIFIERS = new LinkedHashMap<>();

    public static void saveProfiles() {
        var root = new JsonObject();

        Set<UUID> uuids = new HashSet<>(ABILITIES.keySet());
        uuids.addAll(DEBUFFS.keySet());
        uuids.addAll(MODIFIERS.keySet());

        for (var uuid : uuids) {
            var uuidObj = new JsonObject();

            var name = ServerUtil.getPlayerName(uuid, true);
            uuidObj.addProperty("name", name);

            saveElements(uuidObj, "abilities", ABILITIES.getOrDefault(uuid, Collections.emptySet()), Ability::getId);
            saveElements(uuidObj, "debuffs", DEBUFFS.getOrDefault(uuid, Collections.emptySet()), Debuff::getId);
            saveElements(uuidObj, "modifiers", MODIFIERS.getOrDefault(uuid, Collections.emptySet()), Modifier::getId);

            root.add(uuid.toString(), uuidObj);
        }

        try {
            var file = getServerFile();
            file.getParentFile().mkdirs();
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }

            int size = uuids.size();
            if (size > 0) {
                ServerTweaks.LOGGER.info("Saved ability profiles for {} player(s)", size);
            } else {
                ServerTweaks.LOGGER.info("Saved ability profiles");
            }

        } catch (Exception e) {
            ServerTweaks.LOGGER.error("Failed to save ability profiles: {}", e.getMessage());
        }
    }

    static <T> void saveElements(JsonObject playerObj, String memberName, Set<T> elements, Function<T, Identifier> lookup) {
        var array = new JsonArray();
        elements.stream().map(lookup).map(Identifier::toString).sorted().forEach(array::add);
        playerObj.add(memberName, array);
    }

    public static void loadProfiles() {
        ABILITIES.clear();
        DEBUFFS.clear();
        MODIFIERS.clear();
        if (!(Config.playerAbilities.get())) return;

        var file = getServerFile();
        if (!file.exists()) {
            saveProfiles();
            return;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            var root = GSON.fromJson(reader, JsonObject.class);
            for (var entry : root.entrySet()) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(entry.getKey());
                } catch (IllegalArgumentException e) {
                    ServerTweaks.LOGGER.warn("Invalid player UUID '{}', skipping", entry.getKey());
                    continue;
                }

                var uuidObj = entry.getValue().getAsJsonObject();
                Set<Ability> abilities = new LinkedHashSet<>();
                Set<Debuff> debuffs = new LinkedHashSet<>();
                Set<Modifier> modifiers = new LinkedHashSet<>();

                loadElements(uuidObj, abilities, "abilities", "ability", AbilityRegistries::getAbilityById);
                loadElements(uuidObj, debuffs, "debuffs", "debuff", AbilityRegistries::getDebuffById);
                loadElements(uuidObj, modifiers, "modifiers", "modifier", AbilityRegistries::getModifierById);

                ABILITIES.put(uuid, abilities);
                DEBUFFS.put(uuid, debuffs);
                MODIFIERS.put(uuid, modifiers);
            }
        } catch (Exception e) {
            ServerTweaks.LOGGER.error("Failed to load ability profiles: {}", e.getMessage());
        }
    }

    private static <T> void loadElements(JsonObject uuidObj, Set<T> member, String memberName, String elementName, Function<Identifier, T> lookup) {
        if (!uuidObj.has(memberName)) return;

        for (var element : uuidObj.getAsJsonArray(memberName)) {
            var raw = element.getAsString();
            var id = Identifier.tryParse(raw);
            if (id == null) {
                ServerTweaks.LOGGER.warn("Invalid {} id '{}', skipping", elementName, raw);
                continue;
            }

            var type = lookup.apply(id);
            if (type == null) {
                ServerTweaks.LOGGER.warn("Unknown {} '{}', skipping", elementName, raw);
                continue;
            }

            member.add(type);
        }
    }

    public static void reloadProfiles() {
        loadProfiles();
        saveProfiles();
    }

    public static Path getConfigDir() {
        return Path.of("config", ServerTweaks.MODID);
    }

    public static File getServerFile() {
        return Path.of(getConfigDir().toString(), "player_abilities.json").toFile();
    }

    @Environment(EnvType.CLIENT)
    public static File getClientFile(Minecraft client) {
        var server = client.getCurrentServer();
        if (server == null) return getServerFile();
        var ip = server.ip.replaceAll("[^a-zA-Z0-9._-]", "_");
        return Path.of(getConfigDir().toString(), "player_abilities-" + ip + ".json").toFile();
    }
}