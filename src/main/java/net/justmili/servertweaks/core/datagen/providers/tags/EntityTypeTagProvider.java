package net.justmili.servertweaks.core.datagen.providers.tags;

import net.justmili.servertweaks.core.registries.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends TagsProvider<EntityType<?>> {
    public EntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ENTITY_TYPE, future);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider registries) {

        this.tag(TagRegistry.DIET_EW_ENTITY_GENERIC)

            .add(EntityTypeIds.SILVERFISH, EntityTypeIds.ENDERMITE, EntityTypeIds.SLIME);
        this.tag(TagRegistry.DIET_EW_ENTITY_FIRE)
            .add(EntityTypeIds.MAGMA_CUBE);

        this.tag(TagRegistry.DIET_EW_ENTITY_POISON)
            .add(EntityTypeIds.CAVE_SPIDER, EntityTypeIds.SULFUR_CUBE);

        this.tag(TagRegistry.DIET_EW_ENTITY_NUTRITIOUS)
            .add(EntityTypeIds.CAVE_SPIDER);

        this.tag(TagRegistry.DIET_EW_ENTITY_SATURATING)
            .add(EntityTypeIds.SULFUR_CUBE);
    }
}