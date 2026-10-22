package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.shop;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import org.bukkit.block.Block;
import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ShopListener implements Listener {
  @EventHandler
  public void onShopShelfPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    EquipmentSlot slot = event.getHand();
    ItemStack placedItem = player.getInventory().getItem(slot);
    if (!placedItem.getType().name().endsWith("SHELF")) return;
    if (!Objects.equals(placedItem.getPersistentDataContainer().get(Utils.key_custom, PersistentDataType.STRING), "shop-shelf")) return;
    Block placedBlock = event.getBlockPlaced();
    Shelf shelf = (Shelf) placedBlock.getState();
    shelf.getPersistentDataContainer().set(Utils.key_shop_owner, PersistentDataType.STRING, player.getUniqueId().toString());
    shelf.getPersistentDataContainer().set(Utils.key_shop_storage, PersistentDataType.STRING, "");
  }

  @EventHandler
  public void onItemInShopShelf(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (event.getClickedBlock()==null || !(event.getClickedBlock().getBlockData() instanceof Shelf shelf)) return;
    if (!shelf.getPersistentDataContainer().has(Utils.key_shop_owner)) return;
    ItemStack item = event.getItem();
    
  }
}
