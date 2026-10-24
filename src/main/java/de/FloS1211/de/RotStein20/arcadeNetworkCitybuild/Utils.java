package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Utils {
  public static void init() {

  }
  public static NamespacedKey key_custom = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "custom");
  public static NamespacedKey key_coin_amount = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "coin_amount");
  public static NamespacedKey key_sidebar = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"sidebar");
  public static NamespacedKey key_keep_inv = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"keep_inv");
  public static NamespacedKey key_shop_owner = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"shop_owner");
  public static NamespacedKey key_shop_trades = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"shop_trades");
  public static NamespacedKey key_shop_interaction = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(),"shop_interaction");

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
  public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
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
  public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
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

  public static List<ItemStack> splitItemsInStacks(ItemStack item, int amount) {
    List<ItemStack> result = new ArrayList<>();
    int maxStackSize = item.getMaxStackSize();

    while (amount > 0) {
      int stackAmount = Math.min(amount, maxStackSize);

      ItemStack drop = item.clone();
      drop.setAmount(stackAmount);

      result.add(drop);

      amount -= stackAmount;
    }
    return result;
  }

  public static void dropItems(Location location, ItemStack item, int amount) {
    World world = location.getWorld();
    if (world == null || item == null || item.getType().isAir() || amount <= 0) return;
    for (ItemStack drop : splitItemsInStacks(item, amount)) {
      world.dropItemNaturally(location, drop);
    }
  }
}
