package com.pyotcho.orefusion.event

import com.pyotcho.orefusion.OreFusion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent

class LootPlaceEvent : Listener {
    private val lootTable = setOf(
        "minecraft:chests/nether_bridge",
        "minecraft:chests/nether_bridge",
        "chests/simple_dungeon",
        "minecraft:chests/simple_dungeon"
    )

    @EventHandler
    fun generateLoot(event: LootGenerateEvent) {
        val eventLootTable = event.lootTable.key.toString()

        if (!lootTable.any {
            eventLootTable.contains(it)
        }) return

        val config = OreFusion.instance.config
        val lootSpawnChance = config.getDouble("features.lootSpawnChance", 0.05)

        if (Math.random() <= lootSpawnChance) {
            event.inventoryHolder?.inventory?.addItem(OreFusion.runeBook.getItem()!!)
        }
    }
}