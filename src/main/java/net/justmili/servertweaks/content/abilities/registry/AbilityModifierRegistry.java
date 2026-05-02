package net.justmili.servertweaks.content.abilities.registry;

import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbilityModifierRegistry {
    // Registry
    private static final Map<String, AbilityModifier> REGISTRY = new HashMap<>();

    public static final AbilityModifier ADD_GOLD_FOODS_TO_DIET = register(new AbilityModifier("ADD_GOLD_FOODS_TO_DIET"));

    private static AbilityModifier register(AbilityModifier modifier) {
        REGISTRY.put(modifier.getName(), modifier);
        return modifier;
    }

    public static Set<String> getNames() {
        return REGISTRY.keySet();
    }

    public static @Nullable AbilityModifier byName(String name) {
        return REGISTRY.get(name);
    }
}
