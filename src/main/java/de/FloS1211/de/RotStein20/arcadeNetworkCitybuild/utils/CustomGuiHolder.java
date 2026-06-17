package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import jdk.jfr.Enabled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CustomGuiHolder implements InventoryHolder, Listener {
  @Override
  public Inventory getInventory() {
    return null;
  }

  @EventHandler
  public void onInvClick(InventoryClickEvent event) {
   if (event.getView().getTopInventory().getHolder() instanceof CustomGuiHolder) {
     event.setCancelled(true);
   }
  }

  @EventHandler
  public void onInvDrag(InventoryDragEvent event) {
    if (event.getView().getTopInventory().getHolder() instanceof CustomGuiHolder) {
      event.setCancelled(true);
    }
  }
}
