package net.justmili.servertweaks.core.datagen.providers.tags;

import net.justmili.servertweaks.core.registries.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends TagsProvider<Item> {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ITEM, future);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider registries) {

        this.tag(TagRegistry.DIET_MEAT)
            .add(ItemIds.PORKCHOP, ItemIds.COOKED_PORKCHOP, ItemIds.BEEF, ItemIds.COOKED_BEEF, ItemIds.MUTTON, ItemIds.COOKED_MUTTON,
                ItemIds.CHICKEN, ItemIds.COOKED_CHICKEN, ItemIds.COD, ItemIds.COOKED_COD, ItemIds.SALMON, ItemIds.COOKED_SALMON,
                ItemIds.TROPICAL_FISH, ItemIds.PUFFERFISH, ItemIds.RABBIT, ItemIds.COOKED_RABBIT, ItemIds.RABBIT_STEW,
                ItemIds.ROTTEN_FLESH, ItemIds.HONEY_BOTTLE);

        this.tag(TagRegistry.DIET_VEGE)
            .add(ItemIds.BREAD, BlockItemIds.CARROT_CROP.item(), ItemIds.BAKED_POTATO, BlockItemIds.POTATO_CROP.item(), ItemIds.POISONOUS_POTATO, ItemIds.BEETROOT,
                ItemIds.BEETROOT_SOUP, ItemIds.MELON_SLICE, BlockItemIds.SWEET_BERRY_CROP.item(), BlockItemIds.GLOW_BERRY_CROP.item(), ItemIds.CHORUS_FRUIT,
                ItemIds.MUSHROOM_STEW, ItemIds.SUSPICIOUS_STEW, ItemIds.DRIED_KELP, ItemIds.APPLE, ItemIds.COOKIE,
                ItemIds.PUMPKIN_PIE, BlockItemIds.CAKE.item(), ItemIds.HONEY_BOTTLE, ItemIds.MILK_BUCKET);

        this.tag(TagRegistry.DIET_SUGAR)
            .add(BlockItemIds.SWEET_BERRY_CROP.item(), BlockItemIds.GLOW_BERRY_CROP.item(), ItemIds.COOKIE, BlockItemIds.CAKE.item(), ItemIds.HONEY_BOTTLE,
                ItemIds.MELON_SLICE, ItemIds.PUMPKIN_PIE, ItemIds.APPLE, ItemIds.GOLDEN_APPLE, ItemIds.ENCHANTED_GOLDEN_APPLE);

        this.tag(TagRegistry.DIET_EW_ITEMS)
            .add(ItemIds.SLIME_BALL, ItemIds.MAGMA_CREAM, ItemIds.SPIDER_EYE);

        this.tag(TagRegistry.DIET_GOLD)
            .add(ItemIds.GOLDEN_APPLE, ItemIds.ENCHANTED_GOLDEN_APPLE, ItemIds.GOLDEN_CARROT);
    }
}