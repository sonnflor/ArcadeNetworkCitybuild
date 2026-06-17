package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class FriendsGUIListener implements Listener {
    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        // Nur auf GUIs mit unserem CustomGuiHolder reagieren
        if (!(event.getView().getTopInventory().getHolder() instanceof de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder)) return;
        event.setCancelled(true);
        if (event.getClickedInventory() == null) return;
        if (!event.getView().getTopInventory().equals(event.getClickedInventory())) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        // Seitenumschaltung: wir lesen den im Item gespeicherten Ziel-Page-Wert
        NamespacedKey pageKey = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "friends_page");
        Integer page = clicked.getItemMeta() != null && clicked.getItemMeta().getPersistentDataContainer().has(pageKey, PersistentDataType.INTEGER)
            ? clicked.getItemMeta().getPersistentDataContainer().get(pageKey, PersistentDataType.INTEGER)
            : null;
        if (page != null) {
            // Öffne neue Seite für den klickenden Spieler
            if (event.getWhoClicked() instanceof Player p) {
                p.openInventory(Utils.getFriendsGui(p, page));
            }
        }
    }

}
