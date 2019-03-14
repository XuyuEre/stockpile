package notjoe.stockpile.blockentity

import java.util.UUID

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import notjoe.stockpile.inventory.FrozenPlayerInventory

object GraveBlockEntity {
  val Type: BlockEntityType[GraveBlockEntity] =
    BlockEntityType.Builder
      .create[GraveBlockEntity](() => new GraveBlockEntity())
      .build(null)

  val DoubleClickPeriodMs = 500

  def createGrave(world: World, player: PlayerEntity, pos: BlockPos): Unit = {
    world.setBlockState(
      pos,
      Registry.BLOCK.get(new Identifier("stockpile", "grave")).getDefaultState)

    val tile = world.getBlockEntity(pos).asInstanceOf[GraveBlockEntity]
    tile.assignOwner(player)
  }
}

class GraveBlockEntity(var inventory: FrozenPlayerInventory =
                         FrozenPlayerInventory.empty(),
                       var owner: UUID = new UUID(0L, 0L))
    extends BlockEntity(GraveBlockEntity.Type)
    with BlockEntityPersistence {

  def assignOwner(player: PlayerEntity): Unit = {
    player.inventory.main.forEach(println)
    inventory = FrozenPlayerInventory.fromInventory(player.inventory)
    owner = player.getUuid
    markDirty()
  }

  def isRestorableBy(player: PlayerEntity): Boolean = {
    player.getUuid.equals(owner)
  }

  def restoreToPlayer(player: PlayerEntity): Unit = {
    println("Restoring")
    val leftover = inventory.restoreToPlayer(player)
    leftover.foreach(s =>
      world.spawnEntity(new ItemEntity(world, player.x, player.y, player.z, s)))
  }

  override def saveToTag(): CompoundTag = {
    val tag = new CompoundTag
    tag.putUuid("owner", owner)
    tag.put("inventory", inventory.saveToTag())
    tag
  }

  override def loadFromTag(tag: CompoundTag): Unit = {
    owner = tag.getUuid("owner")
    inventory.loadFromTag(tag.getCompound("inventory"))
  }
}
