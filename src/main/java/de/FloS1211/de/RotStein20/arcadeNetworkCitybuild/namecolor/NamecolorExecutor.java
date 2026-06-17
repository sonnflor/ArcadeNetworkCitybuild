package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NamecolorExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage("Dieser Command kann nur als Spieler ausgeführt werden!");
      return true;
    }
    player.openInventory(getNamecolorGUI(player));
    return true;
  }

  private Inventory getNamecolorGUI(Player player) {
    int i = 0;
    String uuid = player.getUniqueId().toString();
    Inventory gui = Bukkit.createInventory(new CustomGuiHolder(),54, Component.text("Namensfarben"));
    for (String colName : NamecolorManager.namecolorNames) {
      gui.setItem(i,getNamecolorItem(NamecolorManager.namecolors.get(colName),uuid));
      i++;
    }
    ItemStack voidItem = new ItemStack(Material.STRUCTURE_VOID);
    ItemMeta voidMeta = voidItem.getItemMeta();
    voidMeta.lore(List.of(Component.text("Klicke, um deine Namensfarbe zu deaktivieren").color(NamedTextColor.GRAY)));
    voidMeta.itemName(Component.text("Namensfarbe deaktivieren").color(NamedTextColor.GRAY));
    voidMeta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING,"default");
    voidItem.setItemMeta(voidMeta);
    gui.setItem(40,voidItem);
    return gui;
  }

  private ItemStack getNamecolorItem(Namecolor col,String uuid) {
    if (NamecolorManager.getUnlockedNamecolors(uuid).contains(col.getName())) {
      return getUnlockedItem(col);
    } else {
      return getLockedItem(col);
    }
  }

  private ItemStack getUnlockedItem(Namecolor col) {
    ItemStack item = new ItemStack(col.getItem());
    ItemMeta meta = item.getItemMeta();
    meta.lore(List.of(Component.text("Klicke, um die Namensfarbe zu aktivieren").color(NamedTextColor.GRAY)));
    meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, col.getName());
    meta.itemName(Component.text("Namensfarbe").color(NamedTextColor.GREEN).append(Component.text(" • ").color(NamedTextColor.GRAY).append(NamecolorManager.getColoredName(col.getName(),col.getName(),true))));
    item.setItemMeta(meta);
    return item;
  }

  private ItemStack getLockedItem(Namecolor col) {
    ItemStack item = new ItemStack(Material.BARRIER);
    ItemMeta meta = item.getItemMeta();
    meta.lore(List.of(Component.text("Du hast diese Namensfarbe noch nicht freigeschaltet.").color(NamedTextColor.RED),Component.text("Du kannst Namensfarben über Crates erhalten. ").color(NamedTextColor.RED)));
    meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, col.getName());
    meta.itemName(Component.text("Namensfarbe").color(NamedTextColor.RED).append(Component.text(" • ").color(NamedTextColor.RED).append(NamecolorManager.getColoredName(col.getName(),col.getName(),true))));
    item.setItemMeta(meta);
    return item;
  }
}
