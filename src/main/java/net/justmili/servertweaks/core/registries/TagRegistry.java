package net.justmili.servertweaks.core.registries;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import static net.justmili.corelibs.util.utils.common.TagUtil.*;

public class TagRegistry {
    public static final TagKey<Block>
        DIET_FOLIAGE = block("diet_herbivore");

    public static final TagKey<Item>
        DIET_MEAT = item("diet_carnivore"),
        DIET_VEGE = item("diet_vegetarian"),
        DIET_SUGAR = item("diet_saccharivore"),
        DIET_EW_ITEMS = item("diet_insectivore"),
        DIET_GOLD = item("diet_golden_food");

    public static final TagKey<EntityType<?>>
        DIET_EW_ENTITY_GENERIC = entityType("insectivore/generic"),
        DIET_EW_ENTITY_FIRE = entityType("insectivore/hot"),
        DIET_EW_ENTITY_POISON = entityType("insectivore/poisonous"),
        DIET_EW_ENTITY_NUTRITIOUS = entityType("insectivore/nutritious"),
        DIET_EW_ENTITY_SATURATING = entityType("insectivore/saturating");

    public static final TagKey<Biome>
        HOT_BIOMES = biome("hot_biomes"),
        COLD_BIOMES = biome("cold_biomes"),
        HYDROPHOBIC_HELMET_EXCEPTIONS = biome("hydrophobic_helmet_exceptions");

    public static final TagKey<Enchantment>
        ENCHANT_BOOST_1 = enchant("boosted_level_by_1"),
        ENCHANT_BOOST_2 = enchant("boosted_level_by_2"),
        ENCHANT_BOOST_3 = enchant("boosted_level_by_3"),
        ENCHANT_BOOST_4 = enchant("boosted_level_by_4"),
        ENCHANT_BOOST_5 = enchant("boosted_level_by_5");
}