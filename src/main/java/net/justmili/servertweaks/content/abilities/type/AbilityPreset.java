package net.justmili.servertweaks.content.abilities.type;

import java.util.Set;

public record AbilityPreset(String name, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }
    public Set<AbilityModifier> getModifiers() {
        return modifiers;
    }
}
