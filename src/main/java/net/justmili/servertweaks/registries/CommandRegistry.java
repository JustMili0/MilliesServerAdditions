package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.commands.*;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, environment) -> {
            if (Config.enableScaleCommand.get())
                Scale.register(dispatcher, commandBuildContext, environment);
            if (Config.enableAfkCommand.get())
                Afk.register(dispatcher, commandBuildContext, environment);
            if (Config.enableDamageToggleCommand.get())
                DamageToggle.register(dispatcher, commandBuildContext, environment);
            if (Config.enableBanishCommand.get())
                Banish.register(dispatcher, commandBuildContext, environment);
            Discard.register(dispatcher, commandBuildContext, environment); // This command is not configurable.
            FillExtras.register(dispatcher, commandBuildContext, environment); // This command is not configurable.
            if (Config.enableFlyCommand.get())
                Fly.register(dispatcher, commandBuildContext, environment);
            if (Config.playerAbilities.get())
                PlayerAbilities.register(dispatcher, commandBuildContext, environment);
        });
    }
}