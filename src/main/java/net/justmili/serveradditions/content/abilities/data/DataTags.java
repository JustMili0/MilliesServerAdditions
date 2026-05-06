package net.justmili.serveradditions.content.abilities.data;

import net.justmili.serveradditions.ServerAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class DataTags {
    public static final TagKey<Item>
        DIET_CARNIVORE = TagKey.create(Registries.ITEM, ServerAdditions.asResource("diets/carnivore")),
        DIET_VEGETARIAN = TagKey.create(Registries.ITEM, ServerAdditions.asResource("diets/vegetarian")),
        DIET_SWEETS = TagKey.create(Registries.ITEM, ServerAdditions.asResource("diets/sweets")),
        DIET_BUG_ITEMS = TagKey.create(Registries.ITEM, ServerAdditions.asResource("diets/bug_eater")),
        DIET_MODIFIER_GOLDEN_FOODS = TagKey.create(Registries.ITEM, ServerAdditions.asResource("diets/modifier_golden_foods"));

    public static final TagKey<Block> DIET_FOLIAGE = TagKey.create(Registries.BLOCK, ServerAdditions.asResource("diets/foliage"));

    public static final TagKey<EntityType<?>>
        DIET_BUG_ENTITY_GENERIC = TagKey.create(Registries.ENTITY_TYPE, ServerAdditions.asResource("bug_eater/generic")),
        DIET_BUG_ENTITY_FIRE = TagKey.create(Registries.ENTITY_TYPE, ServerAdditions.asResource("bug_eater/hot")),
        DIET_BUG_ENTITY_POISON = TagKey.create(Registries.ENTITY_TYPE, ServerAdditions.asResource("bug_eater/poisonous")),
        DIET_BUG_ENTITY_HIGH_SATURATION = TagKey.create(Registries.ENTITY_TYPE, ServerAdditions.asResource("bug_eater/high_saturation"));

    public static final TagKey<Biome>
        HOT_BIOMES = TagKey.create(Registries.BIOME, ServerAdditions.asResource("hot_biomes")),
        COLD_BIOMES = TagKey.create(Registries.BIOME, ServerAdditions.asResource("cold_biomes")),
        HYDROPHOBIC_HELMET_EXCEPTIONS = TagKey.create(Registries.BIOME, ServerAdditions.asResource("hydrophobic_helmet_exceptions"));
}