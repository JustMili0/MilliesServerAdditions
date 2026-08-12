package net.justmili.servertweaks.registries;

import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.commands.*;
import net.justmili.servertweaks.mixin.accessors.CommandNodeAccessor;
import net.minecraft.commands.CommandSourceStack;

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

        var modifyPermissionsPhase = ServerTweaks.asId("modify_permissions");
        CommandRegistrationCallback.EVENT.addPhaseOrdering(modifyPermissionsPhase, Event.DEFAULT_PHASE);
        CommandRegistrationCallback.EVENT.register(modifyPermissionsPhase, (dispatcher, buildContext, selection) -> {
            for (CommandNode<CommandSourceStack> child : dispatcher.getRoot().getChildren()) {
                if (SmpPerms.ALLOWED_FOR_LIMITED_OP.contains(child.getName())) patchRequirementRecursive(child);
            }
        });
    }

    private static void patchRequirementRecursive(CommandNode<CommandSourceStack> node) {
        var original = node.getRequirement();
        //noinspection unchecked
        ((CommandNodeAccessor<CommandSourceStack>) node).setRequirement(source ->
            original.test(source) || source.permissions().hasPermission(SmpPerms.LIMITED_OPERATOR));

        for (CommandNode<CommandSourceStack> child : node.getChildren()) {
            patchRequirementRecursive(child);
        }
    }
}