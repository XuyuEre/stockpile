package notjoe.stockpile.block

import net.fabricmc.fabric.block.FabricBlockSettings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{BlockRenderType, BlockState, BlockWithEntity}
import net.minecraft.world.BlockView

class MetaBarrel(val blockEntitySupplier: () => BlockEntity)
  extends BlockWithEntity(FabricBlockSettings.copy(StockpileBarrelBlock).build())
    with FacingDirection
    with Description {
  override def createBlockEntity(blockView: BlockView): BlockEntity = blockEntitySupplier()

  override def getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL
}
