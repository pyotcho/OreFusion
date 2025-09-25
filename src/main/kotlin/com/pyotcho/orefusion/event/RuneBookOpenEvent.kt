package com.pyotcho.orefusion.event

import com.pyotcho.orefusion.OreFusion
import com.pyotcho.orefusion.common.items.RuneBook
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent


class RuneBookOpenEvent : Listener {
    private val runeBook: RuneBook
    private val veinAndSmelt = OreFusion.VEIN_AND_SMELT

    val pickaxes: List<Material> = listOf(
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.NETHERITE_PICKAXE
    )

    constructor(runeBook: RuneBook) {
        this.runeBook = runeBook;
    }

    @EventHandler
    fun onOpenRuneBook(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)  {
            val player = event.player
            val itemStack = event.item

            if (itemStack != null && runeBook.isCustomItem(itemStack)) {
                event.setCancelled(true);

                val runeSelectInventory = Bukkit.createInventory(
                    null,
                    27,
                    "Select pickaxe to enchant"
                )

                var totalPickaxes = 0
                for (item in player.inventory.contents) {
                    if (item != null && pickaxes.contains(item.type)) {
                        if (totalPickaxes < runeSelectInventory.size) {
                            runeSelectInventory.setItem(totalPickaxes, item.clone())
                            totalPickaxes++
                        }
                    }
                }

                player.openInventory(runeSelectInventory)
            }
        }
    }

    @EventHandler
    fun onSelectPickaxe(event: InventoryClickEvent) {
        if (event.view.title != "Select pickaxe to enchant")
            return

        if (event.currentItem == null)
            return

        event.isCancelled = true;
        if (event.currentItem != null && event.currentItem!!.type in pickaxes) {
            val player = event.whoClicked as Player
            val itemStack = event.currentItem!!
            val runeBook = player.inventory.itemInMainHand

            if (player.gameMode != GameMode.CREATIVE)
                runeBook.amount -= 1

            player.inventory.removeItem(itemStack)
            veinAndSmelt.apply(itemStack, 1)
            player.inventory.addItem(itemStack)

            OreFusion.logger.info("Player ${player.name} enchanted pickaxe: ${itemStack.type}")
            player.closeInventory()
        }
    }
}