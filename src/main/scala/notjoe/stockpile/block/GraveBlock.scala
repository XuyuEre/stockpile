package notjoe.stockpile.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.{Block, BlockState, Blocks}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

object GraveBlock
    extends Block /*WithEntity*/ (
      FabricBlockSettings
        .copy(Blocks.COBBLESTONE_SLAB)
        .breakByHand(true)
        .noCollision()
        .build()) {
  override def onBroken(world: IWorld,
                        pos: BlockPos,
                        state: BlockState): Unit = {}
}
