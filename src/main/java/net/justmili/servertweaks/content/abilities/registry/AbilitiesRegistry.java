package net.justmili.servertweaks.content.abilities.registry;

import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.AbilityUtil;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.TickingAbility;
import net.justmili.servertweaks.mixin.accessors.FoxAccessor;
import net.justmili.servertweaks.core.util.ScalerUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AbilitiesRegistry {
    /// Extra Ability variables
    private static final Identifier SLOW_SPEED = ServerTweaks.asResource("slow_speed");
    private static final Identifier STRONG_HP = ServerTweaks.asResource("strong_health");
    private static final Identifier STRONG_DAMAGE = ServerTweaks.asResource("strong_damage");

    /// Registry
    private static final Map<String, Ability> REGISTRY = new HashMap<>();

    public static final Ability FIRE_IMMUNE = register(new Ability("FIRE_IMMUNE"));
    public static final Ability FREEZE_IMMUNE = register(new Ability("FREEZE_IMMUNE"));
    public static final Ability FALL_IMMUNE = register(new Ability("FALL_IMMUNE"));
    public static final Ability HEAT_SENSITIVE = register(new HeatSensitive());
    public static final Ability COLD_SENSITIVE = register(new ColdSensitive());
    public static final Ability LIGHT = register(new Light());
    public static final Ability SWIFT = register(new Swift());
    public static final Ability SLOW = register(new Slow());
    public static final Ability HOPPY = register(new Hoppy());
    public static final Ability DWARF = register(new Dwarf());
    public static final Ability TOUGH = register(new Ability("TOUGH"));
    public static final Ability STRONG = register(new Strong());
    public static final Ability AQUA_GRACE = register(new AquaGrace());
    public static final Ability BREATHES_UNDERWATER = register(new BreathesUnderwater());
    public static final Ability CANT_BREATHE_AIR = register(new CantBreatheAir());
    public static final Ability CANT_SWIM = register(new CantSwim());                               // DOESN'T WORK
    public static final Ability HYDROPHOBIC = register(new Hydrophobic());
    public static final Ability HUNTED_BY_FOX = register(new HuntedByFox());                        // KINDA WORKS
    public static final Ability HUNTED_BY_WOLF = register(new HuntedByWolf());
    public static final Ability SCARES_CREEPERS = register(new ScaresCreepers());
    public static final Ability SCARES_PHANTOMS = register(new ScaresPhantoms());
    public static final Ability FRIENDS_WITH_NATURE = register(new FriendsWithNature());            // KINDA WORKS
    public static final Ability WEAK_TO_DAMAGE = register(new Ability("WEAK_TO_DAMAGE"));     // DOESN'T WORK
    public static final Ability NIGHT_VISION = register(new NightVision());
    public static final Ability BURNS_IN_DAYLIGHT = register(new BurnsInDaylight());                // KINDA WORKS
    public static final Ability IS_MONSTER = register(new IsMonster());
    public static final Ability CLIMBS_WALLS = register(new Ability("CLIMBS_WALLS"));         // DOESN'T WORK
    public static final Ability CARNIVORE = register(new Ability("CARNIVORE"));               // KINDA WORKS
    public static final Ability VEGETARIAN = register(new Ability("VEGETARIAN"));             // KINDA WORKS
    public static final Ability ONLY_EATS_SWEETS = register(new Ability("ONLY_EATS_SWEETS")); // KINDA WORKS
    public static final Ability GRASS_EATER = register(new Ability("GRASS_EATER"));

    private static Ability register(Ability ability) {
        REGISTRY.put(ability.getName(), ability);
        return ability;
    }

    public static @Nullable Ability byName(String name) {
        return REGISTRY.get(name);
    }

    /// Define ticking abilities

    // FIRE_IMMUNE - AbilityEffects (specialDamageImmune)
    // FREEZE_IMMUNE - AbilityEffects (specialDamageImmune)
    // FALL_IMMUNE - AbilityEffects (specialDamageImmune)

    static class HeatSensitive extends TickingAbility {
        HeatSensitive() { super("HEAT_SENSITIVE"); }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.getGameTime() % 20 != 0) return;
            if (!level.getBiome(player.blockPosition()).is(AbilityTags.HOT_BIOMES)) return;
            player.hurt(level.damageSources().onFire(), 1.0F);
        }
    }

    static class ColdSensitive extends TickingAbility {
        ColdSensitive() { super("COLD_SENSITIVE"); }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!level.getBiome(player.blockPosition()).is(AbilityTags.COLD_BIOMES)) return;
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.LEATHER_HELMET)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(Items.LEATHER_LEGGINGS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS)) return;
            // Completely cancel any effects if player has full leather armor.
            // Still applies freezing overlays but stops damage when armor isn't full leather. It is an intended side effect.

            int targetTime = player.getTicksRequiredToFreeze() + 20;
            player.setTicksFrozen(targetTime);
            player.getEntityData().set(Entity.DATA_TICKS_FROZEN, targetTime, true);
        }
    }

    static class Light extends TickingAbility {
        Light() {
            super("LIGHT");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.getDeltaMovement().y < -0.4) AbilityUtil.applyEffect(player, MobEffects.SLOW_FALLING);
            if (player.onGround()) player.removeEffect(MobEffects.SLOW_FALLING);
        }
    }

    static class Swift extends TickingAbility {
        Swift() {
            super("SWIFT");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isSprinting()) AbilityUtil.applyEffect(player, MobEffects.SPEED, 30, 0);
        }
    }

    static class Slow extends TickingAbility {
        Slow() {
            super("SLOW");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);

            if (speed.getModifier(SLOW_SPEED) == null) {
                speed.addTransientModifier(new AttributeModifier(SLOW_SPEED, -0.47, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }
    }

    static class Hoppy extends TickingAbility {
        Hoppy() {
            super("HOPPY");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            AbilityUtil.applyEffect(player, MobEffects.JUMP_BOOST);
        }
    }

    static class Dwarf extends TickingAbility {
        Dwarf() {
            super("DWARF");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            AttributeInstance scale = ScalerUtil.getScale(player);
            if (scale != null && scale.getBaseValue() > 0.75) ScalerUtil.setScale(player, 0.75);
            AbilityUtil.applyEffect(player, MobEffects.HASTE, 1);
        }
    }

    // TOUGH - LivingEntityMixin (knockback)

    static class Strong extends TickingAbility {
        Strong() {
            super("STRONG");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.getGameTime() % 5 != 0) return;

            int armor = player.getArmorValue();
            float targetHp = Math.max(40.0F, Math.min(100.0F, 100.0F - (armor * 3.0F)));
            AttributeInstance maxHp = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHp != null) {
                maxHp.removeModifier(STRONG_HP);
                maxHp.addTransientModifier(new AttributeModifier(STRONG_HP, targetHp - 20.0, AttributeModifier.Operation.ADD_VALUE));
                if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            }
            AttributeInstance attack = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attack != null && attack.getModifier(STRONG_DAMAGE) == null) {
                attack.addTransientModifier(new AttributeModifier(STRONG_DAMAGE, 3.0, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    static class AquaGrace extends TickingAbility {
        AquaGrace() {
            super("AQUA_GRACE");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.isInWater()) return;
            AbilityUtil.applyEffect(player, MobEffects.CONDUIT_POWER);
            if (level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .get(Enchantments.DEPTH_STRIDER)
                .map(h -> EnchantmentHelper.getItemEnchantmentLevel(h, player.getItemBySlot(EquipmentSlot.HEAD)) > 1)
                .orElse(false)) return; // Return before granting Dolphin's Grace if player has depth strider to prevent OP swimming speeds
            AbilityUtil.applyEffect(player, MobEffects.DOLPHINS_GRACE);
        }
    }

    // BREATHES_UNDERWATER ticking + AbilityEffects (specialDamageImmune)
    static class BreathesUnderwater extends TickingAbility {
        BreathesUnderwater() {
            super("BREATHES_UNDERWATER");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isInWater()) AbilityUtil.applyEffect(player, MobEffects.WATER_BREATHING, 30, 0);
        }
    }

    // CANT_BREATHE_AIR ticking + LivingEntityMixin (increaseAirSupply) to block vanilla air restoration on land
    static class CantBreatheAir extends TickingAbility {
        CantBreatheAir() {
            super("CANT_BREATHE_AIR");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isInWater()) {
                // Restore air when in water
                if (player.getAirSupply() < player.getMaxAirSupply())
                    player.setAirSupply(player.getAirSupply() + 4);
            } else {
                // Drain air on land
                if (player.hasEffect(MobEffects.WATER_BREATHING)) return; // Return and don't drain air if has water breathing on
                player.setAirSupply(player.getAirSupply() - 1);
                if (player.getAirSupply() <= - 20) {
                    player.setAirSupply(0);
                    player.hurt(level.damageSources().drown(), 2.0F);
                }
            }
        }
    }

    static class CantSwim extends TickingAbility {
        CantSwim() {
            super("CANT_SWIM");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.isInWater()) return;
            Vec3 delta = player.getDeltaMovement();
            if (delta.y > 0) player.setDeltaMovement(delta.x, -0.1, delta.z);
        }
    }

    static class Hydrophobic extends TickingAbility {
        Hydrophobic() {
            super("HYDROPHOBIC");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            boolean inWaterBlock = player.isInWater();
            boolean inWaterCauldron = level.getBlockState(player.blockPosition()).is(Blocks.WATER_CAULDRON);

            boolean hasHelmet = !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
            boolean inWetBiome = level.getBiome(player.blockPosition()).is(AbilityTags.HYDROPHOBIC_HELMET_PROTECTION_EXCEPTIONS);
            boolean inRain = player.isInRain() && (!hasHelmet || inWetBiome);

            boolean inWater = inWaterBlock || inRain || inWaterCauldron;

            if (inWater && level.getGameTime() % 20 == 0) player.hurt(level.damageSources().magic(), 1.0F);
        }
    }

    static class HuntedByFox extends TickingAbility {
        HuntedByFox() {
            super("HUNTED_BY_FOX");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            for (Fox fox : AbilityUtil.getNearby(player, Fox.class, 12.0)) {
                if (((FoxAccessor) fox).invokeTrusts(player)) continue;
                if (fox.getTarget() == null) fox.setTarget(player);
            }
        }
    }

    static class HuntedByWolf extends TickingAbility {
        HuntedByWolf() {
            super("HUNTED_BY_WOLF");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            for (Wolf wolf : AbilityUtil.getNearby(player, Wolf.class, 16.0)) {
                if (wolf.isTame()) continue;
                if (wolf.getTarget() == null) wolf.setTarget(player);
            }
        }
    }

    static class ScaresCreepers extends TickingAbility {
        ScaresCreepers() {
            super("SCARES_CREEPERS");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            for (Creeper creeper : AbilityUtil.getNearby(player, Creeper.class, 8.0)) {
                creeper.setTarget(null);
                creeper.getNavigation().moveTo(
                    creeper.getX() + (creeper.getX() - player.getX()),
                    creeper.getY(),
                    creeper.getZ() + (creeper.getZ() - player.getZ()), 1.2);
            }
        }
    }

    static class ScaresPhantoms extends TickingAbility {
        ScaresPhantoms() {
            super("SCARES_PHANTOMS");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            for (Phantom phantom : AbilityUtil.getNearby(player, Phantom.class, 16.0)) {
                phantom.setTarget(null);
                phantom.getNavigation().moveTo(
                    phantom.getX() + (phantom.getX() - player.getX()),
                    phantom.getY() + 8,
                    phantom.getZ() + (phantom.getZ() - player.getZ()), 1.2);
            }
        }
    }

    static class FriendsWithNature extends TickingAbility {
        FriendsWithNature() {
            super("FRIENDS_WITH_NATURE");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            for (Fox fox : AbilityUtil.getNearby(player, Fox.class, 24.0)) {
                FoxAccessor accessor = (FoxAccessor) fox;
                if (accessor.invokeTrusts(player)) continue;
                accessor.invokeAddTrustedEntity(player);
            }
            for (Wolf wolf : AbilityUtil.getNearby(player, Wolf.class, 24.0)) {
                if (!wolf.isTame() && wolf.getTarget() == player) wolf.setTarget(null);
            }
            // Aggro prevention - MobMixin (tick) + TargetingConditionsMixin (test)
            // 100% tame chance - TamableAnimalMixin (tame)
        }
    }

    // WEAK_TO_DAMAGE - LivingEntityMixin (actuallyHurt)

    static class NightVision extends TickingAbility {
        NightVision() {
            super("NIGHT_VISION");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.isDarkOutside()) AbilityUtil.applyEffect(player, MobEffects.NIGHT_VISION, 320, 0);
        }
    }

    static class BurnsInDaylight extends TickingAbility {
        BurnsInDaylight() {
            super("BURNS_IN_DAYLIGHT");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!level.isBrightOutside() || !level.canSeeSky(player.blockPosition())) return;
            if ((level.getBrightness(LightLayer.SKY, player.blockPosition()) <= 8)) return;
            boolean hasHelmet = !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
            if (hasHelmet) return;
            if (player.isInWater()) {
                if (level.getGameTime() % 20 == 0) player.hurt(level.damageSources().magic(), 0.5F);
            } else {
                player.igniteForSeconds(2);
            }
        }
    }

    static class IsMonster extends TickingAbility {
        IsMonster() {
            super("IS_MONSTER");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            // Fear
            for (Villager villager : AbilityUtil.getNearby(player, Villager.class, 16.0)) {
                villager.getNavigation().moveTo(
                    villager.getX() + (villager.getX() - player.getX()),
                    villager.getY(),
                    villager.getZ() + (villager.getZ() - player.getZ()), 0.5);
            }

            // Attack
            for (IronGolem ironGolem : AbilityUtil.getNearby(player, IronGolem.class, 16.0)) {
                if (ironGolem.getTarget() != player) ironGolem.setTarget(player);
            }
            for (SnowGolem snowGolem : AbilityUtil.getNearby(player, SnowGolem.class, 24.0)) {
                if (snowGolem.getTarget() != player) snowGolem.setTarget(player);
            }

            // Ignore
            for (Pillager pillager : AbilityUtil.getNearby(player, Pillager.class, 64.0)) {
                if (pillager.getTarget() == player) pillager.setTarget(null);
            }
            for (Zombie zombie : AbilityUtil.getNearby(player, Zombie.class, 35.0)) {
                if (zombie.getTarget() == player) zombie.setTarget(null);
            }
            for (Skeleton skeleton : AbilityUtil.getNearby(player, Skeleton.class, 16.0)) {
                if (skeleton.getTarget() == player) skeleton.setTarget(null);
            }
        }
    }

    // CLIMBS_WALLS - LivingEntityMixin (onClimbable) + AbilityEffects (shouldClimb bool)

    // CARNIVORE - AbilityEffects (RIGHT_CLICK_BLOCK + RIGHT_CLICK_ITEM)
    // VEGETARIAN - AbilityEffects (RIGHT_CLICK_BLOCK + RIGHT_CLICK_ITEM)
    // ONLY_EATS_SWEETS - AbilityEffects (RIGHT_CLICK_BLOCK + RIGHT_CLICK_ITEM)
    // GRASS_EATER - AbilityEffects (RIGHT_CLICK_BLOCK)
}
