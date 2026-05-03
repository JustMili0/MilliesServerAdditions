package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.commands.*;
import net.justmili.servertweaks.core.util.CommandUtil;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, environment) -> {
            if (CommandUtil.checkIfExpected(Config.enableScaleCommand, true))
                Scale.register(dispatcher, commandBuildContext, environment);
            if (CommandUtil.checkIfExpected(Config.enableAfkCommand, true))
                Afk.register(dispatcher, commandBuildContext, environment);
            if (CommandUtil.checkIfExpected(Config.enableDamageToggleCommand, true))
                DamageToggle.register(dispatcher, commandBuildContext, environment);
            if (CommandUtil.checkIfExpected(Config.enableBanishCommand, true))
                Banish.register(dispatcher, commandBuildContext, environment);
            Discard.register(dispatcher, commandBuildContext, environment); // This command is not configurable.
            FillExtras.register(dispatcher, commandBuildContext, environment); // This command is not configurable.
            if (CommandUtil.checkIfExpected(Config.enableFlyCommand, true))
                Fly.register(dispatcher, commandBuildContext, environment);
            if (CommandUtil.checkIfExpected(Config.playerAbilities, true))
                PlayerAbilities.register(dispatcher, commandBuildContext, environment);
        });
    }
}