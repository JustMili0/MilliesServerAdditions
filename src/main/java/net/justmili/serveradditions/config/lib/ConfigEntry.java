package net.justmili.serveradditions.config.lib;

@SuppressWarnings("unchecked")
public class ConfigEntry<T> {
    private final String key;
    private final T defaultValue;
    private T value;
    private final ConfigLoader config;
    private final T min, max;

    ConfigEntry(String key, T defaultValue, ConfigLoader config) {
        this(key, defaultValue, null, null, config);
    }

    ConfigEntry(String key, T defaultValue, T min, T max, ConfigLoader config) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.config = config;
        this.min = min;
        this.max = max;
    }

    public String key() {
        return key;
    }

    public boolean is(T value) {
        return this.value.equals(value);
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        if (!validate(newValue)) return;
        this.value = newValue;
        config.save();
    }

    public T defaultValue() {
        return defaultValue;
    }

    public T min() {
        return min;
    }

    public T max() {
        return max;
    }

    public boolean hasRange() {
        return min != null && max != null;
    }

    private boolean validate(T val) {
        if (!hasRange()) return true;
        return ((Comparable<T>) val).compareTo(min) >= 0
            && ((Comparable<T>) val).compareTo(max) <= 0;
    }

    void load(String raw) {
        if (raw == null || raw.isBlank()) return;
        try {
            T parsed = parse(raw.trim());
            value = validate(parsed) ? parsed : defaultValue;
        } catch (Exception ignored) {
            value = defaultValue;
        }
    }

    String serialize() {
        return String.valueOf(value);
    }

    private T parse(String raw) {
        if (defaultValue instanceof Boolean) return (T) Boolean.valueOf(raw);
        if (defaultValue instanceof Integer) return (T) Integer.valueOf(raw);
        if (defaultValue instanceof Long) return (T) Long.valueOf(raw);
        if (defaultValue instanceof Double) return (T) Double.valueOf(raw);
        if (defaultValue instanceof Float) return (T) Float.valueOf(raw);
        return (T) raw;
    }
}