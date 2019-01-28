package notjoe.stockpile

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.item.block.BlockItem
import net.minecraft.item.{Item, ItemGroup, ItemStack}
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import notjoe.stockpile.block.{MetaBarrel, StockpileBarrelBlock, TrashCanBlock}
import notjoe.stockpile.blockentity.{InputBarrelBlockEntity, OutputBarrelBlockEntity, StockpileBarrelBlockEntity, TrashCanBlockEntity}

object StockpileInitializer extends ModInitializer {

  val ItemGroup: ItemGroup = FabricItemGroupBuilder.build(new Identifier("stockpile", "all"),
    () => new ItemStack(StockpileBarrelBlock))

  private implicit val Blocks: Map[String, Block] = Map(
    "barrel" -> StockpileBarrelBlock,
    "input_barrel" -> new MetaBarrel(() => new InputBarrelBlockEntity),
    "output_barrel" -> new MetaBarrel(() => new OutputBarrelBlockEntity),
    "trash_can" -> TrashCanBlock
  )

  private implicit val BlockEntityTypes: Map[String, BlockEntityType[_ <: BlockEntity]] = Map(
    "barrel" -> StockpileBarrelBlockEntity.Type,
    "input_barrel" -> InputBarrelBlockEntity.Type,
    "output_barrel" -> OutputBarrelBlockEntity.Type,
    "trash_can" -> TrashCanBlockEntity.Type
  )

  private def registerAll[T](registryType: Registry[T])(implicit contents: Map[String, T]): Unit = {
    contents
      .map { case (name, o) => (new Identifier("stockpile", name), o) }
      .foreach { case (id, o) => Registry.register(registryType, id, o) }
  }

  override def onInitialize(): Unit = {
    registerAll(Registry.BLOCK)
    registerAll(Registry.BLOCK_ENTITY)
    registerAll(Registry.ITEM)(Blocks.mapValues(new BlockItem(_, new Item.Settings().itemGroup(ItemGroup))))

    StockpileTags.initializeAll()
  }
}