package net.justmili.corelibs.util.utils.common;

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

public class TagUtil {

    public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registries, String name) {
        return TagKey.create(registries, ServerTweaks.asId(name));
    }

    public static TagKey<Block> block(String name) {
        return create(Registries.BLOCK, name);
    }

    public static TagKey<Item> item(String name) {
        return create(Registries.ITEM, name);
    }

    public static TagKey<Biome> biome(String name) {
        return create(Registries.BIOME, name);
    }

    public static TagKey<EntityType<?>> entityType(String name) {
        return create(Registries.ENTITY_TYPE, name);
    }

    public static TagKey<Enchantment> enchant(String name) {
        return create(Registries.ENCHANTMENT, name);
    }
}
