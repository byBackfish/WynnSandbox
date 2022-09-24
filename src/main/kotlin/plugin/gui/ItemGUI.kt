package plugin.gui

import de.tr7zw.nbtapi.NBTItem
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import plugin.Plugin
import plugin.item.ItemContainer
import plugin.util.ItemBuilder
import plugin.util.ItemPresets

class ItemGUI: InventoryProvider {


   companion object {
       val inventory: SmartInventory = Plugin.builder.provider(ItemGUI()).size(3, 9).title("Item Management").build()
   }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fill(ClickableItem.empty(ItemPresets.LIME_PLACEHOLDER.build()))
        contents.set(2,8, ClickableItem.of(ItemPresets.CLOSE_BARRIER.build()) { player.closeInventory() })

        contents.set(1, 2, ClickableItem.of(ItemBuilder(Material.NAME_TAG).name("&a&lManage Items").build()) {
            val items = Plugin.itemConfig.items.entries.map { (key, value) ->
                val item =    ItemBuilder.from(NBTItem.convertNBTtoItem(value))
                    .lore("", "&7&lID: $key").build()

                ClickableItem.of(item) {
                    ItemManagementGUI.open(
                        ItemContainer(),
                        player,
                        key,
                        value,
                        item,
                    )
                }
            }

            PaginationGUI.open({ player.closeInventory() }, player, items, 1, listOf())
        })

        contents.set(1, 6, ClickableItem.of(ItemBuilder(Material.GHAST_TEAR).name("&7&lBrowse Items").build()) {
            val items = Plugin.itemConfig.items.entries.map { (key, value) ->
                val item = NBTItem.convertNBTtoItem(value)
                val showed = ItemBuilder.from(item.clone()).lore("", "&7&lID: $key").build()
                ClickableItem.of(showed) {
                    it.isCancelled = true
                    player.playSound(player.location, "minecraft:block.note_block.pling", 1f, 1f)
                    player.inventory.addItem(item)
                }
            }

            PaginationGUI.open({ inventory.open(it) }, player, items, 1, listOf())
        })


    }

    override fun update(player: Player?, contents: InventoryContents?) {

    }


}