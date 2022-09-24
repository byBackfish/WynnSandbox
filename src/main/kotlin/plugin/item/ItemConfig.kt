package plugin.item

import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTContainer
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.nio.file.Paths

class ItemConfig {

    val path = Paths.get(Bukkit.getPluginsFolder().absolutePath, "WynnBox")
    val items = hashMapOf<String, NBTCompound>()

    init {
        if(!path.toFile().exists()) {
            path.toFile().mkdirs()
        }

        loadAllItems();
    }

    fun saveItem(name: String, item: ItemStack) {
        val file = path.resolve("$name.item").toFile()
        if(!file.exists()) file.createNewFile();
        val nbt = NBTItem.convertItemtoNBT(item)
        items[name] = nbt
        println(nbt)
        file.writeText(nbt.toString())
        println(nbt)
    }

    private fun loadItem(name: String): NBTCompound? {
        val file = path.resolve("$name.item").toFile()
        if (!file.exists()) return null
        val item = NBTContainer(file.readText())
        items[name] = item
        return item
    }

    fun deleteItem(name: String) {
        val file = path.resolve("$name.item").toFile()
        if(!file.exists()) return
        file.delete()
        items.remove(name)
    }

    fun editItem(name: String, nbt: NBTCompound) {
        val file = path.resolve("$name.item").toFile()
        if(!file.exists()) return
        file.writeText(nbt.toString())
        items[name] = nbt
    }

    fun loadAllItems() {
        path.toFile().listFiles()?.forEach {
            val name = it.nameWithoutExtension
            loadItem(name)
        }
    }

}