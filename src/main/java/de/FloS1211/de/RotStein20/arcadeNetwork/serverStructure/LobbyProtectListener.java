package de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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

  public static void startVoidProtection() {
    if (!ProxyManager.getServerName().equalsIgnoreCase("lobby")) return;
    Bukkit.getScheduler().runTaskTimer(ArcadeNetwork.getInstance(), () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.getLocation().getY() < 0) {
          Vector direction = player.getLocation().getDirection().normalize();

          Vector boost = direction.multiply(2.0);
          boost.setY(boost.getY() + 2.5);

          player.setVelocity(boost);
          player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
          player.setGliding(true);
        }
      }
    }, 0L, 10L);
  }
}
