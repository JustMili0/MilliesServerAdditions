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
import java.util.Set;

public class SmpPerms {
    public static final Set<String> ALLOWED_FOR_LIMITED_OP = Set.of(
        "stop", "ban", "pardon", "kick", "banish", "discard",
        "gamerule", "gamemode", "fly", "tp", "tick",
        "say", "tellraw", "abilities", "scale"
    );

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
                                FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, SmpPermsArgumentType.defaultPerms());
                                server.getCommands().sendCommands(player);
                            }
                            case MODERATOR -> setPermissions(source, player, server, SmpPermsArgumentType.moderatorPerms(), 2);
                            case ADMINISTRATOR -> setPermissions(source, player, server, SmpPermsArgumentType.adminPerms(), 3);
                            case LIMITED_OPERATOR -> setPermissions(source, player, server, SmpPermsArgumentType.limitedOpPerms(), 4);
                            case OPERATOR -> setPermissions(source, player, server, SmpPermsArgumentType.operatorPerms(), 4);
                            default -> throw new IllegalStateException("Unknown SMP permission level");
                        }

                        return 1;
                    })
                )
            )
        );
    }

    private static void setPermissions(CommandSourceStack source, ServerPlayer player, MinecraftServer server, int smpPermLevel, int permLevel) {
        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, smpPermLevel);
        server.getPlayerList().op(player.nameAndId(), Optional.of(LevelBasedPermissionSet.forLevel(PermissionLevel.byId(permLevel))), Optional.of(false));
        server.getCommands().sendCommands(player);

        var ownOrOthers = source.getPlayer() == player ? "own" : player.getName().getString() + "'s";
        var message = "Set " + ownOrOthers + " SMP permission level to " + SmpPermsArgumentType.permissionNameByLevel(smpPermLevel);
        CommandUtil.sendOk(source, Component.literal(message), true);
    }

    public static boolean isLimitedOperator(ServerPlayer player) {
        return FdaUtil.getInt(player, PlayerVars.SMP_PERM_LEVEL) == SmpPermsArgumentType.limitedOpPerms();
    }
}