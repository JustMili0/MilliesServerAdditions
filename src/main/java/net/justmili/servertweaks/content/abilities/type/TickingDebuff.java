package net.justmili.servertweaks.content.abilities.type;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public abstract class TickingDebuff extends Debuff{
    public TickingDebuff(Identifier id, String displayName, boolean requiresClient) {
        super(id, displayName, requiresClient);
    }

    public abstract void tick(ServerPlayer player, ServerLevel level);
}
