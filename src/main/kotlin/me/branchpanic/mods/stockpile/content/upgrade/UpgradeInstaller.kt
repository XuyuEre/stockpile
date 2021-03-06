package me.branchpanic.mods.stockpile.content.upgrade

import me.branchpanic.mods.stockpile.api.upgrade.UpgradeApplier
import me.branchpanic.mods.stockpile.api.upgrade.UpgradeItem
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

object UpgradeInstaller : UseBlockCallback {
    override fun interact(player: PlayerEntity?, world: World?, hand: Hand?, hit: BlockHitResult?): ActionResult {
        if (player == null || world == null || hand == null || hit == null || player.isSpectator) {
            return ActionResult.PASS
        }

        val heldItem = player.getStackInHand(hand)

        val upgrade = (heldItem.item as? UpgradeItem)?.getUpgrade(heldItem) ?: return ActionResult.PASS

        val blockEntity = (world.getBlockEntity(hit.blockPos) as? UpgradeApplier) ?: return ActionResult.PASS

        if (blockEntity.appliedUpgrades.size >= blockEntity.maxUpgrades) {
            player.addChatMessage(
                TranslatableTextComponent("ui.stockpile.too_many_upgrades", blockEntity.maxUpgrades),
                true
            )

            return ActionResult.FAIL
        }

        if (!blockEntity.canApplyUpgrade(upgrade)) {
            player.addChatMessage(TranslatableTextComponent("ui.stockpile.cant_apply_upgrade"), true)
            return ActionResult.FAIL
        }

        val conflicts = upgrade.getConflictingUpgrades(blockEntity.appliedUpgrades)

        if (conflicts.isNotEmpty()) {
            player.addChatMessage(TranslatableTextComponent("ui.stockpile.upgrade_conflicts"), true)
            return ActionResult.FAIL
        }

        if (world.isClient) {
            return ActionResult.SUCCESS
        }

        blockEntity.applyUpgrade(upgrade)
        heldItem.subtractAmount(1)
        player.inventory.markDirty()

        player.addChatMessage(TranslatableTextComponent("ui.stockpile.upgrade_applied"), true)

        return ActionResult.SUCCESS
    }
}