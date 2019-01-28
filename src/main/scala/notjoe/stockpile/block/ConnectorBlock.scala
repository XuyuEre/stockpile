package notjoe.stockpile.block

import net.fabricmc.fabric.block.FabricBlockSettings
import net.minecraft.block.{Block, BlockState, Blocks}
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.util.Identifier
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.{BlockView, IWorld}
import notjoe.stockpile.block.StockpileProperties.Connection

object ConnectorBlock extends Block(FabricBlockSettings.copy(Blocks.REDSTONE_LAMP).build()) with Description {
  def connectibleBlocks: Set[Block] = Set(
    ConnectorBlock,
    StockpileBarrelBlock,
    Registry.BLOCK.get(new Identifier("stockpile", "input_barrel")),
    Registry.BLOCK.get(new Identifier("stockpile", "output_barrel")),
  )

  override def appendProperties(builder: StateFactory.Builder[Block, BlockState]): Unit = {
    builder
      .`with`(Connection.Up)
      .`with`(Connection.Down)
      .`with`(Connection.North)
      .`with`(Connection.South)
      .`with`(Connection.East)
      .`with`(Connection.West)
  }

  override def getPlacementState(context: ItemPlacementContext): BlockState = {
    Direction.values()
      .map(p => (p, context.getBlockPos.offset(p)))
      .map { case (direction, pos) => (direction, connectibleBlocks.contains(context.getWorld.getBlockState(pos).getBlock)) }
      .foldLeft(getDefaultState) { case (state, (direction, isConnected)) =>
        state.`with`(Connection.propertyForDirection(direction), boolean2Boolean(isConnected))
      }
  }

  override def getStateForNeighborUpdate(state: BlockState,
                                         direction: Direction,
                                         neighborState: BlockState,
                                         world: IWorld,
                                         pos: BlockPos,
                                         neighborPos: BlockPos): BlockState = {
    state.`with`(Connection.propertyForDirection(direction),
      boolean2Boolean(connectibleBlocks.contains(neighborState.getBlock)))
  }

  override def isSimpleFullBlock(state: BlockState, blockView: BlockView, blockPos: BlockPos): Boolean = false

  override def getOutlineShape(blockState_1: BlockState,
                               blockView_1: BlockView,
                               blockPos_1: BlockPos,
                               verticalEntityPosition_1: VerticalEntityPosition): VoxelShape = {
    Block.createCuboidShape(5, 5, 5, 11, 11, 11)
  }
}
