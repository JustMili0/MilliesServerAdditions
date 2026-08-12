package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.Permission;
import net.minecraft.world.entity.EntityEvent;

import java.util.Set;

public class SmpPerms {
    public static final Set<String> ALLOWED_FOR_LIMITED_OP = Set.of(
        "stop", "ban", "pardon", "kick", "banish", "discard",
        "gamerule", "gamemode", "fly", "tp", "tick",
        "say", "tellraw", "abilities", "scale"
    );

    public static final Permission LIMITED_OPERATOR = new Permission.Atom(ServerTweaks.asId("limited_operator"));

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

                        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, level);
                        CommandUtil.sendOk(source,
                            Component.literal(player.getName().getString()
                                + "'s SMP permission level has been set to "
                                + player.getAttached(PlayerVars.SMP_PERM_LEVEL)),
                            false
                        );

                        // now update the client about their new permissions
                        var highest = server.getProfilePermissions(player.nameAndId());
                        if (level == SmpPermsArgumentType.PermissionLevel.LIMITED_OPERATOR) {
                            player.connection.send(new ClientboundEntityEventPacket(player, EntityEvent.PERMISSION_LEVEL_GAMEMASTERS));
                        } else {
                            if (level.getPermissionSet() instanceof LevelBasedPermissionSet other && other.level().isEqualOrHigherThan(highest.level())) highest = other;
                            player.connection.send(new ClientboundEntityEventPacket(player, switch (highest.level()) {
                                case ALL -> EntityEvent.PERMISSION_LEVEL_ALL;
                                case MODERATORS -> EntityEvent.PERMISSION_LEVEL_MODERATORS;
                                case GAMEMASTERS -> EntityEvent.PERMISSION_LEVEL_GAMEMASTERS;
                                case ADMINS -> EntityEvent.PERMISSION_LEVEL_ADMINS;
                                case OWNERS -> EntityEvent.PERMISSION_LEVEL_OWNERS;
                            }));
                        }
                        server.getCommands().sendCommands(player);

                        return 1;
                    })
                )
            )
        );
    }
}