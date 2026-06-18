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
  @SuppressWarnings("UnstableApiUsage")
  public static Dialog getConsentDialog(Player player, Component title, Consumer<Player> onAccept) {
    return Dialog.create(builder -> builder
        .empty()
        .base(DialogBase.builder(title)
            .inputs(List.of())
            .build()
        )
        .type(DialogType.confirmation(
            ActionButton.builder(Component.text("Akzeptieren"))
                .action(DialogAction.customClick(
                    (view, audience) -> onAccept.accept(player),
                    ClickCallback.Options.builder().build()
                ))
                .build(),
            ActionButton.builder(Component.text("Abbrechen"))
                .build()
        ))
    );
  }
  public static String formatDate(long unixSec) {
    ZoneId zone = ZoneId.systemDefault();
    ZonedDateTime zonedDateTime = Instant.ofEpochSecond(unixSec).atZone(zone);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z");
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

  /**
   * Erzeugt einen Spieler-Kopf für ein gegebenes OfflinePlayer-Objekt.
   */
  public static ItemStack getHeadFromOfflinePlayer(OfflinePlayer player){
    ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
    if (itemMeta != null) {
      itemMeta.setOwningPlayer(player);
      itemStack.setItemMeta(itemMeta);
    }
    return itemStack;
  }

  /**
   * Baut die Freunde-GUI für `player` und die gewünschte `page` auf.
   */
  public static Inventory getFriendsGui (Player player, int page){
    Inventory gui= Bukkit.createInventory(new CustomGuiHolder(),54, Component.text("Freunde"));
    String uuid = player.getUniqueId().toString();
    SQLTable table = new SQLTable("friends","uuid = ?", List.of(uuid));
    int slot =10;
    int startIndex = page * 28;
    for (int i = startIndex; i < table.size() && i < startIndex + 28; i++){
      OfflinePlayer targetPlayer;
      try {
        targetPlayer = Bukkit.getOfflinePlayer(UUID.fromString(table.getStringValue("targetUuid",i)));
      } catch (Exception e) {
        continue;
      }
      ItemStack item = getHeadFromOfflinePlayer(targetPlayer);
      SkullMeta meta = (SkullMeta) item.getItemMeta();
      if (meta == null) continue;
      List<Component> lore = List.of(Component.text(""),Component.text("§7Zuletzt Online " + (targetPlayer.isOnline() ? "jetzt" : Utils.formatDate(targetPlayer.getLastSeen()))),Component.text("§7Hinzugefügt am " + Utils.formatDate(table.getIntValue("added",i))));
      meta.lore(lore);
      meta.displayName(Component.text(targetPlayer.getName()));
      item.setItemMeta(meta);
      gui.setItem(slot,item);
      slot++;
      if ((slot+1)%9==0) slot+=2;
    }
    List<Integer> pos = List.of(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53);
    for (int i :pos){
      gui.setItem(i,emptyGlassPane);
    }
    NamespacedKey pageKey = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "friends_page");
    if (page > 0) {
      ItemStack pageItem = new ItemStack(Material.PAPER);
      ItemMeta pageMeta = pageItem.getItemMeta();
      if (pageMeta != null) {
        pageMeta.displayName(Component.text("Vorherige Seite"));
        pageMeta.getPersistentDataContainer().set(pageKey, PersistentDataType.INTEGER,page-1);
        pageItem.setItemMeta(pageMeta);
        gui.setItem(44,pageItem);
      }
    }
    if (page+1 < Math.ceil(table.size() / 28.0)) {
      ItemStack pageItem = new ItemStack(Material.PAPER);
      ItemMeta pageMeta = pageItem.getItemMeta();
      if (pageMeta != null) {
        pageMeta.displayName(Component.text("Nächste Seite"));
        pageMeta.getPersistentDataContainer().set(pageKey, PersistentDataType.INTEGER,page+1);
        pageItem.setItemMeta(pageMeta);
        gui.setItem(53,pageItem);
      }
    }
    return gui;
  }
}
