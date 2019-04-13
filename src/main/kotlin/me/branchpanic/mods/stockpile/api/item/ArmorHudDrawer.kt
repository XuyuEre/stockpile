package me.branchpanic.mods.stockpile.api.item

import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack

interface ArmorHudDrawer {
    companion object {
        fun renderHud(client: MinecraftClient) {
            if (client.player == null) {
                return
            }

            client.player.armorItems.forEach { s -> (s.item as? ArmorHudDrawer)?.drawHud(client, s) }
        }
    }

    fun drawHud(client: MinecraftClient, stack: ItemStack)
}