package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;

import java.util.Objects;

public class Ability {
    private final Identifier id;

    public Ability(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ability ability)) return false;
        return Objects.equals(id, ability.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
