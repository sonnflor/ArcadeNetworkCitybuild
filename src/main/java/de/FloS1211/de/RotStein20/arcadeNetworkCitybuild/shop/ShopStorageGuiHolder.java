package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.shop;

import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ShopStorageGuiHolder implements InventoryHolder {
  @Override
  public org.bukkit.inventory.Inventory getInventory() {
    return inventory;
  }

  Player player;
  Shelf shelf;
  String[] trades;
  int tradeIndex;

  private Inventory inventory;

  public ShopStorageGuiHolder(Player player, Shelf shelf, String[] trades, int tradeIndex) {
    this.player = player;
    this.shelf = shelf;
    this.trades = trades;
    this.tradeIndex = tradeIndex;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
}
