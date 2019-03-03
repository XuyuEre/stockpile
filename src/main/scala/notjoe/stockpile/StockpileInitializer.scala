package notjoe.stockpile

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.item.{ItemGroup, ItemStack}
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import notjoe.stockpile.block.{
  BlockItemProvider,
  GraveBlock,
  StockpileBarrelBlock,
  TrashCanBlock
}
import notjoe.stockpile.blockentity.{
  GraveBlockEntity,
  StockpileBarrelBlockEntity,
  TrashCanBlockEntity
}

object StockpileInitializer extends ModInitializer {

  val ItemGroup: ItemGroup = FabricItemGroupBuilder.build(
    new Identifier("stockpile", "all"),
    () => new ItemStack(StockpileBarrelBlock))

  private implicit val Blocks: Map[String, Block] = Map(
    "barrel" -> StockpileBarrelBlock,
    "trash_can" -> TrashCanBlock,
    "grave" -> GraveBlock
  )

  private implicit val BlockEntityTypes
    : Map[String, BlockEntityType[_ <: BlockEntity]] = Map(
    "barrel" -> StockpileBarrelBlockEntity.Type,
    "trash_can" -> TrashCanBlockEntity.Type,
    "grave" -> GraveBlockEntity.Type
  )

  private def registerAll[T](registryType: Registry[T])(
      implicit contents: Map[String, T]): Unit = {
    contents
      .map { case (name, o) => (new Identifier("stockpile", name), o) }
      .foreach { case (id, o) => Registry.register(registryType, id, o) }
  }

  override def onInitialize(): Unit = {
    registerAll(Registry.BLOCK)
    registerAll(Registry.BLOCK_ENTITY)
    registerAll(Registry.ITEM)(
      Blocks
        .filter { case (_, block) => block.isInstanceOf[BlockItemProvider] }
        .mapValues(_.asInstanceOf[BlockItemProvider].createItemBlock()))

    StockpileTags.initializeAll()
  }
}
