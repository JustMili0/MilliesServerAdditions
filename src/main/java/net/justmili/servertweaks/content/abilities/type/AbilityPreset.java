package net.justmili.servertweaks.content.abilities.type;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class AbilityPreset {
    private final Identifier id;
    private final String description;
    private final Set<Ability> abilities;
    private final Set<AbilityModifier> modifiers;

    public AbilityPreset(Identifier id, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
        this.id = id;
        this.description = description;
        this.abilities = abilities;
        this.modifiers = modifiers;
    }
    public AbilityPreset(String id, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
        this.id = ServerTweaks.asResource(id);
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

    public Set<Ability> getAbilities() {
        return abilities;
    }
    public Set<AbilityModifier> getModifiers() {
        return modifiers;
    }
}
