package notjoe.stockpile.block

import net.fabricmc.fabric.block.FabricBlockSettings
import net.minecraft.block.{Block, BlockState, Blocks}
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.{BlockPos, Direction}
import net.minecraft.util.shape.{VoxelShape, VoxelShapes}
import net.minecraft.world.{BlockView, IWorld}
import notjoe.stockpile.StockpileTags
import notjoe.stockpile.block.StockpileProperties.Connection

object ConnectorBlock extends Block(FabricBlockSettings.copy(Blocks.REDSTONE_LAMP).build()) with Description {
  val ConnectionShapes: Map[BooleanProperty, VoxelShape] = Map(
    Connection.North -> Block.createCuboidShape(5, 5, 0, 11, 11, 5),
    Connection.South -> Block.createCuboidShape(5, 5, 11, 11, 11, 16),
    Connection.East -> Block.createCuboidShape(11, 5, 5, 16, 11, 11),
    Connection.West -> Block.createCuboidShape(0, 5, 5, 5, 11, 11),
    Connection.Up -> Block.createCuboidShape(5, 11, 5, 11, 16, 11),
    Connection.Down -> Block.createCuboidShape(5, 0, 5, 11, 5, 11),
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
      .map { case (direction, pos) => (direction, StockpileTags.barrelConnectible.contains(context.getWorld.getBlockState(pos).getBlock)) }
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
      boolean2Boolean(StockpileTags.barrelConnectible.contains(neighborState.getBlock)))
  }

  override def isSimpleFullBlock(state: BlockState, blockView: BlockView, blockPos: BlockPos): Boolean = false

  override def getOutlineShape(state: BlockState,
                               blockView_1: BlockView,
                               blockPos_1: BlockPos,
                               verticalEntityPosition_1: VerticalEntityPosition): VoxelShape = {
    ConnectionShapes
      .filter { case (property, _) => state.get(property) }
      .values
      .fold(Block.createCuboidShape(5, 5, 5, 11, 11, 11))(VoxelShapes.union)
  }
}
