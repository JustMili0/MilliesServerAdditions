package net.justmili.servertweaks.registries;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class TagRegistry {
    public static final TagKey<Block>
        DIET_FOLIAGE = block("diets/herbivore");

    public static final TagKey<Item>
        DIET_CARNIVORE = item("diets/carnivore"),
        DIET_VEGETARIAN = item("diets/vegetarian"),
        DIET_SWEETS = item("diets/saccharivore"),
        DIET_BUG_ITEMS = item("diets/insectivore"),
        DIET_MODIFIER_GOLDEN_FOODS = item("diets/modifier_golden_foods");

    public static final TagKey<EntityType<?>>
        DIET_BUG_ENTITY_GENERIC = entityType("bug_eater/generic"),
        DIET_BUG_ENTITY_FIRE = entityType("bug_eater/hot"),
        DIET_BUG_ENTITY_POISON = entityType("bug_eater/poisonous"),
        DIET_BUG_ENTITY_NUTRITIOUS = entityType("bug_eater/nutritious"),
        DIET_BUG_ENTITY_SATURATING = entityType("bug_eater/saturating");

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

    private static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registries, String path) {
        return TagKey.create(registries, ServerTweaks.asId(path));
    }

    private static TagKey<Block> block(String path) {
        return create(Registries.BLOCK, path);
    }

    private static TagKey<Item> item(String path) {
        return create(Registries.ITEM, path);
    }

    private static TagKey<Biome> biome(String path) {
        return create(Registries.BIOME, path);
    }

    private static TagKey<EntityType<?>> entityType(String path) {
        return create(Registries.ENTITY_TYPE, path);
    }

    private static TagKey<Enchantment> enchant(String path) {
        return create(Registries.ENCHANTMENT, path);
    }
}