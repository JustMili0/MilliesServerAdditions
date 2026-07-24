package net.justmili.servertweaks.config;

import net.justmili.libs.v1.config.ConfigType;
import net.justmili.libs.v1.config.FileType;
import net.justmili.libs.v1.config.MConfigBuilder;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.servertweaks.ServerTweaks;

public class Config {
    public static ConfigEntry<Boolean> // Commands
        enableAfkCommand,
        enableScaleCommand,
        enableDamageToggleCommand,
        enableBanishCommand,
        enableFlyCommand,
        despawnMonsters;
    public static ConfigEntry<Boolean> // Features
        rightClickHarvest,
        enableAnvilRepair,
        enableEnchantDuplication,
        playerAbilities;
    public static ConfigEntry<Boolean> // Mixin Features
        limitPlayerSpeed,
        limitElytraSpeed,
        limitVehicleSpeed,
        noAiNameTags,
        enableHigherEnchants,
        disableAnvilLimit,
        allowMixEnchantments;

    public static ConfigEntry<Integer> // Feature/Command config
        afkCommandCooldown;
    public static ConfigEntry<Double>
        scaleMinHeight, scaleMaxHeight;
    public static ConfigEntry<Integer> // Mixin Features
        pistonPushLimit;

    public static void register() {
        MConfigBuilder server = new MConfigBuilder(ServerTweaks.MODID, ConfigType.SERVER, FileType.PROPERTIES, true);
        MConfigBuilder mixins = new MConfigBuilder(ServerTweaks.MODID, ConfigType.MIXINS, FileType.PROPERTIES, true);

        // Features/Commands
        server.comment("Should these commands and features be enabled on the server?");

        enableAfkCommand = server.define("enableAfkCommand", true);
        enableScaleCommand = server.define("enableScaleCommand", true);
        enableDamageToggleCommand = server.define("enableDamageToggleCommand", true);
        enableBanishCommand = server.define("enableBanishCommand", true);
        enableFlyCommand = server.define("enableFlyCommand", true);

        despawnMonsters = server.comment("Should \"wild\" monsters despawn around the player when coming out of AFK?")
            .define("despawnMonsters", true);
        afkCommandCooldown = server.comment("Amount of time between the AFK command can be used again")
            .define("afkCommandCooldown", 6000, 0, Integer.MAX_VALUE-255);

        server.comment("What should be the min-max height values (In centimeters) for the \"/scale\" command?");
        scaleMinHeight = server.define("scaleMinHeight", 80.0, 18.5, 2960.0);
        scaleMaxHeight = server.define("scaleMaxHeight", 300.0, 18.5, 2960.0);

        rightClickHarvest = server.comment("Should the player be able to harvest crops with by just right-clicking?")
            .define("rightClickHarvest", true);
        enableAnvilRepair = server.comment("Should a player be able to fix anvils by shift-right-clicking them with iron ingots and iron blocks?")
            .define("anvilRepair", true);
        enableEnchantDuplication = server.comment("")
            .define("enableEnchantDuplication", true);
        playerAbilities = server.comment("[EXPERIMENTAL] Allows server owners to configure player abilities for some or all members")
            .define("playerAbilities", false);

        server.build();

        // Mixins
        mixins.comment("Should these mixins apply on the server?");

        enableHigherEnchants =  mixins.comment("Should some enchantments (controlled by enchantment tags) have a higher max value than Vanilla intended?")
            .define("enableHigherEnchantmentLevels", false);

        limitPlayerSpeed = mixins.comment("Should the server stop the player from moving too fast and print \"Player moved too fast!\" warn when on foot?")
            .define("limitPlayerSpeed", false);
        limitElytraSpeed = mixins.comment("Should the server stop the player from flying too fast and print \"Player moved too fast!\" warn when on elytra?")
            .define("limitElytraSpeed", false);
        limitVehicleSpeed = mixins.comment("Should the server stop the player from going too fast and print \"Player moved too fast!\" warn when in/on vehicle?")
            .define("limitVehicleSpeed", false);
        disableAnvilLimit = mixins.comment("Should the server clamp the max anvil cost to 39 levels if at or over, to prevent \"Too Expensive\"?")
            .define("disableAnvilLimit", true);
        allowMixEnchantments = mixins.comment("Should previously incompatible enchantments be able to be combined?")
            .define("allowMixEnchantments", false);
        pistonPushLimit = mixins.comment("How many blocks should the piston be able to push?")
            .define("pistonPushLimit", 12, 0, 511);

        noAiNameTags = mixins.comment("Should Villagers and Tamable mobs lose their AI when named \"NoAI\"?")
            .define("noAiNameTags", true);

        mixins.build();
    }
}