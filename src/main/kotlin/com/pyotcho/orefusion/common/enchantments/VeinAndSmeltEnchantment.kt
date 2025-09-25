package com.pyotcho.orefusion.common.enchantments

import com.pyotcho.orefusion.OreFusion
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


class VeinAndSmeltEnchantment {
    private var key: NamespacedKey? = null
    private var name: String? = null

    constructor(plugin: OreFusion, id: String, name: String) {
        this.key = NamespacedKey(plugin, id)
        this.name = name
    }

    fun getKey(): NamespacedKey? {
        return key
    }

    fun getName(): String? {
        return name
    }

    fun apply(item: ItemStack, level: Int) {
        val meta = item.itemMeta

        if (meta != null) {
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true)
            meta.lore = listOf(getName() + " $level")
            meta.persistentDataContainer.set(getKey()!!, PersistentDataType.INTEGER, level)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            item.setItemMeta(meta)
        } else {
            OreFusion.logger.warning("Item meta is null")
        }
    }

    fun getLevel(item: ItemStack): Int? {
        val meta = item.itemMeta
        if (meta != null && meta.persistentDataContainer.has(getKey()!!, PersistentDataType.INTEGER)) {
            return meta.persistentDataContainer.get(getKey()!!, PersistentDataType.INTEGER)
        } else {
            OreFusion.logger.warning("Item meta is null or doesn't have level")
        }
        return 0
    }
}