package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.mlibs.v1.utils.common.CommandUtil;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.util.SmpPermsUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class SmpPerms {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("smpperms").requires(src -> CommandUtil.hasPerms(src, 4))
            .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("permission_level", SmpPermsArgumentType.permissionLevel())
                    .suggests(SmpPermsArgumentType::suggest)
                    .executes(context -> op(
                        context.getSource(),
                        EntityArgument.getPlayer(context, "player"),
                        SmpPermsArgumentType.getPermissionLevel(context, "permission_level"))))
            )
        );
    }

    static int op(CommandSourceStack source, ServerPlayer player, SmpPermsArgumentType.PermissionLevel permissionLevel) {
        var ownOrOthers = source.getPlayer() == player ? "own" : player.getName().getString() + "'s";
        var message = "Set " + ownOrOthers + " SMP permission level to Default";

        switch (permissionLevel) {
            case DEFAULT -> {
                SmpPermsUtil.deop(player);
                CommandUtil.sendOk(source, message);
            }
            case MODERATOR -> op(source, player, message, SmpPermsUtil.moderatorPerms(), 2);
            case ADMINISTRATOR -> op(source, player, message, SmpPermsUtil.adminPerms(), 3);
            case LIMITED_OPERATOR -> op(source, player, message, SmpPermsUtil.limitedOpPerms(), 4);
            case OPERATOR -> op(source, player, message, SmpPermsUtil.operatorPerms(), 4);
            default -> throw new IllegalStateException("Unknown SMP permission level");
        }

        return 1;
    }

    public static void op(CommandSourceStack source, ServerPlayer player, String message, int smp, int vanilla) {
        SmpPermsUtil.op(player, smp, vanilla);
        CommandUtil.sendOk(source, message);
    }
}