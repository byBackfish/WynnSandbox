package plugin.messages

import org.bukkit.entity.Player

class Message(val player: Player, val type: MessageType) {


}

enum class MessageType() {
    CHAT,
    TITLE,
    ACTIONBAR,
    BOSSBAR,
}