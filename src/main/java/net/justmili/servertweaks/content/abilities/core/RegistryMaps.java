package net.justmili.servertweaks.content.abilities.core;

import net.justmili.servertweaks.content.abilities.Presets;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class RegistryMaps {
    public static final Map<Identifier, Ability> ABILITIES = new HashMap<>();
    public static final Map<Identifier, AbilityModifier> MODIFIERS = new HashMap<>();
    public static final Map<Identifier, AbilityPreset> PRESETS = new HashMap<>();

    public static Map<Identifier, Ability> getAbilities() {
        return RegistryMaps.ABILITIES;
    }
    public static @Nullable Ability byAbilityId(Identifier id) {
        return ABILITIES.get(id);
    }
    public static Set<Identifier> getAbilityIds() {
        return ABILITIES.keySet();
    }

    public static Map<Identifier, AbilityModifier> getModifiers() {
        return RegistryMaps.MODIFIERS;
    }
    public static @Nullable AbilityModifier byModifierId(Identifier id) {
        return MODIFIERS.get(id);
    }
    public static Set<Identifier> getModifierIds() {
        return MODIFIERS.keySet();
    }

    public static Map<Identifier, AbilityPreset> getPresets() {
        return RegistryMaps.PRESETS;
    }
    public static @Nullable AbilityPreset byPresetId(Identifier id) {
        return PRESETS.get(id);
    }
    public static Set<Identifier> getPresetIds() {
        return PRESETS.keySet();
    }

    public static <T> Stream<String> getAsString(Map<Identifier, T> map) {
        return map.keySet().stream().map(Identifier::toString);
    }
}
