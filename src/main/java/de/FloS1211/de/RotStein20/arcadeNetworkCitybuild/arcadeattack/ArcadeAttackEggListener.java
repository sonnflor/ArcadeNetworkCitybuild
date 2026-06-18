package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.arcadeattack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Color;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;

public class ArcadeAttackEggListener implements Listener {

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    if (!(event.getPlayer() instanceof Player player)) return;

    if (event.getInventory().getType() != InventoryType.ENDER_CHEST) return;

    Inventory ec = player.getEnderChest();
    World world = player.getWorld();
    Location loc = player.getLocation();

    for (int i = 0; i < ec.getSize(); i++) {
      ItemStack item = ec.getItem(i);

      if (item != null && item.getType() == Material.DRAGON_EGG) {
        ec.setItem(i, null); // ❗ Item entfernen
        world.dropItemNaturally(loc, item);
      }
    }
  }

  public static void startEggParticleTask() {
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          Inventory inv = player.getInventory();
          for (ItemStack item : inv.getContents()) {
            if (item!=null) {
              if (item.getType().equals(Material.DRAGON_EGG)) {
                player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 2.2, 0), 75, 0.5, 1.5, 0.5, 0, new Particle.DustOptions(Color.fromRGB(170, 0, 255), 1.5F));
              }
            }
          }
        }
      }
    }.runTaskTimer(ArcadeNetworkCitybuild.getInstance(), 0L, 20L);
  }
}