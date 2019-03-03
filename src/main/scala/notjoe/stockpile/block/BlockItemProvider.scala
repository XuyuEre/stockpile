package notjoe.stockpile.block

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.block.BlockItem
import notjoe.stockpile.StockpileInitializer

trait BlockItemProvider extends Block {
  def createItemBlock(): BlockItem =
    new BlockItem(this,
                  new Item.Settings().itemGroup(StockpileInitializer.ItemGroup))
}
