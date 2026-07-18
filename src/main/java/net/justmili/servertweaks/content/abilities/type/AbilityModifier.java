package net.justmili.servertweaks.content.abilities.type;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class AbilityModifier {
    private final Identifier id;

    public AbilityModifier(Identifier id) {
        this.id = id;
    }
    public AbilityModifier(String id) {
        this.id = ServerTweaks.asResource(id);
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AbilityModifier modifier)) return false;
        return Objects.equals(id, modifier.id);
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
