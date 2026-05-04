package net.justmili.servertweaks.content.abilities.data;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class DataTags {
    public static final TagKey<Item>
        DIET_CARNIVORE = TagKey.create(Registries.ITEM, ServerTweaks.asResource("diet_carnivore")),
        DIET_VEGETARIAN = TagKey.create(Registries.ITEM, ServerTweaks.asResource("diet_vegetarian")),
        DIET_SWEETS = TagKey.create(Registries.ITEM, ServerTweaks.asResource("diet_sweets")),
        DIET_BUG_ITEMS = TagKey.create(Registries.ITEM, ServerTweaks.asResource("diet_bug_items")),
        DIET_MODIFIER_GOLDEN_FOODS = TagKey.create(Registries.ITEM, ServerTweaks.asResource("diet_modifier_golden_foods"));

    public static final TagKey<Block> DIET_FOLIAGE = TagKey.create(
        Registries.BLOCK, ServerTweaks.asResource("diet_foliage"));

    public static final TagKey<EntityType<?>> DIET_BUG_ENTITIES = TagKey.create(
        Registries.ENTITY_TYPE, ServerTweaks.asResource("diet_bug_entities"));

    public static final TagKey<Biome>
        HOT_BIOMES = TagKey.create(Registries.BIOME, ServerTweaks.asResource("hot_biomes")),
        COLD_BIOMES = TagKey.create(Registries.BIOME, ServerTweaks.asResource("cold_biomes")),
        HYDROPHOBIC_HELMET_PROTECTION_EXCEPTIONS = TagKey.create(Registries.BIOME, ServerTweaks.asResource("hydrophobic_helmet_protection_exceptions"));
}