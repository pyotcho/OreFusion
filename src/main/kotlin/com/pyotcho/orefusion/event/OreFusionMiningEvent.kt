package com.pyotcho.orefusion.event

import com.pyotcho.orefusion.OreFusion
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class OreFusionMiningEvent : Listener {
    // DEFAULT ORES
    private val ores = setOf(
        Material.COAL_ORE,
        Material.REDSTONE_ORE,
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.DIAMOND_ORE,
        Material.EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.NETHER_QUARTZ_ORE
    )
    // ORES TO SMELT
    private val oresToSmelt: Map<Material, Material> = mapOf(
        Material.IRON_ORE to Material.IRON_INGOT,
        Material.GOLD_ORE to Material.GOLD_INGOT
    )

    @EventHandler
    fun onOreFusionMiningEvent(event: BlockBreakEvent) {
        val player = event.player
        val tool = player.inventory.itemInMainHand

        if (!tool.type.toString().contains("PICKAXE")) return

        val level = OreFusion.VEIN_AND_SMELT.getLevel(tool) ?: 0
        if (level == 0) return

        val config = OreFusion.instance.config
        val isVeinMinerEnabled = config.getBoolean("features.veinMineEnabled", true)
        val isSmeltingEnabled = config.getBoolean("features.autoSmeltEnabled", true)
        val maxBlocks = config.getInt("vein.maxBlocksPerCycle", 30)
        val dropIfNoSpace = config.getBoolean("drop.dropOnGroundIfNoSpace", true)

        val origin = event.block
        if (!ores.contains(origin.type)) return

        var damageToPickaxe = 0
        val toProcess: MutableList<Block> = ArrayList()
        val visitedBlocks = HashSet<Block>()
        toProcess.add(origin)
        visitedBlocks.add(origin)

        while (toProcess.isNotEmpty() && visitedBlocks.size <= maxBlocks && isVeinMinerEnabled) {
            val blockToRemove = toProcess.removeFirst()
            damageToPickaxe++

            val finalDrop = if (isSmeltingEnabled && oresToSmelt.containsKey(blockToRemove.type)) {
                ItemStack(oresToSmelt[blockToRemove.type] ?: Material.AIR, 1)
            } else {
                when (blockToRemove.type) {
                    Material.REDSTONE_ORE -> ItemStack(Material.REDSTONE)
                    Material.COAL_ORE -> ItemStack(Material.COAL)
                    Material.DIAMOND_ORE -> ItemStack(Material.DIAMOND)
                    Material.EMERALD_ORE -> ItemStack(Material.EMERALD)
                    Material.LAPIS_ORE -> ItemStack(Material.LAPIS_LAZULI)
                    Material.NETHER_QUARTZ_ORE -> ItemStack(Material.QUARTZ)
                    else -> ItemStack(blockToRemove.type)
                }
            }

            val added = player.inventory.addItem(finalDrop)
            if (added.isNotEmpty() && dropIfNoSpace) {
                player.world.dropItemNaturally(blockToRemove.location, finalDrop)
            }

            blockToRemove.type = Material.AIR

            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (dx == 0 && dy == 0 && dz == 0) continue

                        val newBlock = blockToRemove.getRelative(dx, dy, dz)
                        if (!visitedBlocks.contains(newBlock) && ores.contains(newBlock.type)) {
                            if (visitedBlocks.size < maxBlocks) { // Проверяем, чтобы не превысить лимит
                                visitedBlocks.add(newBlock)
                                toProcess.add(newBlock)
                            }
                        }
                    }
                }
            }
        }

        if (player.gameMode != GameMode.CREATIVE && damageToPickaxe > 0) {
            tool.damage(damageToPickaxe, player)
        }
    }
}