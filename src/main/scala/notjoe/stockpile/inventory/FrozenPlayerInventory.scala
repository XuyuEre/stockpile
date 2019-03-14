package notjoe.stockpile.inventory

import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.{DefaultedList, InventoryUtil}
import notjoe.stockpile.blockentity.Persistence

import scala.collection.JavaConverters._

object FrozenPlayerInventory {
  def fromInventory(inventory: PlayerInventory): FrozenPlayerInventory =
    new FrozenPlayerInventory(inventory.armor,
                              inventory.main,
                              inventory.offHand)

  def empty(): FrozenPlayerInventory =
    new FrozenPlayerInventory(DefaultedList.create(),
                              DefaultedList.create(),
                              DefaultedList.create())
}

class FrozenPlayerInventory(var armor: DefaultedList[ItemStack],
                            var main: DefaultedList[ItemStack],
                            var offHand: DefaultedList[ItemStack])
    extends Persistence {

  private def mergeInventoryLists(
      from: List[ItemStack],
      to: DefaultedList[ItemStack]): List[ItemStack] = {

    val (insertable, leftover) = from.zipWithIndex.partition {
      case (_, i) => to.get(i).isEmpty
    }

    insertable.foreach {
      case (stack, i) =>
        to.set(i, stack.copy())
    }

    leftover.map(_._1)
  }

  def restoreToPlayer(player: PlayerEntity): List[ItemStack] = {
    val outOfPlace =
      mergeInventoryLists(armor.asScala.toList, player.inventory.armor) ++
        mergeInventoryLists(main.asScala.toList, player.inventory.main) ++
        mergeInventoryLists(offHand.asScala.toList, player.inventory.offHand)

    val remainder = outOfPlace
      .map { s =>
        val c = s.copy()
        player.inventory.insertStack(c)
        c
      }
      .filterNot(_.isEmpty)

    player.inventory.markDirty()

    remainder
  }

  override def saveToTag(): CompoundTag = {
    val tag = new CompoundTag
    tag.put("armor", InventoryUtil.serialize(new CompoundTag, armor))
    tag.put("main", InventoryUtil.serialize(new CompoundTag, main))
    tag.put("offHand", InventoryUtil.serialize(new CompoundTag, offHand))
    tag
  }

  override def loadFromTag(tag: CompoundTag): Unit = {
    InventoryUtil.deserialize(tag.getCompound("armor"), armor)
    InventoryUtil.deserialize(tag.getCompound("main"), main)
    InventoryUtil.deserialize(tag.getCompound("offHand"), offHand)
  }
}
