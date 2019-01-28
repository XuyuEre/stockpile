package notjoe.stockpile.util

import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.world.BlockView
import notjoe.stockpile.blockentity.StockpileBarrelBlockEntity
import notjoe.stockpile.inventory.MassItemInventory

object BarrelGraphOperations {
  def findAllBarrelsInBank(world: BlockView,
                           source: BlockPos,
                           visited: Set[BlockPos] = Set()): Stream[MassItemInventory] = {
    val neighbors = Direction.values()
      .map(source.offset)
      .filterNot(visited.contains)
      .filter(world.getBlockEntity(_) != null)
      .filter(world.getBlockEntity(_).isInstanceOf[StockpileBarrelBlockEntity])

    neighbors
      .toStream
      .map(world.getBlockEntity(_).asInstanceOf[StockpileBarrelBlockEntity].inventory) #:::
      neighbors.toStream.flatMap(findAllBarrelsInBank(world, _, visited ++ neighbors))
  }
}
