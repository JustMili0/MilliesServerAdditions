package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public abstract class TickingAbility extends Ability {
    public TickingAbility(Identifier id) {
        super(id);
    }

    public abstract void tick(ServerPlayer player, ServerLevel level);
}