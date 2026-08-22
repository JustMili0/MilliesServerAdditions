package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public abstract class TickingAbilityModifier extends AbilityModifier {
    public TickingAbilityModifier(Identifier id, String displayName, boolean isClientRequired) {
        super(id, displayName, isClientRequired);
    }

    public abstract void tick(ServerPlayer player, ServerLevel level);
}
