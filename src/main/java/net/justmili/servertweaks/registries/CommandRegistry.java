package net.justmili.servertweaks.registries;

import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.commands.*;
import net.justmili.servertweaks.mixin.accessors.CommandNodeAccessor;
import net.justmili.servertweaks.util.SmpPermsUtil;
import net.minecraft.commands.CommandSourceStack;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> {
            if (Config.enableAfkCommand.get()) Afk.register(dispatcher);
            if (Config.enableScaleCommand.get()) Scale.register(dispatcher);

            if (Config.enableSmpPermsCommand.get()) SmpPerms.register(dispatcher, buildContext, environment);
            if (Config.enableFlyCommand.get()) Fly.register(dispatcher);
            if (Config.enableDamageToggleCommand.get()) DamageToggle.register(dispatcher, buildContext);
            Discard.register(dispatcher); // This command is not configurable. Too useful to be configurable
            if (Config.enableFillExtrasCommand.get()) FillExtras.register(dispatcher, buildContext, environment);
            if (Config.enableBanishCommand.get()) Banish.register(dispatcher);

            if (Config.playerAbilities.get()) PlayerAbilities.register(dispatcher, buildContext, environment);
        });

        // SMP Permission Levels
        var modifyPermissionsPhase = ServerTweaks.asId("modify_permissions");
        CommandRegistrationCallback.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, modifyPermissionsPhase);
        CommandRegistrationCallback.EVENT.register(modifyPermissionsPhase, (dispatcher, buildContext, selection) -> {
            var root = dispatcher.getRoot();
            Set<CommandNode<CommandSourceStack>> exempt = Collections.newSetFromMap(new IdentityHashMap<>());

            for (CommandNode<CommandSourceStack> child : root.getChildren()) {
                if (SmpPermsUtil.ALLOWED_FOR_LIMITED_OP.contains(child.getName())) collectRedirectTargets(child, exempt);
            }
            for (CommandNode<CommandSourceStack> child : root.getChildren()) {
                if (!SmpPermsUtil.ALLOWED_FOR_LIMITED_OP.contains(child.getName())) patchRestrictRecursive(child, exempt);
            }
        });
    }

    private static void collectRedirectTargets(CommandNode<CommandSourceStack> node, Set<CommandNode<CommandSourceStack>> exempt) {
        var redirect = node.getRedirect();
        if (redirect != null && exempt.add(redirect)) collectRedirectTargets(redirect, exempt);

        for (CommandNode<CommandSourceStack> child : node.getChildren()) collectRedirectTargets(child, exempt);
    }

    private static void patchRestrictRecursive(CommandNode<CommandSourceStack> node, Set<CommandNode<CommandSourceStack>> exempt) {
        if (exempt.contains(node)) return;

        var original = node.getRequirement();
        //noinspection unchecked
        ((CommandNodeAccessor<CommandSourceStack>) node).setRequirement(source -> {
            if (!original.test(source)) return false;
            var player = source.getPlayer();
            return player == null || !SmpPermsUtil.isLimitedOperator(player);
        });

        for (CommandNode<CommandSourceStack> child : node.getChildren()) patchRestrictRecursive(child, exempt);
    }
}