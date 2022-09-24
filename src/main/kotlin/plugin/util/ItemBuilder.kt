package plugin.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(material: Material, count: Int = 1) {

    var item: ItemStack;
    var meta: ItemMeta

    init {
        item = ItemStack(material, count)
        meta = item.itemMeta ?: ItemStack(Material.DIAMOND).itemMeta
    }

    fun name(name: String): ItemBuilder {
        meta.displayName(name.toCC())
        return this;
    }

    fun lore(vararg lore: String): ItemBuilder {
        val existing = meta.lore() ?: mutableListOf()
        existing.addAll(lore.map { it.toCC() })
        meta.lore(existing)
        return this;
    }

    fun build(): ItemStack {
        item.itemMeta = meta;
        return item;
    }

    fun copy(): ItemBuilder {
        return ItemBuilder(this.item.type, this.item.amount).name((meta.displayName() as TextComponent).content())
    }

    fun applyToMeta(run: (meta: ItemMeta) -> Unit): ItemBuilder {
        run(meta)
        return this
    }


    companion object {
        fun from(itemStack: ItemStack): ItemBuilder {
            val builder = ItemBuilder(itemStack.type, itemStack.amount)
            builder.item = itemStack
            builder.meta = itemStack.itemMeta
            return builder
        }
    }
}

fun String.toComponent(): Component {
    return Component.text(this)
}

fun String.toCC(): Component {
    return Component.text(colorize(this))
}

fun String.colorize(s: String): String {
    return this.replace("&", "§")
}
