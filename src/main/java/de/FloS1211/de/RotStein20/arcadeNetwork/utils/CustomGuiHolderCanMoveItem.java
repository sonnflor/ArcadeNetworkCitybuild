package de.FloS1211.de.RotStein20.arcadeNetwork.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class CustomGuiHolderCanMoveItem implements InventoryHolder {
  @Override
  public @NotNull Inventory getInventory() {
    return null;
  }
}
