package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.AbilityEvents;
import net.justmili.servertweaks.content.mechanics.features.*;
import net.justmili.servertweaks.content.mechanics.logic.Banishment;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

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
        UseBlockCallback.EVENT.register((Player player, Level level, InteractionHand hand, BlockHitResult hitResult) -> {
            RightClickHarvest.onUseBlock(player, level, hand, hitResult);
            AnvilRepair.onUseBlock(player, level, hand, hitResult);
            EnchantDuplication.onUseBlock(player, level, hand, hitResult);
            return InteractionResult.PASS;
        });

        if ((Config.playerAbilities.get())) AbilityEvents.registerAbilityEvents();
    }
}