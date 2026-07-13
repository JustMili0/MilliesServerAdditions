package net.justmili.servertweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.justmili.libs.v1.utils.ResourceUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.manage.FileManager;
import net.justmili.servertweaks.core.variables.PlayerAttachments;
import net.justmili.servertweaks.registries.CommandRegistry;
import net.justmili.servertweaks.registries.DimRegistry;
import net.justmili.servertweaks.registries.EventRegistry;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerTweaks implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(ServerTweaks.class);
    public static final String MODID = "servertweaks";

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Millie's Server Additions...");
        Config.register();

        PlayerAttachments.register();
        DimRegistry.register();
        CommandRegistry.register();
        EventRegistry.register();

        ServerLifecycleEvents.SERVER_STARTED.register(FileManager::loadFile);
    }

    public static Identifier asResource(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}
