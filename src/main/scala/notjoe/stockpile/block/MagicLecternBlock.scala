package notjoe.stockpile.block

import net.fabricmc.fabric.block.FabricBlockSettings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{BlockState, BlockWithEntity, Blocks}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{BlockView, World}
import notjoe.stockpile.blockentity.MagicLecternBlockEntity

object MagicLecternBlock extends BlockWithEntity(FabricBlockSettings.copy(Blocks.LECTERN).build()) with Description {
  override def createBlockEntity(blockView: BlockView): BlockEntity = new MagicLecternBlockEntity()

  override def activate(state: BlockState,
                        world: World,
                        pos: BlockPos,
                        player: PlayerEntity,
                        hand: Hand,
                        hitResult: BlockHitResult): Boolean = {
    if (!world.isClient) {
      return true
    }

    val tile = world.getBlockEntity(pos).asInstanceOf[MagicLecternBlockEntity]

    if (!tile.canFunction) {
      player.addChatMessage(new TranslatableTextComponent("stockpile.magic_lectern.needs_structure"), true)
    } else {
      // NO-OP for now
    }

    true
  }
}
