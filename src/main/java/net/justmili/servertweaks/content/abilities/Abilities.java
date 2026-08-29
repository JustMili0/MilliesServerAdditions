package net.justmili.servertweaks.content.abilities;

import net.justmili.libs.v1.utils.common.EntityUtil;
import net.justmili.libs.v1.utils.common.EntityUtil.MobData;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.justmili.servertweaks.content.abilities.core.AbilityRegistries;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.TickingAbility;
import net.justmili.servertweaks.mixin.accessors.FoxAccessor;
import net.justmili.servertweaks.registries.TagRegistry;
import net.justmili.servertweaks.util.ScalerUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.cubemob.Slime;
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

import java.util.*;
import java.util.stream.Collectors;

import static net.justmili.libs.v1.utils.common.AttributeUtil.*;

public class Abilities {
    public static void init() {
    }

    /// Extra Ability variables
    public static final Identifier AR_SLOW_SPEED = ServerTweaks.asId("slow_speed");
    public static final Identifier AR_STRONG_HP = ServerTweaks.asId("strong_health");
    public static final Identifier AR_STRONG_DAMAGE = ServerTweaks.asId("strong_damage");
    private static final Map<UUID, List<WrappedGoal>> STORED_GOALS = new HashMap<>();
    private static final List<MobData> MONSTER_IGNORE = List.of(
        new MobData(Pillager.class, 64.0, 0),
        new MobData(Vindicator.class, 32.0, 0),
        new MobData(Evoker.class, 16.0, 0),
        new MobData(Witch.class, 16.0, 0),
        new MobData(Zombie.class, 48.0, 0),
        new MobData(Husk.class, 48.0, 0),
        new MobData(Drowned.class, 48.0, 0),
        new MobData(Skeleton.class, 24.0, 0),
        new MobData(Parched.class, 24.0, 0),
        new MobData(Slime.class, 16.0, 0)
    );
    private static final List<MobData> MONSTER_FEAR = List.of(
        new MobData(Villager.class, 16.0, 0)
    );
    private static final List<MobData> MONSTER_AGGRO = List.of(
        new MobData(IronGolem.class, 16.0, 0),
        new MobData(SnowGolem.class, 24.0, 0)
    );
    private static final List<MobData> PREDATORY_FEAR = List.of(
        new MobData(Chicken.class, 8.0, 1.4),
        new MobData(Parrot.class, 12.0, 1.25),
        new MobData(Frog.class, 12.0, 2.0),
        new MobData(Salmon.class, 6.0, 1.25),
        new MobData(Pig.class, 8.0, 1.25)
    );

    public static final Ability
        FIRE_IMMUNE, LAVA_IMMUNE, HEAT_IMMUNE, FREEZE_IMMUNE, FALL_IMMUNE,
        HEAT_SENSITIVE, COLD_SENSITIVE,
        LIGHT, SWIFT, SLOW, HOPPY, DWARF, SQUISHY, MAGNETIC, TOUGH, STRONG,
        AQUATIC_GRACE, BREATHES_UNDERWATER, CANT_BREATHE_AIR, CANT_SWIM, HYDROPHOBIC,
        HUNTED_BY_FOX, HUNTED_BY_WOLF, SCARES_CREEPERS, SCARES_PHANTOMS,
        CHILD_OF_NATURE, WEAK_TO_DAMAGE, NIGHT_VISION,
        BURNS_IN_DAYLIGHT, IS_MONSTER, CLIMBS_WALLS, PEARLING,
        PREDATORY, BOVID, CARNIVORE, VEGETARIAN, SACCHARIVORE, HERBIVORE, INSECTIVORE;

    static {
        FIRE_IMMUNE = register(new FireImmune(id("fire_immune"), "Fire Immune", false));
        LAVA_IMMUNE = register(new LavaImmune(id("lava_immune"), "Lava Immune", false));
        HEAT_IMMUNE = register(new Ability(id("heat_immune"), "Heat Immune", false));
        FREEZE_IMMUNE = register(new FreezeImmune(id("freeze_immune"), "Freeze Immune", false));
        FALL_IMMUNE = register(new FallImmune(id("fall_immune"), "Fall Immune", false));
        HEAT_SENSITIVE = register(new HeatSensitive(id("heat_sensitive"), "Heat Sensitive", false));
        COLD_SENSITIVE = register(new ColdSensitive(id("cold_sensitive"), "Cold Sensitive", false));
        LIGHT = register(new Light(id("light"), "Lightweight", false));
        SWIFT = register(new Swift(id("swift"), "Swift", false));
        SLOW = register(new Slow(id("slow"), "Slow", false));
        HOPPY = register(new Hoppy(id("hoppy"), "Hoppy", false));
        DWARF = register(new Dwarf(id("dwarf"), "Dwarf", false));
        SQUISHY = register(new Ability(id("squishy"), "Squishy", false));
        MAGNETIC = register(new Magnetic(id("magnetic"), "Magnetic", false));
        TOUGH = register(new Ability(id("tough"), "Tough", false));
        STRONG = register(new Strong(id("strong"), "Strong", false));
        AQUATIC_GRACE = register(new AquaticGrace(id("aquatic_grace"), "Aquatic Grace", false));
        BREATHES_UNDERWATER = register(new BreathesUnderwater(id("breathes_underwater"), "Breathes Underwater", false));
        CANT_BREATHE_AIR = register(new CantBreatheAir(id("cant_breathe_air"), "Can't Breathe Air", false));
        CANT_SWIM = register(new Ability(id("cant_swim"), "Can't Swim", false));
        HYDROPHOBIC = register(new Hydrophobic(id("hydrophobic"), "Hydrophobic", false));
        HUNTED_BY_FOX = register(new HuntedByFox(id("hunted_by_fox"), "Hunted By Foxes", false));
        HUNTED_BY_WOLF = register(new HuntedByWolf(id("hunted_by_wolf"), "Hunted By Wolves", false));
        SCARES_CREEPERS = register(new ScaresCreepers(id("scares_creepers"), "Scares Creepers", false));
        SCARES_PHANTOMS = register(new ScaresPhantoms(id("scares_phantoms"), "Scares Phantoms", false));
        CHILD_OF_NATURE = register(new ChildOfNature(id("child_of_nature"), "Child of Nature", false));
        WEAK_TO_DAMAGE = register(new Ability(id("weak_to_damage"), "Weak to Damage", false));
        NIGHT_VISION = register(new NightVision(id("night_vision"), "Night Vision", false));
        BURNS_IN_DAYLIGHT = register(new BurnsInDaylight(id("burns_in_daylight"), "Burns In Daylight", false));
        IS_MONSTER = register(new IsMonster(id("is_monster"), "Monster", false));
        // TODO: Implement climbs_walls, should only work in survival, shouldn't work in water or when flying
        CLIMBS_WALLS = register(new Ability(id("climbs_walls"), "Climbs Walls", true));
        PEARLING = register(new Ability(id("pearling"), "Pearling", false));
        PREDATORY = register(new Predatory(id("predatory"), "Predatory", false));
        BOVID = register(new Ability(id("bovid"), "Bovid", false));
        CARNIVORE = register(new Ability(id("carnivore"), "Carnivore", false));
        VEGETARIAN = register(new Ability(id("vegetarian"), "Vegetarian", false));
        SACCHARIVORE = register(new Ability(id("saccharivore"), "Saccharivore", false));
        HERBIVORE = register(new Ability(id("herbivore"), "Herbivore", false));
        INSECTIVORE = register(new Ability(id("insectivore"), "Insectivore", false));
    }

    private static Identifier id(String id) {
        return ServerTweaks.asId(id);
    }

    private static Ability register(Ability ability) {
        AbilityRegistries.ABILITIES.put(ability.getId(), ability);
        return ability;
    }

    // Define ticking abilities
    static class FireImmune extends TickingAbility {
        FireImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isOnFire() && !level.getBlockState(player.blockPosition()).is(Blocks.FIRE) && !player.hasEffect(MobEffects.WEAKNESS)) player.extinguishFire();
        }
    }

    static class LavaImmune extends TickingAbility {
        LavaImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isOnFire() && !level.getBlockState(player.blockPosition()).is(Blocks.LAVA) && !player.hasEffect(MobEffects.WEAKNESS)) player.extinguishFire();
        }
    }

    static class FreezeImmune extends TickingAbility {
        public FreezeImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.hasEffect(MobEffects.WEAKNESS) && player.isFreezing()) player.clearFreeze();
        }
    }

    static class FallImmune extends TickingAbility {
        FallImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!player.hasEffect(MobEffects.WEAKNESS) && player.hasEffect(MobEffects.SLOW_FALLING)) player.removeEffect(MobEffects.SLOW_FALLING);
        }
    }

    static class HeatSensitive extends TickingAbility {
        HeatSensitive(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 20 != 0) return;

            if (!level.getBiome(player.blockPosition()).is(TagRegistry.HOT_BIOMES)) return;
            if (!(level.canSeeSky(player.blockPosition())
                && level.getBrightness(LightLayer.SKY, player.blockPosition()) >= 8)
                || level.isDarkOutside()
                || level.isRainingAt(player.blockPosition())
                || player.isInWater()) return;

            player.hurtServer(level, level.damageSources().onFire(), 1f);
        }
    }

    static class ColdSensitive extends TickingAbility {
        ColdSensitive(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!level.getBiome(player.blockPosition()).is(TagRegistry.COLD_BIOMES)) return;
            // Completely cancel any effects if player has full leather armor.
            // Still applies freezing overlays but stops damage when armor isn't full leather. It is an intended side effect.
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.LEATHER_HELMET)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(Items.LEATHER_LEGGINGS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS)) return;

            int targetTime = player.getTicksRequiredToFreeze() + 20;
            player.setTicksFrozen(targetTime);
            player.getEntityData().set(Entity.DATA_TICKS_FROZEN, targetTime, true);
        }
    }

    static class Light extends TickingAbility {
        Light(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (player.hasEffect(MobEffects.WEAKNESS)) return;
            if (player.getDeltaMovement().y < -0.4 && player.fallDistance > 3) EntityUtil.applyEffect(player, MobEffects.SLOW_FALLING, 60, 1);
            if (player.onGround()) player.removeEffect(MobEffects.SLOW_FALLING);
        }
    }

    static class Swift extends TickingAbility {
        Swift(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isSprinting()) EntityUtil.applyEffect(player, MobEffects.SPEED, 30, 0);
        }
    }

    static class Slow extends TickingAbility {
        Slow(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            float multiplier = AbilityProfilesUtil.has(player, AQUATIC_GRACE) && player.isInWater()? -0.16f : -0.32f;
            var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);

            addOrUpdate(speed, AR_SLOW_SPEED, multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
    }

    static class Hoppy extends TickingAbility {
        Hoppy(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            EntityUtil.applyEffect(player, MobEffects.JUMP_BOOST, 100, 0);
        }
    }

    static class Dwarf extends TickingAbility {
        Dwarf(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            var scale = ScalerUtil.getScale(player);
            if (scale != null && scale.getBaseValue() > 0.75) ScalerUtil.setScale(player, 0.75f);
            EntityUtil.applyEffect(player, MobEffects.HASTE, 100, 1);
        }
    }

    static class Magnetic extends TickingAbility {
        Magnetic(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            float r = 6f, rSq = r * r;
            var items = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(r));

            for (ItemEntity item : items) {
                if (item.distanceToSqr(player) > rSq) continue;
                item.playerTouch(player);
            }
        }
    }

    static class Strong extends TickingAbility {
        Strong(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.getGameTime() % 5 != 0) return;

            var attack = get(player, Attributes.ATTACK_DAMAGE);
            var maxHp = get(player, Attributes.MAX_HEALTH);

            addOrUpdate(attack, AR_STRONG_DAMAGE, 3, AttributeModifier.Operation.ADD_VALUE);

            // Don't apply past this point
            if (!player.gameMode.isSurvival()) return;

            float min = 40f, max = 80f;
            float targetHp = Math.clamp(max - (player.getArmorValue() * 2), min, max);
            if (targetHp % 2 != 0) targetHp += 1;

            addOrReplace(maxHp, AR_STRONG_HP, targetHp - 20, AttributeModifier.Operation.ADD_VALUE);
            if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
        }
    }

    static class AquaticGrace extends TickingAbility {
        AquaticGrace(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.isInWater()) return;

            EntityUtil.applyEffect(player, MobEffects.CONDUIT_POWER, 100, 0);

            int num = player.hasEffect(MobEffects.POISON)? 1 : 2;
            if (level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.DEPTH_STRIDER)
                .map(e -> EnchantmentHelper.getItemEnchantmentLevel(e, player.getItemBySlot(EquipmentSlot.FEET)) > num)
                .orElse(false)) return; // Return before granting Dolphin's Grace if player has depth strider to prevent OP swimming speeds

            EntityUtil.applyEffect(player, MobEffects.DOLPHINS_GRACE, 100, 0);
        }
    }

    static class BreathesUnderwater extends TickingAbility {
        BreathesUnderwater(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            if (player.isInWater()) EntityUtil.applyEffect(player, MobEffects.WATER_BREATHING, 30, 0);
        }
    }

    static class CantBreatheAir extends TickingAbility {
        CantBreatheAir(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            if (player.isInWater() || player.hasEffect(MobEffects.WATER_BREATHING)) {
                // Restore air when in water
                if (player.getAirSupply() < player.getMaxAirSupply())
                    player.setAirSupply(player.getAirSupply() + 4);
            } else {
                // Drain air on land
                player.setAirSupply(player.getAirSupply() - 1);
                if (player.getAirSupply() <= -20) {
                    player.setAirSupply(1);
                    player.hurtServer(level, level.damageSources().drown(), 1.0F);
                }
            }
        }
    }

    static class Hydrophobic extends TickingAbility {
        Hydrophobic(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            boolean inWaterBlock = player.isInWater();
            boolean inWaterCauldron = level.getBlockState(player.blockPosition()).is(Blocks.WATER_CAULDRON);
            boolean hasHelmet = !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
            boolean inWetBiome = level.getBiome(player.blockPosition()).is(TagRegistry.HYDROPHOBIC_HELMET_EXCEPTIONS);
            boolean inRain = player.isInRain() && (!hasHelmet || inWetBiome);

            boolean inWater = inWaterBlock || inRain || inWaterCauldron;

            if (inWater && level.getGameTime() % 20 == 0) player.hurtServer(level, level.damageSources().magic(), 1.0F);
        }
    }

    static class HuntedByFox extends TickingAbility {
        HuntedByFox(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 5 != 0) return;
            // TODO: Possibly turn into a goal mixin
            for (Fox fox : EntityUtil.getNearby(player, Fox.class, 12.0)) {
                var accessor = (FoxAccessor) fox;
                if (accessor.invokeTrusts(player)) continue;

                if (fox.getTarget() == null) fox.setTarget(player);
                if (fox.getTarget() == player && !accessor.invokeIsDefending()) accessor.invokeSetDefending(true);
            }
        }
    }

    static class HuntedByWolf extends TickingAbility {
        HuntedByWolf(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 5 != 0) return;
            for (Wolf wolf : EntityUtil.getNearby(player, Wolf.class, 16.0)) {
                if (wolf.isTame()) continue;
                if (wolf.getTarget() == null) wolf.setTarget(player);
            }
        }
    }

    static class ScaresCreepers extends TickingAbility {
        ScaresCreepers(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            for (Creeper creeper : EntityUtil.getNearby(player, Creeper.class, 10.0)) {
                creeper.setTarget(null);
                creeper.getNavigation().moveTo(
                    creeper.getX() + (creeper.getX() - player.getX()),
                    creeper.getY(),
                    creeper.getZ() + (creeper.getZ() - player.getZ()), 1.2);
            }
        }
    }

    static class ScaresPhantoms extends TickingAbility {
        ScaresPhantoms(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            for (Phantom phantom : EntityUtil.getNearby(player, Phantom.class, 16.0)) {
                phantom.setTarget(null);
                phantom.getNavigation().moveTo(
                    phantom.getX() + (phantom.getX() - player.getX()),
                    phantom.getY() + 8,
                    phantom.getZ() + (phantom.getZ() - player.getZ()), 1.2);
            }
        }
    }

    static class ChildOfNature extends TickingAbility {
        ChildOfNature(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 5 != 0) return;
            for (Fox fox : EntityUtil.getNearby(player, Fox.class, 12.0)) {
                var accessor = (FoxAccessor) fox;
                if (accessor.invokeTrusts(player)) continue;
                accessor.invokeAddTrustedEntity(player);
            }
            for (Wolf wolf : EntityUtil.getNearby(player, Wolf.class, 12.0)) {
                if (!wolf.isTame() && wolf.getTarget() == player) wolf.setTarget(null);
            }
        }
    }

    static class NightVision extends TickingAbility {
        NightVision(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.isDarkOutside()) EntityUtil.applyEffect(player, MobEffects.NIGHT_VISION, 320, 0);
        }
    }

    static class BurnsInDaylight extends TickingAbility {
        BurnsInDaylight(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!level.isBrightOutside() || !level.canSeeSky(player.blockPosition())) return;
            if (level.getBrightness(LightLayer.SKY, player.blockPosition()) <= 8) return;

            if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) return;
            player.igniteForSeconds(2);
        }
    }

    static class IsMonster extends TickingAbility {
        IsMonster(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            // Ignore
            Set<UUID> stillNearby = new HashSet<>();
            EntityUtil.executeForNearby(player, MONSTER_IGNORE, (mob, data) -> {
                var uuid = mob.getUUID();
                stillNearby.add(uuid);

                if (!STORED_GOALS.containsKey(uuid)) {
                    List<WrappedGoal> removed = mob.targetSelector.getAvailableGoals()
                        .stream().filter(goal -> goal.getGoal() instanceof NearestAttackableTargetGoal<?>).collect(Collectors.toList());

                    removed.forEach(goal -> mob.targetSelector.removeGoal(goal.getGoal()));
                    STORED_GOALS.put(uuid, removed);

                    mob.setTarget(null);
                }
            });
            STORED_GOALS.entrySet().removeIf(entry -> {
                if (stillNearby.contains(entry.getKey())) return false;
                var entity = level.getEntity(entry.getKey());

                if (entity instanceof Mob mob) {
                    entry.getValue().forEach(goal -> mob.targetSelector.addGoal(goal.getPriority(), goal.getGoal()));
                }

                return true;
            });

            // Fear
            EntityUtil.executeForNearby(player, MONSTER_FEAR, (mob, data) ->
                mob.getNavigation().moveTo(
                    mob.getX() + (mob.getX() - player.getX()),
                    mob.getY(),
                    mob.getZ() + (mob.getZ() - player.getZ()),
                    data.runSpeed()
                )
            );

            // Attack
            EntityUtil.executeForNearby(player, MONSTER_AGGRO, (mob, data) -> {
                    if (mob.getTarget() != player) mob.setTarget(player);
                }
            );
        }
    }

    static class Predatory extends TickingAbility {
        Predatory(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            EntityUtil.executeForNearby(player, PREDATORY_FEAR, (mob, data) ->
                mob.getNavigation().moveTo(
                    mob.getX() + (mob.getX() - player.getX()),
                    mob.getY(),
                    mob.getZ() + (mob.getZ() - player.getZ()),
                    data.runSpeed()
                )
            );
        }
    }
}
