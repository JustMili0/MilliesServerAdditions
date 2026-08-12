package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SmpPermsArgumentType {
    public static StringArgumentType permissionLevel() {
        return StringArgumentType.word();
    }

    public static PermissionLevel getPermissionLevel(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        var id = StringArgumentType.getString(context, argName);
        var level = PermissionLevel.byId(id);
        if (level == null) throw new SimpleCommandExceptionType(Component.literal("Unknown permission level: " + id)).create();
        return level;
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
            Arrays.stream(PermissionLevel.values()).map(PermissionLevel::getId),
            builder
        );
    }

    public enum PermissionLevel {
        DEFAULT("default"),
        MODERATOR("moderator"),
        ADMINISTRATOR("administrator"),
        LIMITED_OPERATOR("limited_operator"),
        OPERATOR("operator");

        private final String id;

        PermissionLevel(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public static PermissionLevel byId(String id) {
            for (var level : values()) {
                if (level.id.equals(id)) return level;
            }
            return DEFAULT;
        }
    }
}