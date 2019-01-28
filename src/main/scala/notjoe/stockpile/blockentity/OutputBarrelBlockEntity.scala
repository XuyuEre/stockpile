package notjoe.stockpile.blockentity

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import notjoe.stockpile.inventory.MassItemInventory
import notjoe.stockpile.util.BarrelGraphOperations._

object OutputBarrelBlockEntity {
  val Type: BlockEntityType[OutputBarrelBlockEntity] =
    BlockEntityType.Builder.create[OutputBarrelBlockEntity](() => new OutputBarrelBlockEntity).build(null)
}

class OutputBarrelBlockEntity extends BlockEntity(OutputBarrelBlockEntity.Type) with SidedInventory {
  override def getInvAvailableSlots(direction: Direction): Array[Int] = (0 until getInvSize).toArray

  override def canInsertInvStack(i: Int, itemStack: ItemStack, direction: Direction): Boolean = false

  override def canExtractInvStack(i: Int, itemStack: ItemStack, direction: Direction): Boolean =
    findAllBarrelsInBank(world, pos)
      .lift(i)
      .exists(_.canExtractInvStack(MassItemInventory.OutputSlotIndex, itemStack, direction))

  override def getInvSize: Int = findAllBarrelsInBank(world, pos).size

  override def isInvEmpty: Boolean = findAllBarrelsInBank(world, pos).forall(_.isInvEmpty)

  override def getInvStack(i: Int): ItemStack =
    findAllBarrelsInBank(world, pos)
      .lift(i)
      .map(_.getInvStack(MassItemInventory.OutputSlotIndex))
      .getOrElse(ItemStack.EMPTY)

  override def takeInvStack(i: Int, i1: Int): ItemStack =
    findAllBarrelsInBank(world, pos)
      .lift(i)
      .map(_.takeInvStack(MassItemInventory.OutputSlotIndex, i1))
      .getOrElse(ItemStack.EMPTY)

  override def removeInvStack(i: Int): ItemStack =
    findAllBarrelsInBank(world, pos)
      .lift(i)
      .map(_.removeInvStack(MassItemInventory.OutputSlotIndex))
      .getOrElse(ItemStack.EMPTY)

  override def setInvStack(i: Int, itemStack: ItemStack): Unit = ()

  override def canPlayerUseInv(playerEntity: PlayerEntity): Boolean = true

  override def clearInv(): Unit = ()
}
