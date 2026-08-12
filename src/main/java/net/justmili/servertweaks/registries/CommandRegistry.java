package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.commands.*;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> {
            if (Config.enableAfkCommand.get()) Afk.register(dispatcher, buildContext, environment);
            if (Config.enableScaleCommand.get()) Scale.register(dispatcher, buildContext, environment);

            if (Config.enableSmpPermsCommand.get()) SmpPerms.register(dispatcher, buildContext, environment);
            if (Config.enableFlyCommand.get()) Fly.register(dispatcher, buildContext, environment);
            if (Config.enableDamageToggleCommand.get()) DamageToggle.register(dispatcher, buildContext, environment);
            Discard.register(dispatcher, buildContext, environment); // This command is not configurable. Too useful to be configurable
            if (Config.enableFillExtrasCommand.get()) FillExtras.register(dispatcher, buildContext, environment);
            if (Config.enableBanishCommand.get()) Banish.register(dispatcher, buildContext, environment);

            if (Config.playerAbilities.get()) PlayerAbilities.register(dispatcher, buildContext, environment);
        });
    }
}