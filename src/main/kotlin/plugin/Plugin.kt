package plugin
 import fr.minuskube.inv.InventoryManager
 import fr.minuskube.inv.SmartInventory
 import plugin.listener.ConnectListener
 import org.bukkit.Bukkit
 import org.bukkit.World
 import org.bukkit.WorldCreator
 import org.bukkit.WorldType
 import org.bukkit.plugin.java.JavaPlugin
 import plugin.commands.GetCommand
 import plugin.commands.ItemManagerCommand
 import plugin.commands.SaveItemCommand
 import plugin.item.ItemConfig

class Plugin : JavaPlugin() {

    companion object {
        var instance: Plugin? = null
            private set;

        var itemConfig = ItemConfig();

        lateinit var inventoryManager: InventoryManager

        var builder: SmartInventory.Builder = SmartInventory.builder()
            get() = SmartInventory.builder().manager(inventoryManager)
    }

    override fun onEnable(){
        instance = this

        inventoryManager = InventoryManager(this)
        inventoryManager.init();

        logger.info("WynnBox is enabled!")
        Bukkit.getPluginManager().registerEvents(ConnectListener(), this)
        getCommand("item")!!.setExecutor(ItemManagerCommand())
        getCommand("get")!!.setExecutor(GetCommand())
        getCommand("save")!!.setExecutor(SaveItemCommand())

        Bukkit.createWorld(
            WorldCreator("flat")
                .environment(World.Environment.NORMAL)
                .type(WorldType.FLAT)
                .generateStructures(false)
                .generator("flat")
        )

    }

    override fun onDisable() {
        logger.info("WynnBox is disabled!")
    }

}