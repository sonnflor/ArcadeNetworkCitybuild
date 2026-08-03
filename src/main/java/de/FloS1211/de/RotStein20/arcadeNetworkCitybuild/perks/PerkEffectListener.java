package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PerkEffectListener implements Listener {
  public static void updateBirdPerk(Player player) {
    switchBirdPerk(player, PerkManager.isPerkActive(player, "bird"));
  }
  public static void switchBirdPerk(Player player, boolean state) {
    if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
      player.setAllowFlight(state);
    } else {
      player.setAllowFlight(true);
    }
  }
  @EventHandler
  public void onJoinGeneral(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(ArcadeNetworkCitybuild.getInstance(), () -> {
      PerkManager.updatePerks(player);
      updateBirdPerk(player);
    }, 1L);
  }
  @EventHandler
  public void onRespawnGeneral(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(ArcadeNetworkCitybuild.getInstance(), () -> {
      PerkManager.updatePerks(player);
      updateBirdPerk(player);
    }, 1L);
  }
  @EventHandler
  public void onGameModeBirdPerk(PlayerGameModeChangeEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(ArcadeNetworkCitybuild.getInstance(), () -> {
      updateBirdPerk(player);
    }, 1L);
  }
  @EventHandler
  public void onFallDamageCatPerk(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player player)) return;
    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
      if (PerkManager.isPerkActive(player, "cat")) {
        event.setCancelled(true);
      }
    }
  }
  @EventHandler
  public void onBlockBreakTelekinesisPerk(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();

    if (player.getGameMode() == GameMode.CREATIVE) return;
    if (!PerkManager.isPerkActive(player, "telekinesis")) return;
    Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
    event.setDropItems(false);
    for (ItemStack item : drops) {
      HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
      for (ItemStack rest : leftover.values()) {
        player.getWorld().dropItemNaturally(player.getLocation(), rest);
      }
    }
    block.setType(Material.AIR);
  }
  @EventHandler
  public void onEntityDeathTelekinesisPerk(EntityDeathEvent event) {
    Player killer = event.getEntity().getKiller();
    if (killer == null) return;
    if (!PerkManager.isPerkActive(killer, "telekinesis")) return;
    List<ItemStack> drops = new ArrayList<>(event.getDrops());
    event.getDrops().clear();
    for (ItemStack item : drops) {
      HashMap<Integer, ItemStack> leftover = killer.getInventory().addItem(item);
      for (ItemStack rest : leftover.values()) {
        killer.getWorld().dropItemNaturally(killer.getLocation(), rest);
      }
    }
  }
  @EventHandler
  public void onSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    Location bottomLocation = player.getLocation();
    boolean hasDolphinPerk = PerkManager.isPerkActive(player, "dolphin") &&
        (event.isSneaking()&&player.isSwimming()) &&
        (bottomLocation.getBlock().getType().equals(Material.WATER));
    boolean hasRocketPerk = PerkManager.isPerkActive(player,"rocket") &&
        (event.isSneaking()&&player.isGliding());
    if (hasRocketPerk || hasDolphinPerk) {
      Location location = player.getEyeLocation();
      double speed = (hasRocketPerk && hasDolphinPerk) ? 5.5 : 3.5;
      Vector direction = location.getDirection();
      player.setVelocity(direction.multiply(speed));
    }
  }
  @EventHandler
  public void onBlockXPPerk(BlockBreakEvent event) {
    Player player = event.getPlayer();
    if (!PerkManager.isPerkActive(player,"xp")) return;
    event.setExpToDrop(event.getExpToDrop()*2);
  }
  @EventHandler
  public void onEntityXPPerk(EntityDeathEvent event) {
    if (event.getDamageSource().getCausingEntity()== null||!event.getDamageSource().getCausingEntity().getType().equals(EntityType.PLAYER)) return;
    Player player = (Player) event.getDamageSource().getCausingEntity();
    if (!PerkManager.isPerkActive(player,"xp")) return;
    event.setDroppedExp(event.getDroppedExp()*2);
  }
  @EventHandler
  public void onKeepInvDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    if (!PerkManager.isPerkActive(player,"keep_inv")) return;
    String content = toBase64(player.getInventory());
    String armor = itemStackArrayToBase64(player.getInventory().getArmorContents());
    event.setDroppedExp(0);
    event.getDrops().clear();
    player.getPersistentDataContainer().set(Utils.key_keep_inv,PersistentDataType.STRING,content+"|"+armor+"|"+player.getTotalExperience());
  }
  @EventHandler
  public void onKeepInvRespawn(PlayerRespawnEvent event) throws IOException {
    Player player = event.getPlayer();
    if (!PerkManager.isPerkActive(player,"keep_inv")) return;
    String json = player.getPersistentDataContainer().get(Utils.key_keep_inv,PersistentDataType.STRING);
    if (json == null) return;
    ItemStack[] contents = itemStackArrayFromBase64(json.split("\\|",3)[0]);
    player.getInventory().setContents(contents);
    ItemStack[] armor = itemStackArrayFromBase64(json.split("\\|",3)[1]);
    player.getInventory().setArmorContents(armor);
    Bukkit.getScheduler().runTaskLater(ArcadeNetworkCitybuild.getInstance(), () -> {
      player.setTotalExperience(0); // wichtig: reset zuerst
      player.setExp(0);
      player.setLevel(0);
      player.giveExp(Integer.parseInt(json.split("\\|",3)[2]));
    }, 1L);
    player.getPersistentDataContainer().remove(Utils.key_keep_inv);
  }
  private String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      // Write the size of the inventory
      dataOutput.writeInt(items.length);

      // Save every element in the list
      for (int i = 0; i < items.length; i++) {
        dataOutput.writeObject(items[i]);
      }

      // Serialize that array
      dataOutput.close();
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }
  private String toBase64(Inventory inventory) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      // Write the size of the inventory
      dataOutput.writeInt(inventory.getSize());

      // Save every element in the list
      for (int i = 0; i < inventory.getSize(); i++) {
        dataOutput.writeObject(inventory.getItem(i));
      }

      // Serialize that array
      dataOutput.close();
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }
  private ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      ItemStack[] items = new ItemStack[dataInput.readInt()];

      // Read the serialized inventory
      for (int i = 0; i < items.length; i++) {
        items[i] = (ItemStack) dataInput.readObject();
      }

      dataInput.close();
      return items;
    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }
}
