package notjoe.stockpile.blockentity

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import notjoe.stockpile.inventory.MassItemInventory
import notjoe.stockpile.util.BarrelGraphOperations._

object InputBarrelBlockEntity {
  val Type: BlockEntityType[InputBarrelBlockEntity] =
    BlockEntityType.Builder.create[InputBarrelBlockEntity](() => new InputBarrelBlockEntity).build(null)
}

class InputBarrelBlockEntity extends BlockEntity(InputBarrelBlockEntity.Type) with SidedInventory {
  override def getInvAvailableSlots(direction: Direction): Array[Int] = Array(MassItemInventory.InputSlotIndex)

  override def canInsertInvStack(i: Int, itemStack: ItemStack, direction: Direction): Boolean =
    findAllBarrelsInBank(world, pos).exists(_.canInsertInvStack(i, itemStack, direction))

  override def canExtractInvStack(i: Int, itemStack: ItemStack, direction: Direction): Boolean = false

  override def getInvSize: Int = 1

  override def isInvEmpty: Boolean = false

  override def getInvStack(i: Int): ItemStack = ItemStack.EMPTY

  override def takeInvStack(i: Int, i1: Int): ItemStack = ItemStack.EMPTY

  override def removeInvStack(i: Int): ItemStack = ItemStack.EMPTY

  override def setInvStack(i: Int, itemStack: ItemStack): Unit = {
    val matchingInventory = findAllBarrelsInBank(world, pos).find(_.isValidInvStack(i, itemStack))
    if (matchingInventory.isDefined) {
      matchingInventory.get.setInvStack(i, itemStack)
    }
  }

  override def isValidInvStack(i: Int, itemStack: ItemStack): Boolean = {
    findAllBarrelsInBank(world, pos).exists(_.isValidInvStack(i, itemStack))
  }

  override def canPlayerUseInv(playerEntity: PlayerEntity): Boolean = true

  override def clearInv(): Unit = ()
}
