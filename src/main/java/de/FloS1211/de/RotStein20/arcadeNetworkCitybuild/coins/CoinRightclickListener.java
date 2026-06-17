package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.SidebarManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class CoinRightclickListener implements Listener {
  @EventHandler
  public void onCoinsRightclick (PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (itemStack.getType() != Material.SUNFLOWER || !(itemStack.hasItemMeta())) {
      return;
    }
    if (event.getAction() != Action.RIGHT_CLICK_AIR) {
      return;
    }
    event.setCancelled(true);
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    if (!Objects.equals(container.get(Utils.key_custom, PersistentDataType.STRING), "Coin")) {
      return;
    }
    BigDecimal coins = new BigDecimal(container.get(Utils.key_coin_amount, PersistentDataType.DOUBLE));
    int itemAmount = 1;
    if (player.isSneaking()) {
      itemAmount = itemStack.getAmount();
    }
    if (player.getGameMode() != GameMode.CREATIVE) {
      itemStack.setAmount(itemStack.getAmount() - itemAmount);
    }
    CoinsManager.addCoins(player.getUniqueId().toString(),coins.multiply(new BigDecimal(itemAmount)));
    SidebarManager.setSidebar(player);

    player.sendMessage(MessageManager.get("coins-deposit", Map.of("amount",(coins.doubleValue() * itemAmount)+"")));
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
  }
}
