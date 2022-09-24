package plugin.messages

import org.bukkit.entity.Player
import java.util.UUID

class MessageHandler {

    val messages = mutableMapOf<String, ArrayList<Message>>()


    fun getMessages(player: Player): List<Message> {
        return getMessages(player.uniqueId)
    }

    fun getMessages(uuid: UUID): List<Message> {
        return getMessages(uuid.toString())
    }

    fun getMessages(uuid: String): List<Message> {
        return messages[uuid] ?: emptyList()
    }

    fun addMessage(player: Player, message: Message) {
        addMessage(player.uniqueId, message)
    }

    fun addMessage(uuid: UUID, message: Message) {
        addMessage(uuid.toString(), message)
    }

    fun addMessage(uuid: String, message: Message) {
        val list = messages[uuid] ?: arrayListOf()
        list.add(message)
        messages[uuid] = list
    }

}