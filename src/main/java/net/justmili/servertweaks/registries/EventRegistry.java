package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.core.util.ScalerUtil;
import net.justmili.servertweaks.mechanics.features.RightClickHarvest;
import net.justmili.servertweaks.mechanics.features.WhileAfk;
import net.justmili.servertweaks.mechanics.logic.Banishment;

public class EventRegistry {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Banishment::onEntityHurt);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(WhileAfk::onEntityHurt);
        ServerEntityEvents.ENTITY_LOAD.register(Banishment::onEntityLoad);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                Banishment.onPlayerTick(player);
                WhileAfk.onPlayerTick(player);
            }
        });
        if ((Config.playerAbilities.get())) {
            net.justmili.servertweaks.content.abilities.Events.registerAbilityEvents();
        }
        ServerPlayConnectionEvents.JOIN.register(ScalerUtil::convertScoreToVar);
        UseBlockCallback.EVENT.register(RightClickHarvest::onUseBlock);
    }
}