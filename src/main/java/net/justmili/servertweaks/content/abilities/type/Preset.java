package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;

import java.util.Set;

public class Preset {
    private final Identifier id;
    private final String displayName;
    private final String description;
    private final Set<Ability> abilities;
    private final Set<Debuff> debuffs;
    private final Set<Modifier> modifiers;

    public Preset(Identifier id, String displayName, String description, Set<Ability> abilities, Set<Debuff> debuffs, Set<Modifier> modifiers) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.abilities = abilities;
        this.debuffs = debuffs;
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

    public Set<Debuff> getDebuffs() {
        return debuffs;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }
}
