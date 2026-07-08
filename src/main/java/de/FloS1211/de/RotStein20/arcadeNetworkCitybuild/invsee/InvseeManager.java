package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolderCanMoveItem;
import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class InvseeManager implements Listener {
  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    if (event.getView().getTopInventory().getHolder() instanceof CustomGuiHolderCanMoveItem) {
      if (event.getPlayer().hasPermission("ac.admin")) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(PlainTextComponentSerializer.plainText().serialize(event.getView().title()).substring(13));
        if (target.isOnline()) {
          setOnlineInv(target.getPlayer(), event.getView().getTopInventory());
        } else {
          setOfflineInv(target.getUniqueId(), event.getView().getTopInventory());
        }
      }
    }
  }
  // Beispielmethode: Inventory zurückliefern (ohne Threading)
  public static Inventory getOfflineInv(UUID uuid, boolean shouldReplace) {
    File file = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata/"+uuid+".dat");
    if (!file.exists()) return null;
    try {
      ReadWriteNBT nbt = NBT.readFile(file);
      Inventory inv = Bukkit.createInventory(shouldReplace ? new CustomGuiHolderCanMoveItem() : new CustomGuiHolder(), 54, "Inventar von " + Bukkit.getOfflinePlayer(uuid).getName());
      ReadWriteNBTCompoundList list = nbt.getCompoundList("Inventory");
      for (ReadWriteNBT tag : list) {
        int slot = tag.getInteger("Slot");
        ItemStack item = NBT.itemStackFromNBT(tag);
        if (0 <= slot && slot < 36) inv.setItem(slot, item);
        else if (slot == 100) inv.setItem(45, item); // Feet
        else if (slot == 101) inv.setItem(46, item); // Legs
        else if (slot == 102) inv.setItem(47, item); // Chest
        else if (slot == 103) inv.setItem(48, item); // Head
        else if (slot == 99)  inv.setItem(49, item); // Offhand
      }
      return inv;
    } catch (IOException e) {
      return Bukkit.createInventory(new CustomGuiHolder(), 54, "Inventar von " + Bukkit.getPlayer(uuid).getName());
    }
  }


  public static Inventory getOnlineInv(Player player, boolean shouldReplace) {
    Inventory inv = Bukkit.createInventory(shouldReplace ? new CustomGuiHolderCanMoveItem() : new CustomGuiHolder(), 54, "Inventar von " + Bukkit.getPlayer(player.getUniqueId()).getName());
    PlayerInventory playerInv = player.getInventory();

    for (int i = 9; i < 36; i++) {
      inv.setItem(i-9,playerInv.getItem(i));
    }
    for (int i = 0; i < 9; i++) {
      inv.setItem(27+i,playerInv.getItem(i));
    }
    inv.setItem(45,playerInv.getHelmet());
    inv.setItem(46,playerInv.getChestplate());
    inv.setItem(47,playerInv.getLeggings());
    inv.setItem(48,playerInv.getBoots());
    inv.setItem(49,playerInv.getItemInOffHand());
    return inv;
  }

  public static void setOnlineInv(Player player, Inventory inv) {
    PlayerInventory playerInv = player.getInventory();

    // Main Inventory
    for (int i = 9; i < 36; i++) {
      playerInv.setItem(i, inv.getItem(i - 9));
    }

    // Hotbar
    for (int i = 0; i < 9; i++) {
      playerInv.setItem(i, inv.getItem(27 + i));
    }

    playerInv.setHelmet(inv.getItem(45));
    playerInv.setChestplate(inv.getItem(46));
    playerInv.setLeggings(inv.getItem(47));
    playerInv.setBoots(inv.getItem(48));
    playerInv.setItemInOffHand(inv.getItem(49));

    player.updateInventory();
  }

  public static void setOfflineInv(UUID uuid, Inventory inv) {
    File file = new File(
        Bukkit.getWorlds().get(0).getWorldFolder(),
        "playerdata/" + uuid + ".dat"
    );
    if (!file.exists()) {
      return;
    }
    try {
      ReadWriteNBT nbt = NBT.readFile(file);
      ReadWriteNBTCompoundList list = nbt.getCompoundList("Inventory");
      // Altes Inventar löschen
      list.clear();

      // Main Inventory
      for (int slot = 0; slot < 36; slot++) {
        ItemStack item = inv.getItem(slot);
        if (item == null || item.getType().isAir()) {
          continue;
        }
        ReadWriteNBT itemTag = NBT.itemStackToNBT(item);
        itemTag.setInteger("Slot", slot);
        list.addCompound(itemTag);
      }
      // Armor
      addItem(list, inv.getItem(45), 103); // Head
      addItem(list, inv.getItem(46), 102); // Chest
      addItem(list, inv.getItem(47), 101); // Legs
      addItem(list, inv.getItem(48), 100); // Feet

      // Offhand
      addItem(list, inv.getItem(49), 99);

      NBT.writeFile(file, nbt);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void addItem(
      ReadWriteNBTCompoundList list,
      ItemStack item,
      int mcSlot
  ) {
    if (item == null || item.getType().isAir()) {
      return;
    }

    ReadWriteNBT itemTag = NBT.itemStackToNBT(item);
    itemTag.setInteger("Slot", mcSlot);

    list.addCompound(itemTag);
  }
}
