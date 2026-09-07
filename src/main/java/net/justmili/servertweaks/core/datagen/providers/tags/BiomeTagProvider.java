package net.justmili.servertweaks.core.datagen.providers.tags;

import net.justmili.servertweaks.core.registries.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class BiomeTagProvider extends TagsProvider<Biome> {
    public BiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.BIOME, future);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider registries) {

        this.tag(TagRegistry.HOT_BIOMES)
            .add(Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA,
                Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS,
                Biomes.CRIMSON_FOREST, Biomes.NETHER_WASTES);

        this.tag(TagRegistry.COLD_BIOMES)
            .add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA, Biomes.SNOWY_SLOPES,
                Biomes.SNOWY_BEACH, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS,
                Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.FROZEN_RIVER,
                Biomes.GROVE, Biomes.DRIPSTONE_CAVES);

        this.tag(TagRegistry.HYDROPHOBIC_HELMET_EXCEPTIONS)
            .add(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE,
                Biomes.SWAMP, Biomes.MANGROVE_SWAMP,
                Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA,
                Biomes.OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_FROZEN_OCEAN,
                Biomes.FROZEN_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN);
    }
}