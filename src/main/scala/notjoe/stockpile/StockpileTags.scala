package notjoe.stockpile

import net.fabricmc.fabric.tags.TagRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object StockpileTags {
  def barrelStorageUpgrade: Tag[Item] = TagRegistry.item(new Identifier("stockpile", "barrel_storage_upgrade"))
  def barrelConnectible: Tag[Block] = TagRegistry.block(new Identifier("stockpile", "barrel_connectible"))
}
