package de.FloS1211.de.RotStein20.arcadeNetwork.ec;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.IOException;

public class EcGuiListener implements Listener {
  @EventHandler
  public void onEcClose(InventoryCloseEvent event) {
    if (event.getInventory().getHolder() instanceof EcGuiHolder holder) {
      try {
        EcExecutor.saveOfflineEnderChest(holder.targetUuid, event.getInventory());
      } catch (IOException | IllegalStateException e) {
        e.printStackTrace();
      }
    }
  }
}
