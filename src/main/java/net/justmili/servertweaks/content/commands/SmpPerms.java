package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.Optional;

public class SmpPerms {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("smpperms").requires(src -> CommandUtil.hasPerms(src, 4))
            .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("permission_level", SmpPermsArgumentType.permissionLevel())
                    .suggests(SmpPermsArgumentType::suggest)
                    .executes(context -> {
                        var source = context.getSource();
                        var server = source.getServer();
                        var player = EntityArgument.getPlayer(context, "player");
                        var level = SmpPermsArgumentType.getPermissionLevel(context, "permission_level");

                        switch (level) {
                            case DEFAULT -> {
                                server.getPlayerList().deop(player.nameAndId());
                                FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, 0);
                            }
                            case MODERATOR -> setPermissions(source, player, server, 1, 2);
                            case ADMINISTRATOR -> setPermissions(source, player, server, 2, 3);
                            case LIMITED_OPERATOR -> setPermissions(source, player, server, 3, 4);
                            case OPERATOR -> setPermissions(source, player, server, 4, 4);
                            default -> throw new IllegalStateException("Unknown SMP permission level");
                        }

                        return 1;
                    })
                )
            )
        );
    }

    private static void setPermissions(CommandSourceStack source, ServerPlayer player, MinecraftServer server, int smpPermLevel, int permLevel) {
        server.getPlayerList().op(
            player.nameAndId(),
            Optional.of(LevelBasedPermissionSet.forLevel(PermissionLevel.byId(permLevel))),
            Optional.of(false));
        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, smpPermLevel);
        CommandUtil.sendOk(source,
            Component.literal(player.getName().getString()
                + "'s SMP permission level has been set to "
                + player.getAttached(PlayerVars.SMP_PERM_LEVEL)),
            false
        );
    }
}