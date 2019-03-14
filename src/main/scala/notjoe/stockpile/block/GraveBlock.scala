package notjoe.stockpile.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{
  BlockRenderType,
  BlockState,
  BlockWithEntity,
  Blocks
}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{BlockView, World}
import notjoe.stockpile.blockentity.GraveBlockEntity

object GraveBlock
    extends BlockWithEntity(
      FabricBlockSettings
        .copy(Blocks.BEDROCK)
        .noCollision()
        .build()) {
  override def createBlockEntity(blockView: BlockView): BlockEntity =
    new GraveBlockEntity()

  override def getRenderType(state: BlockState): BlockRenderType =
    BlockRenderType.MODEL

  override def activate(state: BlockState,
                        world: World,
                        pos: BlockPos,
                        player: PlayerEntity,
                        hand: Hand,
                        result: BlockHitResult): Boolean = {
    if (world.isClient) {
      return true
    }

    val graveBlockEntity =
      world.getBlockEntity(pos).asInstanceOf[GraveBlockEntity]

    if (graveBlockEntity.isRestorableBy(player)) {
      graveBlockEntity.restoreToPlayer(player)
    }

    true
  }
}
