package net.justmili.servertweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.utils.common.ResourceUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.Debuffs;
import net.justmili.servertweaks.content.abilities.Modifiers;
import net.justmili.servertweaks.content.abilities.Presets;
import net.justmili.servertweaks.content.abilities.core.AbilityProfiles;
import net.justmili.servertweaks.registries.CommandRegistry;
import net.justmili.servertweaks.registries.DimRegistry;
import net.justmili.servertweaks.registries.EventRegistry;
import net.justmili.servertweaks.registries.PacketRegistry;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerTweaks.class);
    public static final String MODID = "servertweaks";

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Millie's Core Libraries...");
        CoreLibs.init();

        LOGGER.info("Initializing Millie's Server Additions...");

        PacketRegistry.init();
        Config.init();
        PlayerVars.init();

        DimRegistry.init();
        CommandRegistry.init();
        EventRegistry.init();

        if (Config.playerAbilities.get()) {
            Abilities.init();
            Debuffs.init();
            Modifiers.init();
            Presets.init();

            ServerLifecycleEvents.SERVER_STARTED.register(AbilityProfiles::loadProfiles);
        }
    }

    public static Identifier asId(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}