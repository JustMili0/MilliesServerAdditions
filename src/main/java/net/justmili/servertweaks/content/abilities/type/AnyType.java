package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;

public interface AnyType {
    // For tagging Abilities, Debuffs, Modifiers and their ticking variants
    Identifier getId();
    String getDisplayName();
    boolean isClientRequired();
}
