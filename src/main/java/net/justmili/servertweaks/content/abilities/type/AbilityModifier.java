package net.justmili.servertweaks.content.abilities.type;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class AbilityModifier {
    private final Identifier name;

    public AbilityModifier(Identifier name) {
        this.name = name;
    }
    public AbilityModifier(String name) {
        this.name = ServerTweaks.asResource(name);
    }

    public Identifier getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AbilityModifier modifier)) return false;
        return Objects.equals(name, modifier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
