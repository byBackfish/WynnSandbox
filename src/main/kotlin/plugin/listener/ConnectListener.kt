package plugin.listener

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import plugin.Plugin
import plugin.gui.ItemGUI
import java.util.Random

class ConnectListener: org.bukkit.event.Listener {


    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.sendMessage("Hello World")
    }

    @EventHandler
    fun onOffhand(event: PlayerSwapHandItemsEvent) {
        event.isCancelled = true;
        ItemGUI.inventory.open(event.player)
    }

}