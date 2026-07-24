package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record GuiHolder(Gui gui) implements InventoryHolder {
  @Override
  public @NotNull Inventory getInventory() {
    return null;
  }
}
