package plugin.gui

import de.tr7zw.nbtapi.NBTCompound
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import fr.minuskube.inv.content.SlotIterator
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import plugin.Plugin
import plugin.item.ItemContainer
import plugin.item.ItemDataTypes
import plugin.util.ItemBuilder
import plugin.util.ItemPresets


class ItemChangeValueGUI(val itemContainer: ItemContainer, var name: String, var nbt: NBTCompound, var itemStack: ItemStack): InventoryProvider {

    companion object {
        fun open(player: Player, itemContainer: ItemContainer, name: String, nbt: NBTCompound, itemStack: ItemStack) {
            val gui = Plugin.builder.provider(ItemChangeValueGUI(itemContainer, name, nbt, itemStack)).title("§a§lItem editor").size(6,9).build()
            gui.open(player)
        }
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemPresets.LIME_PLACEHOLDER.build()));

        contents.set(5, 0, ClickableItem.of(ItemPresets.BACK_ARROW.build()) {
            openOldGUI(player)
        })

        val iterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
        iterator.allowOverride(false)


        ItemDataTypes.values().forEach {
            val current: String = itemContainer.get(it).toString().replace("\"", "")

            iterator.set(ClickableItem.of(ItemBuilder(it.type).name("§a§l${it.displayName}").lore("&7&lCurrent Value: $current").build()) { event ->
                event.isCancelled = true

                AnvilGUI.Builder().onClose {
                    open(player, itemContainer, name, nbt, itemStack)
                }.onComplete { player, text ->
                    itemContainer.set(it, text)
                    nbt = itemContainer.buildNBT()
                    itemStack = itemContainer.buildItem()!!
                    open(player, itemContainer, name, nbt, itemStack)
                    player.playSound(player.location, "minecraft:block.note_block.pling", 1f, 1f)

                    return@onComplete AnvilGUI.Response.close()
                }.title("§a§lEnter a value").text(current).plugin(Plugin.instance).open(player)
            }).next()
        }

        contents.set(5,8, ClickableItem.of(ItemPresets.SUBMIT.build()) {
            Plugin.itemConfig.editItem(name, itemContainer.buildNBT())
            player.playSound(player.location, "minecraft:entity.player.levelup", 1f, 1f);
            openOldGUI(player)
        })

        updatePreview(contents)
    }

    override fun update(player: Player?, contents: InventoryContents?) {

    }

    private fun openOldGUI(player: Player) {
        ItemManagementGUI.open(itemContainer, player, name, nbt, itemStack)
    }

    fun updatePreview(contents: InventoryContents) {
        val item = itemContainer.buildItem()?.let { ItemBuilder.from(it).lore("", "§7§lThis is a preview of the item").build() }
        contents.set(5, 4, ClickableItem.empty(item))
    }

}
