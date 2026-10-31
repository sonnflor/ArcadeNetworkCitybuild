package de.FloS1211.de.RotStein20.arcadeNetwork.ec;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EcGuiHolder implements InventoryHolder {
  UUID targetUuid;
  public EcGuiHolder(UUID targetUuid) {
    this.targetUuid = targetUuid;
  }
  @Override
  public @NotNull Inventory getInventory() {
    return null;
  }
}
