package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.util.SmpPermsUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

public class SmpPerms {
    public static final Set<String> ALLOWED_FOR_LIMITED_OP = Set.of(
        "stop", "ban", "ban-ip", "pardon", "pardon-ip", "kick", "banish", "discard", "gamemode", "fly", "tp", "teleport", "trigger", "say", "tellraw",
        "abilities", "scale", "flan", "waypoint", "function", "whitelist", "banlist", "whynoaxiom", "reload", "datapack", "graves", "servercore", "servux",
        "spectate", "sit", "vanish"
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("smpperms").requires(src -> CommandUtil.hasPerms(src, 4))
            .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("permission_level", SmpPermsArgumentType.permissionLevel())
                    .suggests(SmpPermsArgumentType::suggest)
                    .executes(context -> {
                        var source = context.getSource();
                        var player = EntityArgument.getPlayer(context, "player");
                        var permissionLevel = SmpPermsArgumentType.getPermissionLevel(context, "permission_level");

                        switch (permissionLevel) {
                            case DEFAULT -> SmpPermsUtil.deop(player);
                            case MODERATOR -> setPermissions(source, player, SmpPermsUtil.moderatorPerms(), 2);
                            case ADMINISTRATOR -> setPermissions(source, player, SmpPermsUtil.adminPerms(), 3);
                            case LIMITED_OPERATOR -> setPermissions(source, player, SmpPermsUtil.limitedOpPerms(), 4);
                            case OPERATOR -> setPermissions(source, player, SmpPermsUtil.operatorPerms(), 4);
                            default -> throw new IllegalStateException("Unknown SMP permission level");
                        }

                        return 1;
                    })
                )
            )
        );
    }

    public static void setPermissions(CommandSourceStack source, ServerPlayer player, int smpPermLevel, int permLevel) {
        SmpPermsUtil.op(player, smpPermLevel, permLevel);

        var ownOrOthers = source.getPlayer() == player ? "own" : player.getName().getString() + "'s";
        var message = "Set " + ownOrOthers + " SMP permission level to " + SmpPermsUtil.permissionNameByLevel(smpPermLevel);
        CommandUtil.sendOk(source, Component.literal(message), true);
    }
}