package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GuiManager implements Listener {
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    Inventory inv = event.getClickedInventory();
    if (inv == null) return;
    if (!inv.equals(event.getView().getTopInventory())) return;
    InventoryHolder holder = inv.getHolder();
    if (!(holder instanceof GuiHolder(Gui gui))) return;
    event.setCancelled(true);
    ItemStack item = event.getCurrentItem();
    if (item == null || item.getType().equals(Material.AIR)) return;
    int slot = event.getSlot();
    int page = gui.getPage();
    GuiElement el = gui.getElement(page,slot);
    if (el == null) return;
    if (el instanceof GuiSwitch) ((GuiSwitch) el).onClick((Player) event.getWhoClicked(), gui);
    else if (el instanceof GuiButton) ((GuiButton) el).onClick((Player) event.getWhoClicked(), gui, null);
  }
}
