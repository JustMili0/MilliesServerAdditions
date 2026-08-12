package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.commands.SmpPerms;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionSet;
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
        return SharedSuggestionProvider.suggest(Arrays.stream(PermissionLevel.values()).map(PermissionLevel::getSerializedName), builder);
    }

    public enum PermissionLevel implements StringRepresentable {
        DEFAULT("default", LevelBasedPermissionSet.ALL),
        MODERATOR("moderator", LevelBasedPermissionSet.GAMEMASTER),
        ADMINISTRATOR("administrator", LevelBasedPermissionSet.ADMIN),
        LIMITED_OPERATOR("limited_operator", permission -> permission == SmpPerms.LIMITED_OPERATOR),
        OPERATOR("operator", LevelBasedPermissionSet.OWNER);

        public static final EnumCodec<PermissionLevel> CODEC = StringRepresentable.fromEnum(PermissionLevel::values);

        private final String id;
        private final PermissionSet permissionSet;

        PermissionLevel(String id, PermissionSet permission) {
            this.id = id;
            this.permissionSet = permission;
        }

        @Override
        public @NonNull String getSerializedName() {
            return id;
        }

        public PermissionSet getPermissionSet() {
            return permissionSet;
        }

        public static PermissionLevel byId(String id) {
            return CODEC.byName(id, DEFAULT);
        }
    }
}