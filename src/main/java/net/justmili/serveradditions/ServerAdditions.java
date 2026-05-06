package net.justmili.serveradditions;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.justmili.serveradditions.content.abilities.DataStorage;
import net.justmili.serveradditions.core.variables.PlayerAttachments;
import net.justmili.serveradditions.registries.CommandRegistry;
import net.justmili.serveradditions.registries.EventRegistry;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerAdditions implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(ServerAdditions.class);
    public static final String MODID = "serveradditions";

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Millie's Server Additions...");
        PlayerAttachments.register();
        CommandRegistry.register();
        EventRegistry.register();

        ServerLifecycleEvents.SERVER_STARTED.register(DataStorage::loadFile);
    }

    public static Identifier asResource(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
    public static Identifier asPath(String path) {
        return Identifier.parse(path);
    }
}
