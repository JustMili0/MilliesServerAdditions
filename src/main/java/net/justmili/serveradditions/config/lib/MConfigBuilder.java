package net.justmili.serveradditions.config.lib;

public class MConfigBuilder {
    private final ConfigLoader config;
    private String comment = null;

    public MConfigBuilder(String modId, String suffix, boolean createSubDirectory) {
        this.config = new ConfigLoader(modId, suffix, createSubDirectory);
    }

    public MConfigBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    // String
    public ConfigEntry<String> define(String key, String defaultValue) {
        ConfigEntry<String> entry = new ConfigEntry<>(key, defaultValue, config);
        config.register(entry, comment);
        comment = null;

        return entry;
    }

    // Boolean
    public ConfigEntry<Boolean> define(String key, boolean defaultValue) {
        ConfigEntry<Boolean> entry = new ConfigEntry<>(key, defaultValue, config);
        config.register(entry, comment);
        comment = null;

        return entry;
    }

    // Integer
    public ConfigEntry<Integer> define(String key, int defaultValue, int min, int max) {
        ConfigEntry<Integer> entry = new ConfigEntry<>(key, defaultValue, min, max, config);
        config.register(entry, comment);
        comment = null;

        return entry;
    }

    // Long
    public ConfigEntry<Long> define(String key, long defaultValue, long min, long max) {
        ConfigEntry<Long> entry = new ConfigEntry<>(key, defaultValue, min, max, config);
        config.register(entry, comment);
        comment = null;

        return entry;
    }

    // Double
    public ConfigEntry<Double> define(String key, double defaultValue, double min, double max) {
        ConfigEntry<Double> entry = new ConfigEntry<>(key, defaultValue, min, max, config);
        config.register(entry, comment);
        comment = null;

        return entry;
    }

    public void build() {
        config.loadOrCreate();
    }

    public ConfigLoader getConfig() {
        return config;
    }
}