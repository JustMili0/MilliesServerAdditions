package net.justmili.servertweaks.config.lib;

import net.fabricmc.loader.api.FabricLoader;
import net.justmili.servertweaks.ServerTweaks;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {
    private record EntryInstance(ConfigEntry<?> entry, String comment) { }

    private final Path path;
    private final List<EntryInstance> entries = new ArrayList<>();
    private final Properties properties = new Properties();

    ConfigLoader(String modId, String name, boolean createSubDirectory) {
        Path configDirectory = FabricLoader.getInstance().getConfigDir();

        String fileName = (name == null || name.isBlank())
            ? modId : (createSubDirectory ? name : modId+"-"+name);

        path = createSubDirectory ? configDirectory.resolve(modId).resolve(fileName+".properties")
            : configDirectory.resolve(fileName+".properties");
    }

    void register(ConfigEntry<?> entry, String comment) {
        entries.add(new EntryInstance(entry, comment));
    }

    public void loadOrCreate() {
        File file = path.toFile();
        if (!file.exists()) {
            ServerTweaks.LOGGER.error("No config found. Creating new config");
            save();
            return;
        }

        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            ServerTweaks.LOGGER.error("Failed to load config: {}", e.getMessage());
            return;
        }

        for (EntryInstance instance : entries) {
            instance.entry.load(properties.getProperty(instance.entry.key()));
        }

        ServerTweaks.LOGGER.info("Config loaded from {}", path.getFileName());
    }

    public void save() {
        File file = path.toFile();
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# "+path.getFileName()+"\n\n");

            for (EntryInstance instance : entries) {
                if (instance.comment != null) writer.write("# "+instance.comment+"\n");

                writer.write(hint(instance.entry)+"\n");
                writer.write(instance.entry.key()+"="+instance.entry.serialize()+"\n\n");
            }
        } catch (IOException e) {
            ServerTweaks.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    private String hint(ConfigEntry<?> entry) {
        Object key = entry.defaultValue();

        if (entry.hasRange()) return "# Allowed range: "+entry.min()+"-"+entry.max()+" - Default: "+key; // int, double, long, float
        if (key instanceof Boolean) return "# Allowed values: true, false - Default: "+key; // Boolean
        return "# Default: "+key; // String
    }
}