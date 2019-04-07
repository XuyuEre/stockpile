package me.branchpanic.mods.stockpile.api.barrel;

import me.branchpanic.mods.stockpile.StockpileInitializer;
import me.branchpanic.mods.stockpile.registry.BarrelUpgradeDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import scala.Function1;

/**
 * A BarrelUpgrade changes the behavior of a Barrel in some way.
 */
public interface BarrelUpgrade {
    /**
     * Registers a new Barrel upgrade. This should be done during mod initialization.
     *
     * @param name         Name of the upgrade, used in deserialization.
     * @param serializer   Function used for serializing a BarrelUpgrade to a CompoundTag.
     * @param deserializer Function used for deserializing a CompoundTag from a BarrelUpgrade.
     */
    static void register(Identifier name, Function1<BarrelUpgrade, CompoundTag> serializer, Function1<CompoundTag, BarrelUpgrade> deserializer) {
        StockpileInitializer.BARREL_UPGRADES().add(name, new BarrelUpgradeDefinition(serializer, deserializer));
    }

    /**
     * Loads a registered Barrel upgrade from a given CompoundTag.
     *
     * @param tag Tag containing the serialized Barrel upgrade.
     * @return Barrel upgrade loaded from the given tag.
     */
    static BarrelUpgrade loadRegistered(CompoundTag tag) {
        return StockpileInitializer.BARREL_UPGRADES()
                .get(new Identifier(tag.getString("Name")))
                .deserializer()
                .apply(tag);
    }

    /**
     * Determines whether or not this Barrel upgrade conflicts with another. If true, then both upgrades will not be
     * installable in the same barrel.
     *
     * @param other Barrel upgrade to test conflict with.
     * @return False if the upgrades cannot coexist, otherwise true.
     */
    default boolean conflictsWith(BarrelUpgrade other) {
        return false;
    }

    /**
     * Upgrades the maximum capacity of this barrel.
     *
     * @param currentMaxStacks The current capacity of a barrel, in stacks.
     * @return The capacity of the barrel after this upgrade has been applied, in stacks.
     */
    default int upgradeMaxStacks(int currentMaxStacks) {
        return currentMaxStacks;
    }
}
