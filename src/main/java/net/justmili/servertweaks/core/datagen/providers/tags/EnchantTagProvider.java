package net.justmili.servertweaks.core.datagen.providers.tags;

import net.justmili.servertweaks.core.registries.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.concurrent.CompletableFuture;

public class EnchantTagProvider extends TagsProvider<Enchantment> {
    public EnchantTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ENCHANTMENT, future);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider registries) {

        this.tag(TagRegistry.ENCHANT_BOOST_1)
            .add(Enchantments.PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.BLAST_PROTECTION,
                Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.LOOTING);

        this.tag(TagRegistry.ENCHANT_BOOST_2)
            .add(Enchantments.LUNGE, Enchantments.UNBREAKING, Enchantments.MULTISHOT);

        this.tag(TagRegistry.ENCHANT_BOOST_3)
            .add(Enchantments.FROST_WALKER, Enchantments.EFFICIENCY);

        this.tag(TagRegistry.ENCHANT_BOOST_4).add();

        this.tag(TagRegistry.ENCHANT_BOOST_5).add();
    }
}