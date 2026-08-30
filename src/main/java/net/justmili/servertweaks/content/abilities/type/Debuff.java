package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;

import java.util.Objects;

public class Debuff {
    private final Identifier id;
    private final String displayName;
    private final boolean clientRequired;

    public Debuff(Identifier id, String displayName, boolean requiresClient) {
        this.id = id;
        this.displayName = displayName;
        this.clientRequired = requiresClient;
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
        if (!(obj instanceof Debuff ability)) return false;
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
