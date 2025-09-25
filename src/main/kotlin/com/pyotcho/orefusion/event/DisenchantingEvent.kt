package com.pyotcho.orefusion.event

import com.pyotcho.orefusion.OreFusion
import com.pyotcho.orefusion.common.enchantments.VeinAndSmeltEnchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareGrindstoneEvent
import org.bukkit.persistence.PersistentDataType

class DisenchantingEvent : Listener {
    @EventHandler
    fun onDisenchantingEvent(event: PrepareGrindstoneEvent) {
        val result = event.result ?: return
        val fakeEnchant = VeinAndSmeltEnchantment(OreFusion.instance, "vein_and_smelt", "Rune: VeinSmelt")

        val meta = result.itemMeta ?: return
        val data = meta.persistentDataContainer

        if (data.has(fakeEnchant.getKey()!!, PersistentDataType.INTEGER)) {
            data.remove(fakeEnchant.getKey()!!)

            meta.lore = meta.lore?.filterNot {
                it.contains(fakeEnchant.getName().toString())
            } ?: listOf()

            result.itemMeta = meta
            event.result = result
        }
    }

}