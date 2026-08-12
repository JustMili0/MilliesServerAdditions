package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SmpPermsArgumentType {
    public static StringArgumentType permissionLevel() {
        return StringArgumentType.word();
    }

    public static PermissionLevel getPermissionLevel(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        var id = StringArgumentType.getString(context, argName);
        return PermissionLevel.byId(id);
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
            Arrays.stream(PermissionLevel.values()).map(PermissionLevel::getSerializedName),
            builder
        );
    }

    public enum PermissionLevel implements StringRepresentable {
        DEFAULT("default", "Default", 0),
        MODERATOR("moderator", "Moderator", 1),
        ADMINISTRATOR("administrator", "Administrator", 2),
        LIMITED_OPERATOR("limited_operator", "Limited Operator", 3),
        OPERATOR("operator", "Operator", 4);

        public static final EnumCodec<PermissionLevel> CODEC = StringRepresentable.fromEnum(PermissionLevel::values);

        private final String id;
        private final String displayName;
        private final int permissionLevel;

        PermissionLevel(String id, String displayName, int permissionLevel) {
            this.id = id;
            this.displayName = displayName;
            this.permissionLevel = permissionLevel;
        }

        @Override
        public @NonNull String getSerializedName() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getPermissionLevel() {
            return permissionLevel;
        }

        public static PermissionLevel byId(String id) {
            return CODEC.byName(id, DEFAULT);
        }
    }

    public static String permissionNameByLevel(int level) {
        for (var value : PermissionLevel.values()) {
            if (value.getPermissionLevel() == level) return value.getDisplayName();
        }
        return PermissionLevel.DEFAULT.getDisplayName();
    }

    public static int defaultPerms() {
        return PermissionLevel.DEFAULT.getPermissionLevel();
    }

    public static int moderatorPerms() {
        return PermissionLevel.MODERATOR.getPermissionLevel();
    }

    public static int adminPerms() {
        return PermissionLevel.ADMINISTRATOR.getPermissionLevel();
    }

    public static int limitedOpPerms() {
        return PermissionLevel.LIMITED_OPERATOR.getPermissionLevel();
    }

    public static int operatorPerms() {
        return PermissionLevel.OPERATOR.getPermissionLevel();
    }
}