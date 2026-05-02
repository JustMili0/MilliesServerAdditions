package net.justmili.servertweaks.content.abilities.registry;

import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.AbilityUtil;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.TickingAbility;
import net.justmili.servertweaks.core.util.ScalerUtil;
import net.justmili.servertweaks.mixin.accessors.FoxAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.skeleton.Parched;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class AbilitiesRegistry {
    /// Extra Ability variables
    private static final Map<UUID, List<WrappedGoal>> storedGoals = new HashMap<>();
    private record MobData(double range, double speed) {}
    private static final Identifier SLOW_SPEED = ServerTweaks.asResource("slow_speed");
    private static final Identifier STRONG_HP = ServerTweaks.asResource("strong_health");
    private static final Identifier STRONG_DAMAGE = ServerTweaks.asResource("strong_damage");

    /// Registry
    private static final Map<String, Ability> REGISTRY = new HashMap<>();

    public static final Ability FIRE_IMMUNE = register(new Ability("FIRE_IMMUNE"));
    public static final Ability LAVA_IMMUNE = register(new Ability("LAVA_IMMUNE"));
    public static final Ability HEAT_IMMUNE = register(new Ability("HEAT_IMMUNE"));
    public static final Ability FREEZE_IMMUNE = register(new Ability("FREEZE_IMMUNE"));
    public static final Ability FALL_IMMUNE = register(new Ability("FALL_IMMUNE"));
    public static final Ability HEAT_SENSITIVE = register(new HeatSensitive());
    public static final Ability COLD_SENSITIVE = register(new ColdSensitive());
    public static final Ability LIGHT = register(new Light());
    public static final Ability SWIFT = register(new Swift());
    public static final Ability SLOW = register(new Slow());
    public static final Ability HOPPY = register(new Hoppy());
    public static final Ability DWARF = register(new Dwarf());
    public static final Ability SQUISHY = register(new Ability("SQUISHY")); // TODO #5
    public static final Ability MAGNETIC = register(new Ability("MAGNETIC")); // TODO #5
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
    public static final Ability BURNS_IN_DAYLIGHT = register(new BurnsInDaylight());
    public static final Ability IS_MONSTER = register(new IsMonster());
    public static final Ability CLIMBS_WALLS = register(new Ability("CLIMBS_WALLS"));         // DOESN'T WORK
    public static final Ability PEARLING = register(new Ability("PEARLING")); // TODO #4
    public static final Ability PREDATORY = register(new Predatory());
    public static final Ability CARNIVORE = register(new Ability("CARNIVORE"));               // KINDA WORKS
    public static final Ability VEGETARIAN = register(new Ability("VEGETARIAN"));             // KINDA WORKS
    public static final Ability ONLY_EATS_SWEETS = register(new Ability("ONLY_EATS_SWEETS")); // KINDA WORKS
    public static final Ability GRASS_EATER = register(new Ability("GRASS_EATER"));
    public static final Ability BUG_EATER = register(new Ability("BUG_EATER")); // TODO #5

    private static Ability register(Ability ability) {
        REGISTRY.put(ability.getName(), ability);
        return ability;
    }

    public static Set<String> getNames() {
        return REGISTRY.keySet();
    }

    public static @Nullable Ability byName(String name) {
        return REGISTRY.get(name);
    }

    /// Define ticking abilities

    // FIRE_IMMUNE - AbilityEffects (specialDamageImmune)
    // FREEZE_IMMUNE - AbilityEffects (specialDamageImmune)
    // FALL_IMMUNE - AbilityEffects (specialDamageImmune)

    static class HeatSensitive extends TickingAbility {
        HeatSensitive() {
            super("HEAT_SENSITIVE");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 20 != 0) return;
            if (!level.getBiome(player.blockPosition()).is(AbilityTags.HOT_BIOMES)) return;
            player.hurt(level.damageSources().onFire(), 1.0F);
        }
    }

    static class ColdSensitive extends TickingAbility {
        ColdSensitive() {
            super("COLD_SENSITIVE");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!level.getBiome(player.blockPosition()).is(AbilityTags.COLD_BIOMES)) return;
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.LEATHER_HELMET)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(Items.LEATHER_LEGGINGS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS)) return;
            // Completely cancel any effects if player has full leather armor.
            // Still applies freezing overlays but stops damage when armor isn't full leather. It is an intended side effect.

            int targetTime = player.getTicksRequiredToFreeze()+20;
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
            if (!player.gameMode.isSurvival()) return;
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
                speed.addTransientModifier(new AttributeModifier(SLOW_SPEED, -0.32, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            } else {
                speed.removeModifier(SLOW_SPEED);
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

            AttributeInstance attack = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attack != null && attack.getModifier(STRONG_DAMAGE) == null) {
                attack.addTransientModifier(new AttributeModifier(STRONG_DAMAGE, 3.0, AttributeModifier.Operation.ADD_VALUE));
            } else {
                attack.removeModifier(STRONG_DAMAGE);
            }

            if (!player.gameMode.isSurvival()) return;

            int armor = player.getArmorValue();
            float targetHp = Math.max(40.0F, Math.min(100.0F, 100.0F-(armor * 3.0F)));
            AttributeInstance maxHp = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHp != null) {
                maxHp.removeModifier(STRONG_HP);
                maxHp.addTransientModifier(new AttributeModifier(STRONG_HP, targetHp-20.0, AttributeModifier.Operation.ADD_VALUE));
                if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            } else {
                maxHp.removeModifier(STRONG_HP);
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
                .orElse(false))
                return; // Return before granting Dolphin's Grace if player has depth strider to prevent OP swimming speeds
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
            if (!player.gameMode.isSurvival()) return;

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
            if (!player.gameMode.isSurvival()) return;

            if (player.isInWater() || player.hasEffect(MobEffects.WATER_BREATHING)) {
                // Restore air when in water
                if (player.getAirSupply() < player.getMaxAirSupply())
                    player.setAirSupply(player.getAirSupply()+4);
            } else {
                // Drain air on land
                player.setAirSupply(player.getAirSupply()-1);
                if (player.getAirSupply() <= -20) {
                    player.setAirSupply(1);
                    player.hurt(level.damageSources().drown(), 1.0F);
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
            if (!player.gameMode.isSurvival()) return;
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
            if (!player.gameMode.isSurvival()) return;

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
            if (!player.gameMode.isSurvival()) return;

            for (Creeper creeper : AbilityUtil.getNearby(player, Creeper.class, 8.0)) {
                creeper.setTarget(null);
                creeper.getNavigation().moveTo(
                    creeper.getX()+(creeper.getX()-player.getX()),
                    creeper.getY(),
                    creeper.getZ()+(creeper.getZ()-player.getZ()), 1.2);
            }
        }
    }

    static class ScaresPhantoms extends TickingAbility {
        ScaresPhantoms() {
            super("SCARES_PHANTOMS");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            for (Phantom phantom : AbilityUtil.getNearby(player, Phantom.class, 16.0)) {
                phantom.setTarget(null);
                phantom.getNavigation().moveTo(
                    phantom.getX()+(phantom.getX()-player.getX()),
                    phantom.getY()+8,
                    phantom.getZ()+(phantom.getZ()-player.getZ()), 1.2);
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
            if (!player.gameMode.isSurvival()) return;
            if (!level.isBrightOutside() || !level.canSeeSky(player.blockPosition())) return;
            if ((level.getBrightness(LightLayer.SKY, player.blockPosition()) <= 8)) return;

            if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) return;
            player.igniteForSeconds(2);
        }
    }

    private static final Map<Class<?>, Double> MONSTER_IGNORE = Map.of(
        Pillager.class, 64.0, Vindicator.class, 32.0, Evoker.class, 16.0, Witch.class, 16.0,
        Zombie.class, 48.0, Husk.class, 48.0, Drowned.class, 48.0,
        Skeleton.class, 24.0, Parched.class, 24.0,
        Slime.class, 16.0
    );
    private static final Map<Class<?>, Double> MONSTER_AGGRO = Map.of(
        IronGolem.class, 16.0, SnowGolem.class, 24.0
    );
    static class IsMonster extends TickingAbility {
        IsMonster() {
            super("IS_MONSTER");
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            // Ignore
            Set<UUID> stillNearby = new HashSet<>();
            MONSTER_IGNORE.forEach((type, range) ->
                AbilityUtil.forEachNearby(player, type, range, (Mob mob) -> {
                    UUID id = mob.getUUID();
                    stillNearby.add(id);
                    if (!storedGoals.containsKey(id)) {
                        List<WrappedGoal> removed = mob.targetSelector.getAvailableGoals()
                            .stream()
                            .filter(goal -> goal.getGoal() instanceof NearestAttackableTargetGoal<?>)
                            .collect(Collectors.toList());
                        removed.forEach(goal -> mob.targetSelector.removeGoal(goal.getGoal()));
                        storedGoals.put(id, removed);
                        mob.setTarget(null);
                    }
                })
            );
            storedGoals.entrySet().removeIf(entry -> {
                if (stillNearby.contains(entry.getKey())) return false;
                Entity entity = level.getEntity(entry.getKey());
                if (entity instanceof Mob mob) {
                    entry.getValue().forEach(g -> mob.targetSelector.addGoal(g.getPriority(), g.getGoal()));
                }
                return true;
            });

            // Fear
            for (Villager villager : AbilityUtil.getNearby(player, Villager.class, 16.0)) {
                if (villager.isSleeping()) villager.stopSleeping();
                villager.getNavigation().moveTo(
                    villager.getX()+(villager.getX()-player.getX()),
                    villager.getY(),
                    villager.getZ()+(villager.getZ()-player.getZ()), 0.75);
            }

            // Attack
            MONSTER_AGGRO.forEach((type, range) ->
                AbilityUtil.forEachNearby(player, type, range, (Mob mob) -> {
                    if (mob.getTarget() != player) mob.setTarget(player);
                })
            );
        }
    }

    // CLIMBS_WALLS - LivingEntityMixin (onClimbable) + AbilityEffects (shouldClimb bool)

    private static final Map<Class<?>, MobData> PREDATORY_FEAR = Map.of(
        Chicken.class, new MobData(8.0, 1.4),
        Parrot.class, new MobData(16.0, 1.25),
        Frog.class, new MobData(12.0, 2.0),
        Salmon.class, new MobData(6.0, 1.25),
        Pig.class, new MobData(8.0, 1.25)
    );
    static class Predatory extends TickingAbility {
        Predatory() { super("PREDATORY"); }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            PREDATORY_FEAR.forEach((type, data) ->
                AbilityUtil.forEachNearby(player, type, data.range(), (Mob mob) -> {
                    mob.getNavigation().moveTo(
                        mob.getX() + (mob.getX() - player.getX()),
                        mob.getY(),
                        mob.getZ() + (mob.getZ() - player.getZ()), data.speed());
                })
            );
        }
    }

    // CARNIVORE - AbilityEffects (RIGHT_CLICK_BLOCK + RIGHT_CLICK_ITEM)
    // VEGETARIAN - AbilityEffects (RIGHT_CLICK_BLOCK + RIGHT_CLICK_ITEM)
    // ONLY_EATS_SWEETS - AbilityEffects (RIGHT_CLICK_BLOCK + RIGHT_CLICK_ITEM)
    // GRASS_EATER - AbilityEffects (RIGHT_CLICK_BLOCK)
}
