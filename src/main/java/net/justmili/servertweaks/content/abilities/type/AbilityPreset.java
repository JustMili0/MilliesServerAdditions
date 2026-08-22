package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;

import java.util.Set;

public class AbilityPreset {
    private final Identifier id;
    private final String displayName;
    private final String description;
    private final Set<Ability> abilities;
    private final Set<AbilityModifier> modifiers;

    public AbilityPreset(Identifier id, String displayName, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.abilities = abilities;
        this.modifiers = modifiers;
    }

    public Identifier getId() {
        return id;
    }

    public String getDesc() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public Set<AbilityModifier> getModifiers() {
        return modifiers;
    }
}
