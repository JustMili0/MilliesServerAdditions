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
    public static final TagKey<Item>
        DIET_CARNIVORE = createTag(Registries.ITEM, "diets/carnivore"),
        DIET_VEGETARIAN = createTag(Registries.ITEM, "diets/vegetarian"),
        DIET_SWEETS = createTag(Registries.ITEM, "diets/saccharivore"),
        DIET_BUG_ITEMS = createTag(Registries.ITEM, "diets/insectivore"),
        DIET_MODIFIER_GOLDEN_FOODS = createTag(Registries.ITEM, "diets/modifier_golden_foods");

    public static final TagKey<Block>
        DIET_FOLIAGE = createTag(Registries.BLOCK, "diets/herbivore"),
        DIET_ALLOW_BLOCK_INTERACTION = createTag(Registries.BLOCK, "diets/allow_block_interaction");

    public static final TagKey<EntityType<?>>
        DIET_BUG_ENTITY_GENERIC = createTag(Registries.ENTITY_TYPE, "bug_eater/generic"),
        DIET_BUG_ENTITY_FIRE = createTag(Registries.ENTITY_TYPE, "bug_eater/hot"),
        DIET_BUG_ENTITY_POISON = createTag(Registries.ENTITY_TYPE, "bug_eater/poisonous"),
        DIET_BUG_ENTITY_NUTRITIOUS = createTag(Registries.ENTITY_TYPE, "bug_eater/nutritious"),
        DIET_BUG_ENTITY_SATURATING = createTag(Registries.ENTITY_TYPE, "bug_eater/saturating");

    public static final TagKey<Biome>
        HOT_BIOMES = createTag(Registries.BIOME, "hot_biomes"),
        COLD_BIOMES = createTag(Registries.BIOME, "cold_biomes"),
        HYDROPHOBIC_HELMET_EXCEPTIONS = createTag(Registries.BIOME, "hydrophobic_helmet_exceptions");

    public static final TagKey<Enchantment>
        ENCHANT_BOOST_1 = createTag(Registries.ENCHANTMENT, "boosted_level_by_1"),
        ENCHANT_BOOST_2 = createTag(Registries.ENCHANTMENT, "boosted_level_by_2"),
        ENCHANT_BOOST_3 = createTag(Registries.ENCHANTMENT, "boosted_level_by_3"),
        ENCHANT_BOOST_4 = createTag(Registries.ENCHANTMENT, "boosted_level_by_4"),
        ENCHANT_BOOST_5 = createTag(Registries.ENCHANTMENT, "boosted_level_by_5");

    private static <T> TagKey<T> createTag(ResourceKey<? extends Registry<T>> registries, String path) {
        return TagKey.create(registries, ServerTweaks.asResource(path));
    }
}