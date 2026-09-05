package net.justmili.servertweaks.content.abilities.core;

import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.justmili.servertweaks.content.abilities.type.Modifier;
import net.justmili.servertweaks.content.abilities.type.Preset;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeRegistries {
    public static final Map<Identifier, Ability> ABILITIES = new HashMap<>();
    public static final Map<Identifier, Debuff> DEBUFFS = new HashMap<>();
    public static final Map<Identifier, Modifier> MODIFIERS = new HashMap<>();
    public static final Map<Identifier, Preset> PRESETS = new HashMap<>();

    public static Map<Identifier, Ability> getAbilities() {
        return ABILITIES;
    }

    public static @Nullable Ability getAbilityById(Identifier id) {
        return ABILITIES.get(id);
    }

    public static Set<Identifier> getAbilityIds() {
        return ABILITIES.keySet();
    }

    public static Map<Identifier, Debuff> getDebuffs() {
        return DEBUFFS;
    }

    public static @Nullable Debuff getDebuffById(Identifier id) {
        return DEBUFFS.get(id);
    }

    public static Set<Identifier> getDebuffIds() {
        return DEBUFFS.keySet();
    }

    public static Map<Identifier, Modifier> getModifiers() {
        return MODIFIERS;
    }

    public static @Nullable Modifier getModifierById(Identifier id) {
        return MODIFIERS.get(id);
    }

    public static Set<Identifier> getModifierIds() {
        return MODIFIERS.keySet();
    }

    public static Map<Identifier, Preset> getPresets() {
        return PRESETS;
    }

    public static @Nullable Preset getPresetById(Identifier id) {
        return PRESETS.get(id);
    }

    public static Set<Identifier> getPresetIds() {
        return PRESETS.keySet();
    }
}