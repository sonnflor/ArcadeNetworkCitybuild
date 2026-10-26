package de.FloS1211.de.RotStein20.arcadeNetwork.shop;

import org.bukkit.block.Shelf;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class ShopConfigGuiHolder implements InventoryHolder {
  Shelf shelf;
  List<ShopTrade> trades;
  int activeItemSelected = -1;
  int activePriceSelected = -1;
  private Inventory inventory;

  @Override
  public Inventory getInventory() {
    return inventory;
  }

  public ShopConfigGuiHolder(List<ShopTrade> trades, Shelf shelf) {
    this.shelf = shelf;
    this.trades = trades;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
}
