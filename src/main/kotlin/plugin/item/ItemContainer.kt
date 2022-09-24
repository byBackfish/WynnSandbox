package plugin.item

import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTContainer
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.inventory.ItemStack

class ItemContainer(var nbt: NBTCompound = defaultCompound()) {

    companion object {
        private fun defaultCompound(): NBTContainer {
            val nbt = NBTContainer()

            nbt.setShort("Count", 1)
            nbt.setString("id", "minecraft:stone")

            return nbt
        }
    }

    private val values: MutableMap<ItemDataTypes, Any> = mutableMapOf(
        ItemDataTypes.HIDE_FLAGS to 255,
    )

    fun set(type: ItemDataTypes, value: Any) {
        values[type] = value
    }

    fun get(type: ItemDataTypes, def: Any? = null): Any? {
        return values[type] ?: type.getter(nbt) ?: def
    }

    fun buildNBT(): NBTCompound {
        values.forEach {(key, value) ->
            key.setter.invoke(nbt, value)
        }
        return nbt
    }

    fun buildItem(): ItemStack? {
        val nbt = buildNBT()
        return NBTItem.convertNBTtoItem(nbt)
    }
}