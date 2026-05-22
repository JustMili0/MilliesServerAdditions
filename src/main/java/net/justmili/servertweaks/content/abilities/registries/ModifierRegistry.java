package net.justmili.servertweaks.content.abilities.registries;

import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModifierRegistry {
    // Registry
    private static final Map<String, AbilityModifier> REGISTRY = new HashMap<>();

    public static final AbilityModifier CAN_EAT_GOLDEN_FOOD = register(new AbilityModifier("can_eat_golden_foods"));

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
