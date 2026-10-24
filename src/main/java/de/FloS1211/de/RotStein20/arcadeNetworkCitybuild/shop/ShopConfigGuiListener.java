package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.shop;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinsManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;
import org.bukkit.Material;
import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.List;

public class ShopConfigGuiListener implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getInventory().getHolder() instanceof ShopConfigGuiHolder holder)) return;
    Player player = (Player) event.getWhoClicked();
    event.setCancelled(true);
    Inventory gui = event.getView().getTopInventory();
    int slot = event.getSlot();
    Shelf shelf = (Shelf) holder.shelf.getBlock().getState();
    List<ShopTrade> trades = holder.trades;
    if (event.getClickedInventory() == null || (event.getClickedInventory().equals(gui) && !event.getCursor().isEmpty())) {
      return;
    }
    if (holder.activeItemSelected > -1) {
      if (!event.getClickedInventory().equals(gui)) {
        ItemStack newItem = event.getCurrentItem();
        if (newItem == null || newItem.getType().isAir()) {
          newItem = ShopManager.getShopConfigPlaceholderItem(player);
        }
        trades.get(holder.activeItemSelected).item = newItem.clone();
        trades.get(holder.activeItemSelected).amount = newItem.getAmount();
        gui.setItem(9+(holder.activeItemSelected*3+1), newItem);
        shelf.getPersistentDataContainer().set(Utils.key_shop_trades, PersistentDataType.STRING, trades.stream().map(ShopTrade::toString).reduce((a, b) -> a+";"+b).orElse(""));
        if (newItem.getType() != Material.STRUCTURE_VOID)
          shelf.getSnapshotInventory().setItem(holder.activeItemSelected, newItem);
        else
          shelf.getSnapshotInventory().setItem(holder.activeItemSelected, null);
        shelf.update(true, false);
      } else {
        ItemStack orgItem = trades.get(holder.activeItemSelected).item;
        orgItem.setAmount(trades.get(holder.activeItemSelected).amount);
        gui.setItem(9+(holder.activeItemSelected*3+1), orgItem);
      }
      holder.activeItemSelected = -1;
    } else if (holder.activePriceSelected > -1) {
      if (!event.getClickedInventory().equals(gui)) {
        ItemStack newPriceItem = event.getCurrentItem();
        if (newPriceItem == null
            || newPriceItem.getType() != Material.SUNFLOWER
            || !newPriceItem.getPersistentDataContainer().has(Utils.key_coin_amount, PersistentDataType.DOUBLE))
        {
          LanguageManager.send(player, "shop.chat.click-failed-wrong_coin");
          return;
        }
        double newCoinAmount = newPriceItem.getPersistentDataContainer().get(Utils.key_coin_amount, PersistentDataType.DOUBLE);
        newPriceItem = CoinsManager.getCoinItem(BigDecimal.valueOf(newCoinAmount), 1);
        trades.get(holder.activePriceSelected).price = (int) newCoinAmount;
        gui.setItem(18+(holder.activePriceSelected*3+1), newPriceItem);
        shelf.getPersistentDataContainer().set(Utils.key_shop_trades, PersistentDataType.STRING, trades.stream().map(ShopTrade::toString).reduce((a, b) -> a+";"+b).orElse(""));
        shelf.update();
      } else {
        ItemStack orgItem = CoinsManager.getCoinItem(BigDecimal.valueOf(trades.get(holder.activePriceSelected).price), 1);
        gui.setItem(18+(holder.activePriceSelected*3+1), orgItem);
      }
      holder.activePriceSelected = -1;
    } else {
      if (!event.getClickedInventory().equals(gui)) {
        event.setCancelled(event.getClick().isShiftClick());
        return;
      }
      if (slot == 10 || slot == 13 || slot == 16) {
        int selected = (slot-10)/3;
        if (trades.get(selected).itemsInStock > 0) {
          LanguageManager.send(player, "shop.chat.click-failed-filled_storage");
          return;
        }
        holder.activeItemSelected = selected;
        ItemStack selectPlaceholder = new ItemStack(Material.BARRIER);
        ItemMeta selectPlaceholderMeta = selectPlaceholder.getItemMeta();
        selectPlaceholderMeta.itemName(LanguageManager.get(player, "shop.gui.shop-config.select-placeholder-name"));
        selectPlaceholderMeta.lore(LanguageManager.getList(player, "shop.gui.shop-config.select-placeholder-lore"));
        selectPlaceholder.setItemMeta(selectPlaceholderMeta);
        gui.setItem(slot, new ItemStack(selectPlaceholder));
      } else if (slot == 19 || slot == 22 || slot == 25) {
        holder.activePriceSelected = (slot-19)/3;
        ItemStack selectPlaceholder = new ItemStack(Material.BARRIER);
        ItemMeta selectPlaceholderMeta = selectPlaceholder.getItemMeta();
        selectPlaceholderMeta.itemName(LanguageManager.get(player, "shop.gui.shop-config.select-placeholder-coin-name"));
        selectPlaceholderMeta.lore(LanguageManager.getList(player, "shop.gui.shop-config.select-placeholder-coin-lore"));
        selectPlaceholder.setItemMeta(selectPlaceholderMeta);
        gui.setItem(slot, selectPlaceholder);
      } else if (slot == 37 || slot == 40 || slot == 43) {
        int tradeIndex = (slot-37)/3;
        ShopManager.openStorageConfig(player, shelf, tradeIndex);
      }
    }
  }

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    if (!(event.getView().getTopInventory().getHolder() instanceof ShopConfigGuiHolder holder)) return;
    if (event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) {
        return;
    }
    Shelf shelf = (Shelf) holder.shelf.getBlock().getState();
    shelf.getPersistentDataContainer().set(Utils.key_shop_interaction, PersistentDataType.STRING, "");
    shelf.update();
  }
}
