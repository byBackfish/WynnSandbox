package plugin.commands

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.command.Command
import org.bukkit.command.CommandException
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import plugin.Plugin

class GetCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            var nbtItem = Plugin.itemConfig.items.get(args?.get(0))
            if (nbtItem != null) {
                sender.inventory.addItem(NBTItem.convertNBTtoItem(nbtItem))
            } else {
                sender.sendMessage("Item not found")
            }
            sender.sendMessage("Saved item")
        } else {
            throw CommandException("You must be a player to use this command!")
        }
        return true
    }
}