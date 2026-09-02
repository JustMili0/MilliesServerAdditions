package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface TickingType {
    // For tagging ticking variants of Abilities, Debuffs, Modifiers
    void tick(ServerPlayer player, ServerLevel level);
}
