package com.pyotcho.orefusion

import com.pyotcho.orefusion.common.commands.RuneCommand
import com.pyotcho.orefusion.common.enchantments.VeinAndSmeltEnchantment
import com.pyotcho.orefusion.common.items.RuneBook
import com.pyotcho.orefusion.event.DisenchantingEvent
import com.pyotcho.orefusion.event.LootPlaceEvent
import com.pyotcho.orefusion.event.OreFusionMiningEvent
import com.pyotcho.orefusion.event.RuneBookOpenEvent
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger


class OreFusion : JavaPlugin() {

    companion object {
        lateinit var instance: OreFusion
        lateinit var VEIN_AND_SMELT: VeinAndSmeltEnchantment
        lateinit var runeBook: RuneBook
        lateinit var logger: Logger
    }

    override fun onEnable() {
        instance = this
        VEIN_AND_SMELT = VeinAndSmeltEnchantment(this, "vein_and_smelt", "Rune: VeinSmelt")
        runeBook = RuneBook(this, "vein_smelt", "vein_smelt","Rune: VeinSmelt", Material.ENCHANTED_BOOK)
        Companion.logger = this.logger

        saveDefaultConfig()

        registerEvents()
        registerCommands()
    }

    override fun onDisable() {

    }

    fun registerEvents() {
        this.server.pluginManager.registerEvents(OreFusionMiningEvent(), this)
        this.server.pluginManager.registerEvents(LootPlaceEvent(), this)
        this.server.pluginManager.registerEvents(RuneBookOpenEvent(runeBook), this)
        this.server.pluginManager.registerEvents(DisenchantingEvent(), this)
    }

    fun registerCommands() {
        this.getCommand("rune")?.setExecutor(RuneCommand())
    }
}
