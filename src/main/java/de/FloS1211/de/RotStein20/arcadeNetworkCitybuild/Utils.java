package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class Utils {
  public static void init() {

  }
  public static NamespacedKey key_custom = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "custom");
  public static NamespacedKey key_coin_amount = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "coin_amount");

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
}
