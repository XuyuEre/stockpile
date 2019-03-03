package notjoe.stockpile.block

import java.util

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.{BlockState, BlockWithEntity, Blocks}
import net.minecraft.item.ItemStack
import net.minecraft.world.BlockView
import net.minecraft.world.loot.context.{LootContext, LootContextParameters}
import notjoe.stockpile.blockentity.GraveBlockEntity

object GraveBlock
    extends BlockWithEntity(
      FabricBlockSettings
        .copy(Blocks.COBBLESTONE_SLAB)
        .breakByHand(true)
        .noCollision()
        .build()) {
  override def createBlockEntity(blockView: BlockView): BlockEntity =
    new GraveBlockEntity()

  override def getDroppedStacks(
      state: BlockState,
      builder: LootContext.Builder): util.List[ItemStack] =
    builder
      .get(LootContextParameters.BLOCK_ENTITY)
      .asInstanceOf[GraveBlockEntity]
      .inventory
}
