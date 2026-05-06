package net.justmili.serveradditions.registries;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.justmili.serveradditions.config.Config;
import net.justmili.serveradditions.content.commands.*;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, environment) -> {
            if (Config.enableScaleCommand.get() == true)
                Scale.register(dispatcher, commandBuildContext, environment);
            if (Config.enableAfkCommand.get() == true)
                Afk.register(dispatcher, commandBuildContext, environment);
            if (Config.enableDamageToggleCommand.get() == true)
                DamageToggle.register(dispatcher, commandBuildContext, environment);
            if (Config.enableBanishCommand.get() == true)
                Banish.register(dispatcher, commandBuildContext, environment);
            Discard.register(dispatcher, commandBuildContext, environment); // This command is not configurable.
            FillExtras.register(dispatcher, commandBuildContext, environment); // This command is not configurable.
            if (Config.enableFlyCommand.get() == true)
                Fly.register(dispatcher, commandBuildContext, environment);
            if (Config.playerAbilities.get() == true)
                PlayerAbilities.register(dispatcher, commandBuildContext, environment);
        });
    }
}