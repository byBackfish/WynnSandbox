package plugin.commands

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.command.Command
import org.bukkit.command.CommandException
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import plugin.Plugin

class SaveItemCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if(args.isEmpty()) {
                throw CommandException("You need to specify a name for the item!")
            }
            Plugin.itemConfig.saveItem(args[0], sender.inventory.itemInMainHand)
            sender.sendMessage("§aItem saved!")
        } else {
            throw CommandException("You must be a player to use this command!")
        }
        return true
    }
}