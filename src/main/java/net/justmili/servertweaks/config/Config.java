package net.justmili.servertweaks.config;

import net.justmili.corelibs.config.ConfigType;
import net.justmili.corelibs.config.FileType;
import net.justmili.corelibs.config.MConfigBuilder;
import net.justmili.corelibs.config.entry.ConfigEntry;
import net.justmili.servertweaks.ServerTweaks;

public class Config {
    public static final MConfigBuilder server = new MConfigBuilder(ServerTweaks.MODID, ConfigType.SERVER, FileType.PROPERTIES, true);
    public static final MConfigBuilder common = new MConfigBuilder(ServerTweaks.MODID, ConfigType.COMMON, FileType.PROPERTIES, true);

    public static ConfigEntry<Boolean> afkCommand, despawnMonstersPostAfk;
    public static ConfigEntry<Integer> afkCooldown;
    public static ConfigEntry<Boolean> scaleCommand;
    public static ConfigEntry<Float> scaleMinHeight, scaleMaxHeight;
    public static ConfigEntry<Boolean> smpPermsCommand;
    public static ConfigEntry<Boolean> flyCommand;
    public static ConfigEntry<Boolean> damageToggleCommand;
    public static ConfigEntry<Boolean> fillExtrasCommand;
    public static ConfigEntry<Boolean> banishCommand;

    public static ConfigEntry<Boolean> playerAbilities;
    public static ConfigEntry<Boolean> obfInvisDeathMessages;
    public static ConfigEntry<Boolean> rightClickHarvest;
    public static ConfigEntry<Boolean> noAiNameTags;

    public static ConfigEntry<Boolean> enableAnvilRepair, disableAnvilLimit;
    public static ConfigEntry<Boolean> enableEnchantDuplication, enableEnchantMixing, enableHigherEnchants;

    public static ConfigEntry<Boolean> limitPlayerSpeed, limitElytraSpeed, limitVehicleSpeed;
    public static ConfigEntry<Boolean> fasterRiptideCharge;
    public static ConfigEntry<Integer> pistonPushLimit;

    public static ConfigEntry<Boolean> enableDiscordIntegrationFix;


    public static void common() {
        common.comment("Should these features be enabled on the server?");

        // Commands
        smpPermsCommand = common.define("enableSmpPermsCommand", false);
        afkCommand = common.define("enableAfkCommand", true);
        scaleCommand = common.define("enableScaleCommand", false);
        damageToggleCommand = common.define("enableDamageToggleCommand", false);
        fillExtrasCommand = common.define("enableFillExtrasCommand", false);
        banishCommand = common.define("enableBanishCommand", false);
        flyCommand = common.define("enableFlyCommand", true);

        despawnMonstersPostAfk =
            common.comment("Should \"wild\" (unnamed, not in boats/minecarts) monsters despawn around the player when coming out of AFK?")
            .define("despawnMonstersPostAfk", true);
        afkCooldown = common.comment("Amount of time between the AFK command can be used again")
            .define("afkCommandCooldown", 6000, 0, Integer.MAX_VALUE - 255);

        common.comment("What should be the min-max height values (In centimeters) for the \"/scale\" command?");
        scaleMinHeight = common.define("scaleMinHeight", 80f, 18.5f, 2960f);
        scaleMaxHeight = common.define("scaleMaxHeight", 300f, 18.5f, 2960f);

        // Enchanting
        enableEnchantMixing =
            common.comment("Should previously incompatible enchantments be able to be combined?")
            .define("enableEnchantMixing", false);
        enableHigherEnchants =
            common.comment("Should some enchantments (controlled by enchantment tags) have a higher max value than Vanilla intended?")
            .define("enableHigherEnchantmentLevels", false);
        enableEnchantDuplication = common.comment("""
            Should players be able to shift-right-click with an enchanted book in their offhand and a book in their main hand" +
            to duplicate the enchanted book using their experience?
            """).define("enableEnchantDuplication", true);

        // Limits
        var limitsWhenText = "Should the server stop the player from moving too fast and print \"Player moved too fast!\" warn when ";
        limitPlayerSpeed = common.comment(limitsWhenText + "on foot?").define("limitPlayerSpeed", false);
        limitElytraSpeed = common.comment(limitsWhenText + "using elytra?").define("limitElytraSpeed", false);
        limitVehicleSpeed = common.comment(limitsWhenText + "in/on vehicle?").define("limitVehicleSpeed", false);
        disableAnvilLimit =
            common.comment("Should the server clamp the max anvil cost to 39 levels if at or over, to prevent \"Too Expensive\"?")
            .define("disableAnvilLimit", true);
        fasterRiptideCharge =
            common.comment("Should the right-click-hold time be shorter (by half) in order to use a Riptide Trident?")
            .define("fasterRiptideCharge", true);
        pistonPushLimit =
            common.comment("How many blocks should the piston be able to push?")
            .define("pistonPushLimit", 12, 0, 511);

        // QoL, Gameplay
        rightClickHarvest =
            common.comment("Should the player be able to harvest crops with by just right-clicking?")
            .define("rightClickHarvest", true);
        enableAnvilRepair =
            common.comment("Should a player be able to fix anvils by shift-right-clicking them with iron ingots and iron blocks?")
            .define("anvilRepair", true);
        noAiNameTags =
            common.comment("Should Villagers and Tamable mobs lose their AI when named \"NoAI\"?")
            .define("noAiNameTags", true);
        obfInvisDeathMessages =
            common.comment("Should names of invisible players be obfuscated in chat in death messages?")
            .define("obfuscateInvisDeathMessages", true);

        // Experimental, Fun
        playerAbilities = common.comment("Allows server owners to configure player abilities for some or all members")
            .define("playerAbilities", false);

        common.build();
    }

    public static void server() {

        server.comment("Should these features be enabled on the server?");

        // Fixes and Workarounds
        enableDiscordIntegrationFix = server.comment("""
            Should the mod attempt to fix issue #51 of Discord Integration?
            https://github.com/ErdbeerbaerLP/DiscordIntegration/issues/51
            """).define("enableDiscordIntegrationFix", true);

        server.build();
    }
}