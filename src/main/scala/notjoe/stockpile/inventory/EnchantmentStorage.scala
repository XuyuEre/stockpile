package notjoe.stockpile.inventory

import net.minecraft.enchantment.{Enchantment, InfoEnchantment}

class EnchantmentStorage {
  def getStoredLevels(enchantment: Enchantment): Int = ???

  def add(info: InfoEnchantment): EnchantmentStorage = ???

  def remove(info: InfoEnchantment): EnchantmentStorage = ???
}
