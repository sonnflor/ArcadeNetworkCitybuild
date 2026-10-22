package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;
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
import java.util.Locale;

public class NamecolorExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      LanguageManager.send(commandSender, "general.invalid-executor");
      return true;
    }
    player.openInventory(getNamecolorGUI(player));
    return true;
  }

  private Inventory getNamecolorGUI(Player player) {
    int i = 0;
    String uuid = player.getUniqueId().toString();
    Inventory gui = Bukkit.createInventory(new CustomGuiHolder(),54, LanguageManager.get(player, "namecolor.gui.namecolor.title"));
    for (String colName : NamecolorManager.namecolorNames) {
      gui.setItem(i,getNamecolorItem(NamecolorManager.namecolors.get(colName),uuid, player.locale()));
      i++;
    }
    ItemStack voidItem = new ItemStack(Material.STRUCTURE_VOID);
    ItemMeta voidMeta = voidItem.getItemMeta();
    voidMeta.lore(LanguageManager.getList(player, "namecolor.gui.namecolor.deactivate-lore"));
    voidMeta.itemName(LanguageManager.get(player, "namecolor.gui.namecolor.deactivate"));
    voidMeta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING,"default");
    voidItem.setItemMeta(voidMeta);
    gui.setItem(40,voidItem);
    return gui;
  }

  private ItemStack getNamecolorItem(Namecolor col, String uuid, Locale locale) {
    if (NamecolorManager.getUnlockedNamecolors(uuid).contains(col.name())) {
      return getUnlockedItem(col, locale);
    } else {
      return getLockedItem(col, locale);
    }
  }

  private ItemStack getUnlockedItem(Namecolor col, Locale locale) {
    ItemStack item = new ItemStack(col.item());
    ItemMeta meta = item.getItemMeta();
    meta.lore(LanguageManager.getList(locale, "namecolor.gui.namecolor.namecolor-lore"));
    meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, col.name());
    meta.itemName(LanguageManager.get(locale, "namecolor.gui.namecolor.namecolor-pre").append(NamecolorManager.getColoredName(col.name(),col.name(),true)));
    item.setItemMeta(meta);
    return item;
  }

  private ItemStack getLockedItem(Namecolor col, Locale locale) {
    ItemStack item = new ItemStack(Material.BARRIER);
    ItemMeta meta = item.getItemMeta();
    meta.lore(LanguageManager.getList(locale, "namecolor.gui.namecolor.forbidden-lore"));
    meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, col.name());
    meta.itemName(LanguageManager.get(locale, "namecolor.gui.namecolor.forbidden-pre").append(NamecolorManager.getColoredName(col.name(),col.name(),true)));
    item.setItemMeta(meta);
    return item;
  }
}