package plugin.item

import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTContainer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import plugin.util.toCC

enum class ItemDataTypes(val displayName: String, val type: Material, val setter: (nbt: NBTCompound, values: Any) -> Unit, val getter: (nbt: NBTCompound) -> String) {

    ID("ID", Material.STONE,
        { nbt, values ->
            kotlin.runCatching {
                Material.valueOf(values.toString().toUpperCase())
            }.onSuccess {
                nbt.setString("id", "minecraft:${values.toString().toLowerCase()}")
            }.onFailure {
                nbt.setString("id", "minecraft:stone")
            }
        },
        { nbt -> nbt.getString("id").split(":")[1] }
    ),
    NAME("Display Name", Material.NAME_TAG,
        { nbt, values ->
            nbt.get("tag.display").setObject("Name", values.toString().replace("&", "§"))
        },
        { it.get("tag.display").getString("Name") }
    ),
    LORE("Lore", Material.PAPER,
        { nbt, values ->
            val list = nbt.get("tag.display").getStringList("Lore")
            list.clear()
            list.addAll(values.toString().replace("&", "§").split("\n"))
        },
        { it ->
            it.get("tag.display").getStringList("Lore").joinToString("\n") { it.replace("§", "&") }
        }
    ),
    DAMAGE("Damage", Material.ANVIL,
        { nbt, values ->
            nbt.get("tag").setBoolean("Unbreakable", true)
            nbt.get("tag").setShort("Damage", values.toString().toShort())

        },
        {
            it.get("tag").getShort("Damage").toString()
        }
    ),
    HIDE_FLAGS("Hide Flags (int)", Material.BRICK_STAIRS,
    { nbt, values ->
        nbt.get("tag").setInteger("HideFlags", values.toString().toInt())
    },
    {
        it.get("tag").getInteger("HideFlags").toString()
    }
    )
}

fun NBTCompound.get(key: String): NBTCompound {
    var nbt = this
    key.split(".").forEach {
        nbt = nbt.getOrCreateCompound(it)
    }
    return nbt
}
