package net.justmili.serveradditions.config;

import net.justmili.serveradditions.ServerAdditions;
import net.justmili.serveradditions.config.lib.ConfigEntry;
import net.justmili.serveradditions.config.lib.MConfigBuilder;


public class Config {
    public static ConfigEntry<Boolean>
        enableAfkCommand, // Command on/off
        enableScaleCommand, // Command on/off
        enableDamageToggleCommand, // Command on/off
        enableBanishCommand, // Command on/off
        enableFlyCommand, // Command on/off
        despawnMonsters, // Afk command related setting
        noAiNameTags, // Disable/enable NoAI name tags
        limitPlayerSpeed, // Config for "UncapSpeedLimits" mixin.
        limitElytraSpeed, // Config for "UncapSpeedLimits" mixin.
        limitVehicleSpeed, // Config for "UncapSpeedLimits" mixin.
        removeAnvilLimit, // Config for "RemoveAnvilLimit" mixin.
        rightClickHarvest, // Config for "RightClickHarvest" feature.
        anvilRepair,
        playerAbilities;

    public static ConfigEntry<Integer>
        afkCommandCooldown, // Afk command related setting
        pistonPushLimit; // Config for "BetterPushLimit" mixin.

    public static void register() {
        MConfigBuilder builder = new MConfigBuilder(ServerAdditions.MODID, "config", true);

        builder.comment("Should these commands be enabled on the server?");
        enableAfkCommand = builder.define("enableAfkCommand", true);
        enableScaleCommand = builder.define("enableScaleCommand", true);
        enableDamageToggleCommand = builder.define("enableDamageToggleCommand", true);
        enableBanishCommand = builder.define("enableBanishCommand", true);
        enableFlyCommand = builder.define("enableFlyCommand", true);

        despawnMonsters = builder.comment("Should \"wild\" monsters despawn around the player when coming out of AFK?")
            .define("despawnMonsters", true);
        afkCommandCooldown = builder.comment("Amount of time between the AFK command can be used again")
            .define("afkCommandCooldown", 6000, 0, Integer.MAX_VALUE-255);

        limitPlayerSpeed = builder.comment("Should the server stop the player from moving too fast and print \"Player moved too fast!\" warn when on foot?")
            .define("limitPlayerSpeed", true);
        limitElytraSpeed = builder.comment("Should the server stop the player from flying too fast and print \"Player moved too fast!\" warn when on elytra?")
            .define("limitElytraSpeed", false);
        limitVehicleSpeed = builder.comment("Should the server stop the player from going too fast and print \"Player moved too fast!\" warn when in/on vehicle?")
            .define("limitVehicleSpeed", true);
        removeAnvilLimit = builder.comment("Should the server clamp the max anvil cost to 39 levels if at or over, to prevent \"Too Expensive\"?")
            .define("removeAnvilLimit", true);
        pistonPushLimit = builder.comment("How many blocks should the piston be able to push?")
            .define("pistonPushLimit", 12, 0, 511);

        rightClickHarvest = builder.comment("Should the player be able to harvest crops with by just right-clicking?")
            .define("rightClickHarvest", true);
        anvilRepair = builder.comment("Should a player be able to fix anvils by shift-right-clicking them with iron ingots and iron blocks?")
            .define("anvilRepair", true);
        noAiNameTags = builder.comment("Should Villagers and Tamable mobs lose their AI when named \"NoAI\"?")
            .define("noAiNameTags", true);
        playerAbilities = builder.comment("[EXPERIMENTAL] Allows server owners to configure player abilities for some or all members")
            .define("playerAbilities", false);

        builder.build();
    }
}