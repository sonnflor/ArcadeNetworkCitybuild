package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class NamecolorGuiListener implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getView().getTopInventory().getHolder() instanceof CustomGuiHolder)||!event.getView().title().equals(Component.text("Namensfarben"))) return;
    event.setCancelled(true);
    if (event.getClickedInventory() == null || !event.getClickedInventory().equals(event.getView().getTopInventory())) return;
    Player player = (Player) event.getWhoClicked();
    String uuid = player.getUniqueId().toString();
    ItemStack item = event.getCurrentItem();
    if (item == null || item.getType().equals(Material.AIR)) return;
    if (item.getType() == Material.BARRIER) {
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
      return;
    } else {
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }
    ItemMeta meta = item.getItemMeta();
    if (meta == null) return;
    PersistentDataContainer pdc = meta.getPersistentDataContainer();
    if (!pdc.has(Utils.key_custom)) return;
    String namecolor = pdc.get(Utils.key_custom, PersistentDataType.STRING);

    if (Objects.equals(namecolor, "default")) {
      LanguageManager.send(player, "namecolor.chat.deactivate");
      NamecolorManager.setNamecolor(uuid,"default");
    } else {
      NamecolorManager.setNamecolor(uuid,namecolor);
      player.sendMessage(LanguageManager.get(player, "namecolor.chat.activate-pre").append(NamecolorManager.getColoredName(namecolor, NamecolorManager.namecolors.get(namecolor).name(), true)).append(LanguageManager.get(player, "namecolor.chat.activate-aft")));
    }
    PlayerNameManager.updatePlayerName(uuid);
    player.closeInventory();
  }
}
