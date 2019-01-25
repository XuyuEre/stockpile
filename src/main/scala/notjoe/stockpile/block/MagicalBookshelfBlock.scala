package notjoe.stockpile.block

import net.fabricmc.fabric.block.FabricBlockSettings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{BlockWithEntity, Blocks}
import net.minecraft.world.BlockView
import notjoe.stockpile.blockentity.MagicalBookshelfBlockEntity

object MagicalBookshelfBlock extends BlockWithEntity(FabricBlockSettings.copy(Blocks.BOOKSHELF).build()) {
  override def createBlockEntity(blockView: BlockView): BlockEntity = new MagicalBookshelfBlockEntity()
}
