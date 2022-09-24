package plugin.util

import org.bukkit.Material
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

class ItemPresets {

    companion object {
        val LIME_PLACEHOLDER = ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(" ")

        val CLOSE_BARRIER = ItemBuilder(Material.BARRIER).name("§c§lClose")
        val BACK_ARROW = ItemBuilder(Material.TIPPED_ARROW).applyToMeta {
            (it as PotionMeta).basePotionData = PotionData(PotionType.INSTANT_DAMAGE)
        }.name("§c§lBack")

        val NEXT_PAGE = ItemBuilder(Material.SPECTRAL_ARROW).name("§aNext Page")
        val PREVIOUS_PAGE = ItemBuilder(Material.SPECTRAL_ARROW).name("§aPrevious Page")

        val SEARCH_SIGN = ItemBuilder(Material.WARPED_SIGN).name("§aSearch")

        val SUBMIT = ItemBuilder(Material.LIME_DYE).lore("§aClick to submit").name("§aSubmit")
    }

}