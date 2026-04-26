package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.servertweaks.content.abilities.AbilityEffects;
import net.justmili.servertweaks.mechanics.logic.*;

public class Events {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Banishment::onEntityHurt);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(WhileAfk::onEntityHurt);
        //ServerLivingEntityEvents.ALLOW_DAMAGE.register(WhileDuel::onEntityHurt);
        //EntityEvent.LIVING_DEATH.register(WhileDuel::onPlayerDeath);
        ServerEntityEvents.ENTITY_LOAD.register(Banishment::onEntityLoad);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                Banishment.onPlayerTick(player);
                WhileAfk.onPlayerTick(player);
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
            ScaleConvert.onServerJoined(handler.player));
        //PlayerEvent.PLAYER_QUIT.register(WhileDuel::onPlayerDisconnect);
        UseBlockCallback.EVENT.register(RightClickHarvest::onUseBlock);
        AbilityEffects.registerAbilityEvents();
    }
}