package notjoe.stockpile.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.{ItemStack, Items}
import net.minecraft.util.math.Direction
import notjoe.stockpile.blockentity.AutoPersistence.PersistentField
import notjoe.stockpile.inventory.EnchantedBookStorage._

object EnchantedBookStorage {
  val InputSlotIndex = 0
  val BookSlotIndex = 1
  val OutputSlotIndex = 2
}

class EnchantedBookStorage(@PersistentField var inputStack: ItemStack = ItemStack.EMPTY,
                           @PersistentField var bookStack: ItemStack = ItemStack.EMPTY,
                           @PersistentField var outputStack: ItemStack = ItemStack.EMPTY,
                           val onChanged: () => Unit = () => {}) extends SidedInventory {

  private def inventoryAsList: List[ItemStack] = List(inputStack, bookStack, outputStack)

  override def getInvAvailableSlots(direction: Direction): Array[Int] = Array(InputSlotIndex, BookSlotIndex)

  override def canInsertInvStack(i: Int, itemStack: ItemStack, direction: Direction): Boolean = i match {
    case InputSlotIndex => itemStack.getItem == Items.ENCHANTED_BOOK
    case BookSlotIndex => itemStack.getItem == Items.BOOK
    case _ => false
  }

  override def canExtractInvStack(i: Int, itemStack: ItemStack, direction: Direction): Boolean = ???

  override def getInvSize: Int = 3

  override def isInvEmpty: Boolean = inputStack.isEmpty && bookStack.isEmpty && outputStack.isEmpty

  override def getInvStack(i: Int): ItemStack = inventoryAsList.lift(i).getOrElse(ItemStack.EMPTY)

  override def takeInvStack(i: Int, i1: Int): ItemStack = ???

  override def removeInvStack(i: Int): ItemStack = ???

  override def setInvStack(i: Int, itemStack: ItemStack): Unit = i match {
    case InputSlotIndex => inputStack = itemStack
    case BookSlotIndex => inputStack = itemStack
    case OutputSlotIndex => outputStack = itemStack
  }

  override def markDirty(): Unit = onChanged()

  override def canPlayerUseInv(playerEntity: PlayerEntity): Boolean = true

  override def clearInv(): Unit = {
    inputStack = ItemStack.EMPTY
    bookStack = ItemStack.EMPTY
    outputStack = ItemStack.EMPTY
  }
}
