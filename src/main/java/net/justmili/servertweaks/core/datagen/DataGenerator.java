package net.justmili.servertweaks.core.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.justmili.servertweaks.core.datagen.providers.tags.*;

public class DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGen) {
        var pack = dataGen.createPack();

        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(BiomeTagProvider::new);
        pack.addProvider(EntityTypeTagProvider::new);
        pack.addProvider(EnchantTagProvider::new);
    }
}
