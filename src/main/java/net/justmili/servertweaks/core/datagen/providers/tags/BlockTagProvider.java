package net.justmili.servertweaks.core.datagen.providers.tags;

import net.justmili.servertweaks.core.registries.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends TagsProvider<Block> {
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.BLOCK, future);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider registries) {

        this.tag(TagRegistry.DIET_FOLIAGE)
            .add(BlockItemIds.SHORT_GRASS.block(), BlockItemIds.TALL_GRASS.block(), BlockItemIds.SHORT_DRY_GRASS.block(), BlockItemIds.TALL_DRY_GRASS.block(),
                BlockItemIds.BUSH.block(), BlockItemIds.FERN.block(), BlockItemIds.LARGE_FERN.block(), BlockItemIds.FIREFLY_BUSH.block(),
                BlockItemIds.SUGAR_CANE.block(), BlockItemIds.SEAGRASS.block(), BlockItemIds.SEA_PICKLE.block(), BlockItemIds.KELP.block(),
                BlockItemIds.BIG_DRIPLEAF.block(), BlockItemIds.SMALL_DRIPLEAF.block(), BlockItemIds.VINE.block(), BlockItemIds.GLOW_BERRY_CROP.block(),
                BlockItemIds.GLOW_LICHEN.block()).add(BlockIds.TALL_SEAGRASS, BlockIds.CAVE_VINES_PLANT);
    }
}