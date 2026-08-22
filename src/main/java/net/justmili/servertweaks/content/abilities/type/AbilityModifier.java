package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;

import java.util.Objects;

public class AbilityModifier {
    private final Identifier id;
    private final String displayName;
    private final boolean clientRequired;

    public AbilityModifier(Identifier id, String displayName, boolean isClientRequired) {
        this.id = id;
        this.displayName = displayName;
        this.clientRequired = isClientRequired;
    }

    public Identifier getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isClientRequired() {
        return clientRequired;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AbilityModifier modifier)) return false;
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
