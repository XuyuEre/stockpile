package notjoe.stockpile.block

import net.fabricmc.fabric.block.FabricBlockSettings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{BlockRenderType, BlockState, BlockWithEntity}
import net.minecraft.world.BlockView
import notjoe.stockpile.blockentity.InputBarrelBlockEntity

object InputBarrelBlock extends BlockWithEntity(FabricBlockSettings.copy(StockpileBarrelBlock).build())
  with FacingDirection
  with Description {
  override def createBlockEntity(blockView: BlockView): BlockEntity = new InputBarrelBlockEntity()

  override def getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL
}
