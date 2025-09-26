package com.pyotcho.orefusion.common.items

import com.pyotcho.orefusion.OreFusion
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


class RuneBook {
    private var key: NamespacedKey? = null
    private var itemStack: ItemStack? = null
    private var keyName: String? = null

    constructor(plugin: OreFusion, id: String, keyName: String, name: String, material: Material) {
        this.key = NamespacedKey(plugin, id)
        this.itemStack = ItemStack(material)
        this.keyName = keyName

        var itemMeta = itemStack!!.itemMeta

        if (itemMeta != null) {
            itemMeta.setDisplayName(name)
            itemMeta.lore = listOf("Ancient Rune infused your pickaxe with God's power")
            itemMeta.persistentDataContainer.set(key!!, PersistentDataType.BYTE, 1)
            itemStack!!.itemMeta = itemMeta
        }
    }

    fun getItem(): ItemStack? {
        return itemStack?.clone()
    }

    fun getKeyName(): String? {
        return keyName
    }

    fun isCustomItem(item: ItemStack): Boolean {
        val meta = item.itemMeta
        return meta != null && meta.persistentDataContainer.has(key!!, PersistentDataType.BYTE)
    }
}