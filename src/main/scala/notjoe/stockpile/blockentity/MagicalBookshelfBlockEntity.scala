package notjoe.stockpile.blockentity

import net.minecraft.block.entity.{BlockEntity, BlockEntityType}

object MagicalBookshelfBlockEntity {
  val Type: BlockEntityType[MagicalBookshelfBlockEntity] =
    BlockEntityType.Builder.create[MagicalBookshelfBlockEntity](() => new MagicalBookshelfBlockEntity).build(null)
}

class MagicalBookshelfBlockEntity() extends BlockEntity(MagicalBookshelfBlockEntity.Type) {

}
