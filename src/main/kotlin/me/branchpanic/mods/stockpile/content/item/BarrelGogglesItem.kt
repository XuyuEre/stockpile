package me.branchpanic.mods.stockpile.content.item

import com.mojang.blaze3d.platform.GlStateManager
import me.branchpanic.mods.stockpile.Stockpile
import me.branchpanic.mods.stockpile.api.item.ArmorHudDrawer
import me.branchpanic.mods.stockpile.content.blockentity.ItemBarrelBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.resource.language.I18n
import net.minecraft.entity.EquipmentSlot
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.HitResult
import net.minecraft.world.RayTraceContext
import kotlin.math.max

object BarrelGogglesItem : ArmorItem(ArmorMaterials.IRON, EquipmentSlot.HEAD, Item.Settings()), ArmorHudDrawer {
    private data class Icon(val x: Int, val y: Int, val width: Int, val height: Int)

    private val TEXTURES = Stockpile.id("textures/gui/barrel_goggles.png")
    private val BARREL = Icon(0, 0, 8, 8)
    private val CHEST = Icon(8, 0, 8, 8)
    private val CLOSED_LOCK = Icon(0, 8, 8, 8)
    private val OPEN_LOCK = Icon(0, 16, 8, 8)
    private val INSERT = Icon(8, 8, 8, 8)
    private val EXTRACT = Icon(8, 16, 8, 8)

    override fun drawHud(client: MinecraftClient, stack: ItemStack) {
        val reach = client.interactionManager.reachDistance
        val rayTraceStart = client.player.getCameraPosVec(1f)
        val rayTraceEnd = rayTraceStart.add(client.player.getRotationVec(1f).multiply(reach.toDouble()))
        val result = client.world.rayTrace(
            RayTraceContext(
                rayTraceStart,
                rayTraceEnd,
                RayTraceContext.ShapeType.OUTLINE,
                RayTraceContext.FluidHandling.NONE,
                client.player
            )
        )

        if (result.type != HitResult.Type.BLOCK) {
            return
        }

        val blockEntity = client.world.getBlockEntity(result.blockPos)

        val x = client.window.scaledWidth / 2 + 8
        var y = client.window.scaledHeight / 2 - 4

        if (blockEntity is ItemBarrelBlockEntity) {
            y += drawInfoLine(client, client.textRenderer, BARREL, I18n.translate("ui.stockpile.goggles.item_barrel"), x, y)

            y += if (blockEntity.backingStorage.clearWhenEmpty) {
                drawInfoLine(client, client.textRenderer, OPEN_LOCK, "unlocked", x, y)
            } else {
                drawInfoLine(client, client.textRenderer, CLOSED_LOCK, "locked", x, y)
            }

        } else if (blockEntity is Inventory) {
            y += drawInfoLine(client, client.textRenderer, CHEST, I18n.translate("ui.stockpile.goggles.inventory"), x, y)
            if (blockEntity is SidedInventory) {
                if (blockEntity.canInsertInvStack(0, client.player.mainHandStack, result.side)) {
                    y += drawInfoLine(client, client.textRenderer, INSERT, "Can insert on this side", x, y)
                }

                if (blockEntity.canExtractInvStack(0, client.player.mainHandStack, result.side)) {
                    y += drawInfoLine(client, client.textRenderer, INSERT, "Can extract on this side", x, y)
                }
            }
        }
    }

    private fun drawInfoLine(
        client: MinecraftClient,
        renderer: TextRenderer,
        icon: Icon,
        text: String,
        x: Int,
        y: Int
    ): Int {
        client.textureManager.bindTexture(TEXTURES)

        GlStateManager.enableBlend()
        DrawableHelper.blit(
            x,
            y + (renderer.fontHeight / 2) - (icon.height / 2),
            icon.x.toFloat(),
            icon.y.toFloat(),
            icon.x + icon.width,
            icon.y + icon.height,
            64, 64
        )

        renderer.draw(text, x.toFloat() + icon.width + 4, y.toFloat(), 0xFFFFFF)
        return max(renderer.fontHeight, icon.height)
    }
}