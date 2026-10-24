package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.shop;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ShopStorageGuiListener implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getInventory().getHolder() instanceof ShopStorageGuiHolder holder)) return;
    boolean isClickInTop = event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getTopInventory());
    boolean isClickShiftToTop = event.isShiftClick() && event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getBottomInventory());
    if (isClickInTop || isClickShiftToTop) {
      ItemStack movedItem = null;
      if (isClickInTop)
        movedItem = event.getCursor();
      else if (isClickShiftToTop)
        movedItem = event.getCurrentItem();
      if (event.getClick().equals(ClickType.NUMBER_KEY))
        movedItem = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
      else if (event.getClick() == ClickType.SWAP_OFFHAND)
        movedItem = event.getWhoClicked()
            .getInventory()
            .getItemInOffHand();
      ItemStack allowedItem = ShopTrade.fromString(holder.trades[holder.tradeIndex]).item;
      if (movedItem == null || movedItem.isEmpty()) return;
      if (!movedItem.isSimilar(allowedItem)) {
        event.setCancelled(true);
        LanguageManager.send((Player) event.getWhoClicked(), "shop.gui.storage.item_move-failed", "material", allowedItem.getType().name());
      }
    }
  }

  @EventHandler
  public void onDrag(InventoryDragEvent event) {
    if (!(event.getInventory().getHolder() instanceof ShopStorageGuiHolder holder)) return;
    Inventory top = event.getView().getTopInventory();

    boolean touchesTop = event.getRawSlots().stream()
        .anyMatch(rawSlot -> rawSlot < top.getSize());

    if (!touchesTop) {
      return;
    }

    ItemStack draggedItem = event.getOldCursor();

    ItemStack allowedItem =
        ShopTrade.fromString(holder.trades[holder.tradeIndex]).item;

    if (!draggedItem.isEmpty()
        && !draggedItem.isSimilar(allowedItem)) {
      event.setCancelled(true);
      LanguageManager.send((Player) event.getWhoClicked(), "shop.gui.storage.item_move-failed", "material", allowedItem.getType().name());
    }
  }

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    if (!(event.getInventory().getHolder() instanceof ShopStorageGuiHolder holder)) return;
    if (event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)
        || event.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) return;
    ShopTrade trade = ShopTrade.fromString(holder.trades[holder.tradeIndex]);
    trade.itemsInStock = Arrays.stream(event.getView().getTopInventory().getContents())
        .filter(item -> item != null && !item.getType().isAir())
        .mapToInt(ItemStack::getAmount)
        .sum();
    String[] trades = holder.trades;
    trades[holder.tradeIndex] = trade.toString();
    holder.trades = trades;
    Shelf shelf = (Shelf) holder.shelf.getBlock().getState();
    shelf.getPersistentDataContainer().set(Utils.key_shop_trades, PersistentDataType.STRING, Arrays.stream(trades).reduce((a, b) -> a+";"+b).orElse(""));
    shelf.update();
    Bukkit.getScheduler().runTask(
        ArcadeNetworkCitybuild.getInstance(),
        () -> ShopManager.openShopConfig((Player) event.getPlayer(), shelf)
    );
  }
}
