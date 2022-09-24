package plugin.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import fr.minuskube.inv.content.SlotIterator
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import plugin.Plugin
import plugin.util.ItemBuilder
import plugin.util.ItemPresets
import java.awt.Component
import java.awt.TextComponent
import java.util.*
import kotlin.math.ceil

class PaginationGUI(var onCancel: (player: Player) -> Unit, var items: List<ClickableItem>, val page: Int = 1, private var filterItems: List<ClickableItem>): InventoryProvider {

    val PAGE_SIZE = 28;

    val pages = ceil(items.size.toDouble() / PAGE_SIZE).toInt()

    companion object {
       fun open(onCancel: (player: Player) -> Unit, player: Player, i: List<ClickableItem>, index: Int = 1, f: List<ClickableItem>) {
           player.closeInventory()
           val inventory = Plugin.builder.provider(PaginationGUI(onCancel, i, index, f)).title("§7§lBrowser").size(6, 9).build()
           inventory.open(player, index)
       }
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemPresets.LIME_PLACEHOLDER.build()))
        //fill items to be 28 at least

        val list = filterItems.ifEmpty { items }
        
        val pagination = contents.pagination()

        if(list.size < 28) pagination.page(pagination.first().page)

        pagination.setItems(*list.toTypedArray())
        pagination.setItemsPerPage(PAGE_SIZE)

        val iterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
        iterator.allowOverride(false)
        pagination.addToIterator(iterator)

        contents.set(5,4, ClickableItem.of(ItemPresets.BACK_ARROW.build()) {
            player.closeInventory()
            onCancel.invoke(player)
        })

        contents.set(0,8, ClickableItem.of(ItemBuilder(Material.SPECTRAL_ARROW).name("§c§lReset Search").build()) {
            player.closeInventory()
            open(onCancel,  player, items, 1, items)
        })

        contents.set(5,3, ClickableItem.of(ItemPresets.PREVIOUS_PAGE.copy().lore("&7Page ${pagination.page+1}/${pages}").build()) {
            open(onCancel,  player, items, pagination.previous().page, filterItems)
        })

        contents.set(5,5, ClickableItem.of(ItemPresets.NEXT_PAGE.copy().lore("&7Page ${pagination.page+1}/${pages}").build()) {
            open(onCancel,  player, items, pagination.next().page, filterItems)
        })

        contents.set(5,2, ClickableItem.of(ItemPresets.PREVIOUS_PAGE.copy().name("§a§lTo first").build()) {
            open(onCancel,  player, items, pagination.first().page, filterItems)
        })

        contents.set(5,6, ClickableItem.of(ItemPresets.NEXT_PAGE.copy().name("§a§lTo last").build()) {
            open(onCancel,  player, items, pagination.last().page, filterItems)
        })

        contents.set(5 , 8, ClickableItem.of(ItemPresets.SEARCH_SIGN.build()) {
            AnvilGUI.Builder().onClose {
                open(onCancel,  player, items, page, filterItems)
            }.onComplete { _, text ->
                val filtered = items.filter { it ->
                    return@filter it.item.type.name.lowercase().contains(text.lowercase())
                }.toList()

                open(onCancel,  player, items, pagination.first().page, filtered)
                AnvilGUI.Response.close()
            }.onClose {
                open(onCancel,  player, items, page, filterItems)
            }.text("Search").itemLeft(ItemStack(Material.PAPER)).plugin(Plugin.instance).open(player)
        })

    }

    override fun update(player: Player, contents: InventoryContents) {

    }

}