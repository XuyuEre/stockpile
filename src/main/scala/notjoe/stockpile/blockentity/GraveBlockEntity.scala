package notjoe.stockpile.blockentity

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.{DefaultedList, Identifier, InventoryUtil}
import net.minecraft.world.World

object GraveBlockEntity {
  val Type: BlockEntityType[GraveBlockEntity] =
    BlockEntityType.Builder
      .create[GraveBlockEntity](() => new GraveBlockEntity())
      .build(null)

  val DoubleClickPeriodMs = 500

  def createGrave(world: World,
                  inventory: PlayerInventory,
                  pos: BlockPos): Unit = {
    world.setBlockState(
      pos,
      Registry.BLOCK.get(new Identifier("stockpile", "grave")).getDefaultState)

    val tile = world.getBlockEntity(pos).asInstanceOf[GraveBlockEntity]
    tile.inventory.addAll(inventory.armor)
    tile.inventory.addAll(inventory.main)
    tile.inventory.addAll(inventory.offHand)
  }
}

class GraveBlockEntity(
    var inventory: DefaultedList[ItemStack] = DefaultedList.create())
    extends BlockEntity(GraveBlockEntity.Type)
    with BlockEntityPersistence {

  override def saveToTag(): CompoundTag =
    InventoryUtil.serialize(new CompoundTag, inventory)

  override def loadFromTag(tag: CompoundTag): Unit =
    InventoryUtil.deserialize(tag, inventory)
}
