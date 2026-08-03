package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.UUID;
import java.time.LocalDateTime;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
  public static void init() {

  }
  public static NamespacedKey key_custom = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "custom");
  public static NamespacedKey key_coin_amount = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "coin_amount");
  public static NamespacedKey key_sidebar = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"sidebar");
  public static NamespacedKey key_keep_inv = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"keep_inv");

  public static Integer canFitItem(Player player, ItemStack itemsToAdd) {
    if (itemsToAdd == null || itemsToAdd.getAmount() <= 0) return 0;

    Inventory inv = player.getInventory();
    int remaining = itemsToAdd.getAmount();
    int maxStackSize = itemsToAdd.getMaxStackSize();

    // 1. Bestehende, gleiche ItemStacks auffüllen
    for (ItemStack content : inv.getStorageContents()) {
      if (content == null) continue;

      if (content.isSimilar(itemsToAdd)) {
        int space = maxStackSize - content.getAmount();
        if (space > 0) {
          int used = Math.min(space, remaining);
          remaining -= used;
          if (remaining <= 0) {
            return 0;
          }
        }
      }
    }

    // 2. Leere Slots nutzen
    for (ItemStack content : inv.getStorageContents()) {
      if (content == null) {
        int used = Math.min(maxStackSize, remaining);
        remaining -= used;
        if (remaining <= 0) {
          return 0;
        }
      }
    }

    // 3. Was übrig bleibt, passt nicht mehr rein
    return remaining;
  }
  public static String formatDate(long unixSec, String pattern) {
    ZoneId zone = ZoneId.systemDefault();
    ZonedDateTime zonedDateTime = Instant.ofEpochSecond(unixSec).atZone(zone);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    String formatted = zonedDateTime.format(formatter);
    return formatted;
  }
  public static String formatDuration(long seconds) {
    long days = seconds / 86400;
    seconds %= 86400;
    long hours = seconds / 3600;
    seconds %= 3600;
    long minutes = seconds / 60;
    seconds %= 60;
    return days + " Tage, "
        + hours + " Stunden, "
        + minutes + " Minuten und "
        + seconds + " Sekunden";
  }
  //für die Freunde-GUI: ItemStack eines Spieler-Kopfes mit Namen und Lore aus OfflinePlayer-Objekt erstellen
  // Einfaches Glas-Pane Item für GUI-Filler
  public static ItemStack emptyGlassPane;
  static {
    emptyGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    ItemMeta meta = emptyGlassPane.getItemMeta();
    if (meta != null) meta.displayName(Component.text(" "));
    emptyGlassPane.setItemMeta(meta);
  }

  public static ItemStack getHeadFromOfflinePlayer(OfflinePlayer player){
    ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
    if (itemMeta != null) {
      itemMeta.setOwningPlayer(player);
      itemStack.setItemMeta(itemMeta);
    }
    return itemStack;
  }
}
