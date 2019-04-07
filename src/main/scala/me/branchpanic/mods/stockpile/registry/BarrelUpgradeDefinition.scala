package me.branchpanic.mods.stockpile.registry

import me.branchpanic.mods.stockpile.api.barrel.BarrelUpgrade
import net.minecraft.nbt.CompoundTag

case class BarrelUpgradeDefinition(serializer: BarrelUpgrade => CompoundTag,
                                   deserializer: CompoundTag => BarrelUpgrade)
