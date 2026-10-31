package de.FloS1211.de.RotStein20.arcadeNetwork.ec;

import de.FloS1211.de.RotStein20.arcadeNetwork.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.utils.CustomGuiHolder;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class EcExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(LanguageManager.get(Locale.ENGLISH, "general.chat.invalid-executor"));
      return true;
    }
    //syntax: /ec | /ec <player> | /ec player "edit"
    if (args.length == 0) {
      player.openInventory(player.getEnderChest());
    } else {
      OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
      if (!(targetPlayer.hasPlayedBefore() || targetPlayer.isOnline())) {
        LanguageManager.send(player, "general.chat.invalid-player");
        return true;
      }
      if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
        player.openInventory(player.getEnderChest());
        return true;
      }
      if (targetPlayer.isOnline()) {
        Player onlineTarget = targetPlayer.getPlayer();
        if (onlineTarget == null) return true;
        if (player.isOp() && args.length == 2 && args[1].equalsIgnoreCase("edit")) {
          player.openInventory(onlineTarget.getEnderChest());
        } else {
          Inventory ec = onlineTarget.getEnderChest();
          Inventory gui = Bukkit.createInventory(
              new CustomGuiHolder(),
              27,
              Component.text("Enderchest von ")
                  .append(PlayerNameManager.getPlayerName(onlineTarget.getUniqueId().toString())));
          for (int i = 0; i < ec.getSize(); i++) {
            gui.setItem(i, ec.getItem(i));
          }
          player.openInventory(gui);
        }
      } else {
        Inventory gui = getOfflineEnderChest(targetPlayer.getUniqueId(), player.isOp() && args.length == 2 && args[1].equalsIgnoreCase("edit"));
        if (gui == null) {
          player.sendMessage("Fehler");
          return true;
        }
        player.openInventory(gui);
      }
    }
    return true;
  }

  public static Inventory getOfflineEnderChest(UUID uuid, boolean edit) {
    File file = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata/" + uuid + ".dat");
    if (!file.exists()) return null;
    try {
      ReadWriteNBT nbt = NBT.readFile(file);
      Inventory inv = Bukkit.createInventory(edit ? new EcGuiHolder(uuid) : new CustomGuiHolder(), 27, Component.text("Enderchest von").append(PlayerNameManager.getPlayerName(uuid.toString())));
      ReadWriteNBTCompoundList list = nbt.getCompoundList("EnderItems");
      for (ReadWriteNBT tag : list) {
        int slot = tag.getByte("Slot");
        if (slot < 0 || slot >= 27) continue;
        ItemStack item = NBT.itemStackFromNBT(tag);
        inv.setItem(slot, item);
      }
      return inv;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void saveOfflineEnderChest(UUID uuid, Inventory inventory) throws IOException {
    if (Bukkit.getPlayer(uuid) != null) {
      throw new IllegalStateException("Spieler ist online!");
    }
    File file = new File(
        Bukkit.getWorlds().get(0).getWorldFolder(),
        "playerdata/" + uuid + ".dat"
    );
    if (!file.exists()) return;
    ReadWriteNBT nbt = NBT.readFile(file);
    nbt.removeKey("EnderItems");
    ReadWriteNBTCompoundList list =
        nbt.getCompoundList("EnderItems");
    for (int slot = 0; slot < 27; slot++) {
      ItemStack item = inventory.getItem(slot);
      if (item == null || item.getType().isAir()) {
        continue;
      }
      ReadWriteNBT itemNBT = NBT.itemStackToNBT(item);
      itemNBT.setByte("Slot", (byte) slot);
      list.addCompound(itemNBT);
    }
    NBT.writeFile(file, nbt);
  }
}
