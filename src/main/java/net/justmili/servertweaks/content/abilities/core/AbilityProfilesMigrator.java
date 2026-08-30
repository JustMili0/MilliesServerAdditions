package net.justmili.servertweaks.content.abilities.core;

import com.google.gson.JsonObject;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class AbilityProfilesMigrator {
    public static boolean migrateAbilitiesSplit(JsonObject uuidObj, Set<Ability> abilities, Set<Debuff> debuffs) {
        if (!uuidObj.has("abilities")) return false;
        boolean migrated = false;

        for (var element : uuidObj.getAsJsonArray("abilities")) {
            var raw = element.getAsString();
            var id = Identifier.tryParse(raw);
            if (id == null) {
                ServerTweaks.LOGGER.warn("Invalid ability id '{}', skipping", raw);
                continue;
            }

            var ability = AbilityRegistries.getAbilityById(id);
            if (ability != null) {
                abilities.add(ability);
                continue;
            }

            var debuff = AbilityRegistries.getDebuffById(id);
            if (debuff != null) {
                debuffs.add(debuff);
                migrated = true;
                ServerTweaks.LOGGER.info("Migrated '{}' from abilities to debuffs", raw);
                continue;
            }

            ServerTweaks.LOGGER.warn("Unknown ability '{}', skipping", raw);
        }

        return migrated;
    }

    public static boolean migrateLegacyKey(JsonObject uuidObj, String oldKey, String newKey) {
        if (uuidObj.has(newKey) || !uuidObj.has(oldKey)) return false;
        uuidObj.add(newKey, uuidObj.remove(oldKey));
        return true;
    }
}
