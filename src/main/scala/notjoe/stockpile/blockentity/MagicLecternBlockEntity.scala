package notjoe.stockpile.blockentity

import net.minecraft.block.Blocks
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.util.math.BlockPos
import MagicLecternBlockEntity._

import scala.math._

object MagicLecternBlockEntity {
  val Type: BlockEntityType[MagicLecternBlockEntity] =
    BlockEntityType.Builder.create[MagicLecternBlockEntity](() => new MagicLecternBlockEntity).build(null)

  val RequiredBookshelvesInRadius = 9
}

class MagicLecternBlockEntity() extends BlockEntity(Type) {
  def canFunction: Boolean = bookshelfRingExists(pos, 2) && bookshelfRingExists(pos.up(), 2)

  def bookshelfRingExists(centerPos: BlockPos, radius: Int): Boolean = {
    val positions = for {
      x <- -radius to radius
      z <- -radius to radius
      if abs(x) == radius | abs(z) == radius
    } yield (x, z)

    positions.count { case (x, z) => world.getBlockState(centerPos.add(x, 0, z)).getBlock == Blocks.BOOKSHELF } >= RequiredBookshelvesInRadius
  }
}
