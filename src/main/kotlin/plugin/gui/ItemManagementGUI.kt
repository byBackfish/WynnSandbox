package plugin.gui

import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTItem
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import plugin.Plugin
import plugin.item.ItemContainer
import plugin.util.ItemBuilder
import plugin.util.ItemPresets
import plugin.util.toCC
import java.util.Random

class ItemManagementGUI(var container: ItemContainer = ItemContainer(), var name: String, var nbt: NBTCompound, var itemStack: ItemStack): InventoryProvider {

    companion object {
        fun open(container: ItemContainer = ItemContainer(), player: Player, name: String, nbt: NBTCompound, itemStack: ItemStack) {
            player.closeInventory()
            container.nbt = nbt
            val inventory = Plugin.builder.provider(ItemManagementGUI(container, name, nbt, itemStack)).title("§7§aManage Item: $name").size(3, 9).build()
            inventory.open(player)
        }
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fill(ClickableItem.empty(ItemPresets.LIME_PLACEHOLDER.build()))

        contents.set(0, 0, ClickableItem.empty(itemStack.clone()))

        contents.set(2,0, ClickableItem.of(ItemPresets.BACK_ARROW.build()) {
            player.closeInventory()
        })

        contents.set(1, 2, ClickableItem.of(ItemBuilder(Material.WRITABLE_BOOK).name("§aEdit values").lore("§7Change the NBT values of this item").build()) {
            ItemChangeValueGUI.open(player, container, name, nbt, itemStack)
        })

        contents.set(1, 6, ClickableItem.of(ItemBuilder(Material.NAME_TAG).name("§bCopy Item").lore("§aThis will copy the item").build()) {
            Plugin.itemConfig.saveItem("$name#${Random().nextInt(100)}", NBTItem.convertNBTtoItem(nbt))
            player.closeInventory()
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
            player.sendMessage("§aCopied item to config")
        })


        contents.set(2, 8, ClickableItem.of(ItemBuilder(Material.TNT).name("§l§4Delete Item").lore("§cYou can't undo this").build()) {
            Plugin.itemConfig.deleteItem(name)
            player.closeInventory()
            player.playSound(player.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
            player.sendMessage("&aSuccessfully deleted item $name".toCC())
        })

        contents.set(0, 8, ClickableItem.of(ItemBuilder(Material.PAPER).name("§7§lChange internal name").build()) {
            AnvilGUI.Builder().onClose {
                open(container, player, name, nbt, itemStack)
            }.onComplete { _, text ->
                Plugin.itemConfig.deleteItem(name)
                Plugin.itemConfig.saveItem(text, NBTItem.convertNBTtoItem(nbt))
                player.closeInventory()
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                player.sendMessage("§aRenamed item to $text")
                return@onComplete AnvilGUI.Response.close()
            }.text(name).title("§7§lChange internal name").text(name).plugin(Plugin.instance).open(player)
        })

    }

    override fun update(player: Player?, contents: InventoryContents?) {

    }

}