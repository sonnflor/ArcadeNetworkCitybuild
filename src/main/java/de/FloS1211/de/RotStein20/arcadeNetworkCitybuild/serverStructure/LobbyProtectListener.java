package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbyProtectListener implements Listener {
  private boolean shouldCancel(Player player) {
    return player.getWorld().getName().equalsIgnoreCase("lobby") && !player.hasPermission("ac.admin");
  }

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    if (shouldCancel(event.getPlayer())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    if (shouldCancel(event.getPlayer())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    if (shouldCancel(event.getPlayer())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInteractEntity(PlayerInteractEntityEvent event) {
    if (shouldCancel(event.getPlayer())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player player && shouldCancel(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onHit(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player player && shouldCancel(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onHunger(FoodLevelChangeEvent event) {
    if (event.getEntity() instanceof Player player && shouldCancel(player)) {
      event.setFoodLevel(20);
    }
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    if (shouldCancel(event.getPlayer())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPickup(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player player && shouldCancel(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInvClick(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player player && shouldCancel(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onArmorStand(PlayerArmorStandManipulateEvent event) {
    if (shouldCancel(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
}
