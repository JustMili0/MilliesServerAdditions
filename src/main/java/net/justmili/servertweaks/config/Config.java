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
        allowMixEnchantments,
        obfInvisDeathMessages;

    public static ConfigEntry<Integer> // Feature/Command config
        afkCommandCooldown;
    public static ConfigEntry<Float>
        scaleMinHeight, scaleMaxHeight;
    public static ConfigEntry<Integer> // Mixin Features
        pistonPushLimit;

    public static void register() {
        MConfigBuilder common = new MConfigBuilder(ServerTweaks.MODID, ConfigType.COMMON, FileType.PROPERTIES, true);

        // Commands
        common.comment("Should these commands and features be enabled on the server?");

        enableAfkCommand = common.define("enableAfkCommand", true);
        enableScaleCommand = common.define("enableScaleCommand", false);
        enableDamageToggleCommand = common.define("enableDamageToggleCommand", true);
        enableBanishCommand = common.define("enableBanishCommand", false);
        enableFlyCommand = common.define("enableFlyCommand", true);

        despawnMonsters = common.comment("Should \"wild\" monsters despawn around the player when coming out of AFK?")
            .define("despawnMonsters", true);
        afkCommandCooldown = common.comment("Amount of time between the AFK command can be used again")
            .define("afkCommandCooldown", 6000, 0, Integer.MAX_VALUE-255);

        common.comment("What should be the min-max height values (In centimeters) for the \"/scale\" command?");
        scaleMinHeight = common.define("scaleMinHeight", 80f, 18.5f, 2960f);
        scaleMaxHeight = common.define("scaleMaxHeight", 300f, 18.5f, 2960f);

        // Enchanting
        allowMixEnchantments = common.comment("Should previously incompatible enchantments be able to be combined?")
            .define("allowMixEnchantments", false);
        enableHigherEnchants =  common.comment("Should some enchantments (controlled by enchantment tags) have a higher max value than Vanilla intended?")
            .define("enableHigherEnchantmentLevels", false);
        enableEnchantDuplication = common.comment("")
            .define("enableEnchantDuplication", true);

        // Limits
        limitPlayerSpeed = common.comment("Should the server stop the player from moving too fast and print \"Player moved too fast!\" warn when on foot?")
            .define("limitPlayerSpeed", false);
        limitElytraSpeed = common.comment("Should the server stop the player from flying too fast and print \"Player moved too fast!\" warn when on elytra?")
            .define("limitElytraSpeed", false);
        limitVehicleSpeed = common.comment("Should the server stop the player from going too fast and print \"Player moved too fast!\" warn when in/on vehicle?")
            .define("limitVehicleSpeed", false);
        disableAnvilLimit = common.comment("Should the server clamp the max anvil cost to 39 levels if at or over, to prevent \"Too Expensive\"?")
            .define("disableAnvilLimit", true);
        pistonPushLimit = common.comment("How many blocks should the piston be able to push?")
            .define("pistonPushLimit", 12, 0, 511);

        // QoL, Gameplay
        rightClickHarvest = common.comment("Should the player be able to harvest crops with by just right-clicking?")
            .define("rightClickHarvest", true);
        enableAnvilRepair = common.comment("Should a player be able to fix anvils by shift-right-clicking them with iron ingots and iron blocks?")
            .define("anvilRepair", true);
        noAiNameTags = common.comment("Should Villagers and Tamable mobs lose their AI when named \"NoAI\"?")
            .define("noAiNameTags", true);
        obfInvisDeathMessages = common.comment("Should names of invisible players be obfuscated in chat in death messages?")
            .define("obfuscateInvisDeathMessages", true);

        // Experimental, Fun
        playerAbilities = common.comment("[EXPERIMENTAL] Allows server owners to configure player abilities for some or all members")
            .define("playerAbilities", false);

        common.build();
    }
}