package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.AbilityEvents;
import net.justmili.servertweaks.content.mechanics.features.AnvilRepair;
import net.justmili.servertweaks.content.mechanics.features.ArmedArmorStands;
import net.justmili.servertweaks.content.mechanics.features.RightClickHarvest;
import net.justmili.servertweaks.content.mechanics.features.WhileAfk;
import net.justmili.servertweaks.content.mechanics.logic.Banishment;
import net.justmili.servertweaks.core.util.ScalerUtil;

public class EventRegistry {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Banishment::onEntityHurt);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(WhileAfk::onEntityHurt);
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            ArmedArmorStands.onEntityLoad(entity);
            Banishment.onEntityLoad(entity,  level);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                Banishment.onPlayerTick(player);
                WhileAfk.onPlayerTick(player);
            }
        });
        UseBlockCallback.EVENT.register(RightClickHarvest::onUseBlock);
        UseBlockCallback.EVENT.register(AnvilRepair::onUseBlock);

        if ((Config.playerAbilities.get())) AbilityEvents.registerAbilityEvents();
    }
}