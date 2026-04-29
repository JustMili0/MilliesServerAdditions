package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.justmili.servertweaks.content.abilities.AbilityManager;
import net.justmili.servertweaks.content.commands.arguments.AbilitySetArgumentType;
import net.justmili.servertweaks.core.util.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerAbilities {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(
            Commands.literal("abilities")
                .then(Commands.literal("reload")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .executes(context -> {
                        MinecraftServer server = context.getSource().getServer();
                        AbilityManager.reloadFile(server);

                        CommandUtil.sendSucc(context.getSource(), "Reloaded Player Abilities");
                        return 1;
                    })
                )
                .then(Commands.literal("pickPremadeSet")
                    .then(Commands.argument("set", AbilitySetArgumentType.setSelect())
                        .suggests(AbilitySetArgumentType::suggest)
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!CommandUtil.checkIfPlayerExecuted(context)) return 0;
                            ServerPlayer player = source.getPlayer();

                            String setName = StringArgumentType.getString(context, "set");
                            AbilitySetArgumentType.AbilitySet set = AbilitySetArgumentType.getSet(setName);
                            if (set == null) {
                                CommandUtil.sendFail(source, "Unknown ability set: " + setName);
                                return 0;
                            }

                            MutableComponent description = Component.literal(set.description() + "\n\n");
                            MutableComponent apply = Component.literal("     [APPLY] ")
                                .setStyle(Style.EMPTY.withColor(0x55FF55).withClickEvent(new ClickEvent.RunCommand("/abilities applyChosenSet " + setName)));
                            MutableComponent cancel = Component.literal(" [CANCEL]")
                                .setStyle(Style.EMPTY.withColor(0xFF5555).withClickEvent(new ClickEvent.RunCommand("/abilities cancelChoosingSet")));

                            player.sendSystemMessage(description.append(apply).append(cancel));
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("applyChosenSet")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("set", AbilitySetArgumentType.setSelect())
                        .suggests(AbilitySetArgumentType::suggest)
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!CommandUtil.checkIfPlayerExecuted(context)) return 0;
                            ServerPlayer player = source.getPlayer();

                            String setName = StringArgumentType.getString(context, "set");
                            AbilitySetArgumentType.AbilitySet set = AbilitySetArgumentType.getSet(setName);
                            if (set == null) {
                                CommandUtil.sendFail(source, "Unknown ability set: " + setName);
                                return 0;
                            }

                            AbilityManager.applySet(player.getUUID(), set, source.getServer());
                            CommandUtil.sendSucc(source, "Applied the " + setName + " set!");

                            return 1;
                        })
                    )
                )
                .then(Commands.literal("cancelChoosingSet")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        if (!CommandUtil.checkIfPlayerExecuted(context)) return 0;

                        // Do literally fucking nothing

                        return 1;
                    })
                )
        );
    }
}

/* TODO
  - (Admin) `wipeAbilitiesProfile` - Erases a player from `player_abilities.json` file and resets `picked_ability_preset` player variable back to false
  - (Admin) `grant <player> <abilityOrDebuff | modifier>` - Allows permission level 2 and above staff to grant players abilities, ability debuffs or ability modifiers
  - (Admin) `revoke <player> <abilityOrDebuff | modifier>` - Allows permission level 2 and above staff to revoke players' abilities, ability debuffs or ability modifiers
 */