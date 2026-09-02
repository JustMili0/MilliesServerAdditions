package net.justmili.mlibs.v1.utils.server;

import net.justmili.mlibs.v1.utils.common.MathUtil;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.WorldData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServerUtil {

    // Players
    public static PlayerList getPlayerList(MinecraftServer server) {
        return server.getPlayerList();
    }

    public static int getMaxPlayers(MinecraftServer server) {
        return getPlayerList(server).getMaxPlayers();
    }

    public static int getPlayerCount(MinecraftServer server) {
        return getPlayerList(server).getPlayerCount();
    }

    public static List<ServerPlayer> getPlayers(MinecraftServer server) {
        return getPlayerList(server).getPlayers();
    }

    public static ServerPlayer getPlayer(MinecraftServer server, UUID uuid) {
        return getPlayerList(server).getPlayer(uuid);
    }

    public static ServerPlayer getPlayer(MinecraftServer server, String username) {
        return getPlayerList(server).getPlayerByName(username);
    }

    public static String getPlayerName(MinecraftServer server, UUID uuid, boolean tryGetOfflinePlayer) {
        var player = getPlayerList(server).getPlayer(uuid);
        if (player != null) return player.getName().getString();
        // 1.21.9+
        //if (tryGetOfflinePlayer) return server.services().profileResolver().fetchById(uuid).map(GameProfile::name).orElse("");
        return "";
    }

    public static String getPlayerName(MinecraftServer server, String username, boolean tryGetOfflinePlayer) {
        var player = getPlayerList(server).getPlayerByName(username);
        if (player != null) return player.getName().getString();
        // 1.21.9+
        //if (tryGetOfflinePlayer) return server.services().profileResolver().fetchByName(username).map(GameProfile::name).orElse("");
        return "";
    }

    public static void opPlayer(ServerPlayer player, LevelBasedPermissionSet permission, boolean canBypassPlayerLimit) {
        getPlayerList(player.level().getServer()).op(player.nameAndId(), Optional.of(permission), Optional.of(canBypassPlayerLimit));
    }

    public static void deopPlayer(ServerPlayer player) {
        getPlayerList(player.level().getServer()).deop(player.nameAndId());
    }

    public static boolean isOp(ServerPlayer player) {
        return getPlayerList(player.level().getServer()).isOp(player.nameAndId());
    }

    public static void broadcast(MinecraftServer server, Component message, boolean showAboveHotbar) {
        getPlayerList(server).broadcastSystemMessage(message, showAboveHotbar);
    }

    public static void kickUnwhitelisted(MinecraftServer server) {
        server.kickUnlistedPlayers();
    }

    // Levels
    public static ServerLevel overworld(MinecraftServer server) {
        return server.overworld();
    }

    public static ServerLevel getLevel(MinecraftServer server, ResourceKey<Level> dimension) {
        return server.getLevel(dimension);
    }

    public static Iterable<ServerLevel> getLevels(MinecraftServer server) {
        return server.getAllLevels();
    }

    public static GameRules gameRules(MinecraftServer server) {
        return server.getGameRules();
    }

    public static WorldData worldData(MinecraftServer server) {
        return server.getWorldData();
    }

    public static Difficulty getDifficulty(MinecraftServer server) {
        return worldData(server).getDifficulty();
    }

    public static void setDifficulty(MinecraftServer server, Difficulty difficulty, boolean force) {
        server.setDifficulty(difficulty, force);
    }

    public static GameType getDefaultGameType(MinecraftServer server) {
        return server.getDefaultGameType();
    }

    public static void setDefaultGameType(MinecraftServer server, GameType type) {
        server.setDefaultGameType(type);
    }

    public static boolean isHardcore(MinecraftServer server) {
        return server.isHardcore();
    }

    // Registries / managers
    public static Commands commands(MinecraftServer server) {
        return server.getCommands();
    }

    public static void runCommandAs(CommandSourceStack source, String command) {
        commands(source.getServer()).performPrefixedCommand(source, command);
    }

    public static void runCommandAsServer(MinecraftServer server, String command) {
        commands(server).performPrefixedCommand(server.createCommandSourceStack(), command);
    }

    public static ServerFunctionManager functions(MinecraftServer server) {
        return server.getFunctions();
    }

    public static ServerAdvancementManager advancements(MinecraftServer server) {
        return server.getAdvancements();
    }

    public static RecipeManager recipeManager(MinecraftServer server) {
        return server.getRecipeManager();
    }

    public static StructureTemplateManager structureManager(MinecraftServer server) {
        return server.getStructureManager();
    }

    public static ServerScoreboard scoreboard(MinecraftServer server) {
        return server.getScoreboard();
    }

    public static CustomBossEvents bossEvents(MinecraftServer server) {
        return server.getCustomBossEvents();
    }

    // Networking / identity
    public static ServerConnectionListener getConnection(MinecraftServer server) {
        return server.getConnection();
    }

    public static LevelBasedPermissionSet getPermissionLevel(ServerPlayer player) {
        return player.level().getServer().getProfilePermissions(player.nameAndId());
    }

    public static boolean usesOnlineMode(MinecraftServer server) {
        return server.usesAuthentication();
    }

    public static boolean usesOfflineMode(MinecraftServer server) {
        return !usesOnlineMode(server);
    }

    // General state
    public static boolean isRunning(MinecraftServer server) {
        return server.isRunning();
    }

    public static boolean isStopped(MinecraftServer server) {
        return server.isStopped();
    }

    public static boolean isReady(MinecraftServer server) {
        return server.isReady();
    }

    public static boolean isSaving(MinecraftServer server) {
        return server.isCurrentlySaving();
    }

    public static int getTickCount(MinecraftServer server) {
        return server.getTickCount();
    }

    public static float getAvgTickTime(MinecraftServer server) {
        return (float) (server.getAverageTickTimeNanos() / MathUtil.NANOS_IN_A_SECOND);
    }

    public static String getMotd(MinecraftServer server) {
        return server.getMotd();
    }

    public static void setMotd(MinecraftServer server, String motd) {
        server.setMotd(motd);
    }

    public static int getPort(MinecraftServer server) {
        return server.getPort();
    }

    public static void setPort(MinecraftServer server, int port) {
        server.setPort(port);
    }

    public static boolean isPvpOn(MinecraftServer server) {
        return gameRules(server).get(GameRules.PVP);
    }

    public static void setPvp(MinecraftServer server, boolean allowed) {
        gameRules(server).set(GameRules.PVP, allowed, server);
    }

    public static boolean isSingleplayer(MinecraftServer server) {
        return server.isSingleplayer();
    }

    public static int getPlayerIdleTimeout(MinecraftServer server) {
        return server.playerIdleTimeout();
    }

    public static void setPlayerIdleTimeout(MinecraftServer server, int timeout) {
        server.setPlayerIdleTimeout(timeout);
    }

    public static boolean isWhitelistOn(MinecraftServer server) {
        return server.isEnforceWhitelist();
    }

    public static void setWhitelist(MinecraftServer server, boolean status) {
        server.setEnforceWhitelist(status);
    }

    public static int absoluteMaxWorldSize(MinecraftServer server) {
        return server.getAbsoluteMaxWorldSize();
    }

    public static Optional<MinecraftServer.ServerResourcePackInfo> resourcePack(MinecraftServer server) {
        return server.getServerResourcePack();
    }

    public static boolean resourcePackRequired(MinecraftServer server) {
        return server.isResourcePackRequired();
    }

    public static String serverVersion(MinecraftServer server) {
        return server.getServerVersion();
    }

    public static void saveEverything(MinecraftServer server, boolean suppressLog, boolean flush, boolean forced) {
        server.saveEverything(suppressLog, flush, forced);
    }

    public static void saveChunks(MinecraftServer server, boolean suppressLog, boolean flush, boolean forced) {
        server.saveAllChunks(suppressLog, flush, forced);
    }

    public static void pause(MinecraftServer server) {
        server.halt(true);
    }

    public static void unpause(MinecraftServer server) {
        server.halt(false);
    }

    // Integrated only
    public static boolean isOpenToLAN(IntegratedServer server) {
        return server.isPublished();
    }

    public static boolean openToLAN(IntegratedServer server, GameType gameMode, boolean cheats, int port) {
        return server.publishServer(MinecraftServer.MultiplayerScope.LAN, gameMode, cheats, port);
    }

    // Dedicated only
    public static DedicatedServerProperties getProperties(DedicatedServer server) {
        return server.getProperties();
    }

    public static String serverIp(DedicatedServer server) {
        return server.getServerIp();
    }

    public static int serverPort(DedicatedServer server) {
        return server.getServerPort();
    }

    public static String runCommand(DedicatedServer server, String command) {
        return server.runCommand(command);
    }

    public static boolean isFlightAllowed(DedicatedServer server) {
        return server.allowFlight();
    }

    public static void setFlightAllowed(DedicatedServer server, boolean allowed) {
        server.setAllowFlight(allowed);
    }
}