package net.justmili.serveradditions.registries;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.serveradditions.config.Config;
import net.justmili.serveradditions.content.abilities.UseEvents;
import net.justmili.serveradditions.core.util.ScalerUtil;
import net.justmili.serveradditions.mechanics.features.AnvilRepair;
import net.justmili.serveradditions.mechanics.features.RightClickHarvest;
import net.justmili.serveradditions.mechanics.features.WhileAfk;
import net.justmili.serveradditions.mechanics.logic.Banishment;

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
        ServerPlayConnectionEvents.JOIN.register(ScalerUtil::convertScoreToVar);
        UseBlockCallback.EVENT.register(RightClickHarvest::onUseBlock);
        UseBlockCallback.EVENT.register(AnvilRepair::onUseBlock);

        if ((Config.playerAbilities.get())) UseEvents.registerAbilityEvents();
    }
}