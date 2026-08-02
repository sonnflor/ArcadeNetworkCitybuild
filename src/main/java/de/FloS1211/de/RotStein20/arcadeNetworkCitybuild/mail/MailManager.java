package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mail;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButton;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButtonType;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiSwitch;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.Namecolor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import io.papermc.paper.registry.data.dialog.*;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MailManager {
  @SuppressWarnings("UnstableApiUsage")
  public static void sendMail(Player sender) {
    List<DialogBody> bodyContent = List.of(
        DialogBody.plainMessage(Component.text("Bitte gib Betreff und Nachricht ein: "))
    );
    List<DialogInput> bodyInputs = List.of(
        DialogInput.text("input_target" ,Component.text("Empfänger: ")).maxLength(16).build(),
        DialogInput.text("input_caption",Component.text("Betreff: ")).maxLength(35).build(),
        DialogInput.text("input_message",Component.text("Nachricht: ")).maxLength(3000).multiline(TextDialogInput.MultilineOptions.create(50,120)).build()
    );
    ActionButton cancelButton = ActionButton.builder(Component.text("Abbrechen")).build();
    ActionButton sendButton = ActionButton
        .builder(Component.text("Senden"))
        .action(
            DialogAction.customClick(
                (DialogResponseView view, Audience audience) -> {
                  String caption = view.getText("input_caption");
                  String message = view.getText("input_message");
                  String targetName = view.getText("input_target");
                  if (caption == null || caption.isBlank() || message == null || message.isBlank() || targetName == null || targetName.isBlank()) {
                    if (audience instanceof Player player) {
                      player.sendMessage(MessageManager.get("mail-wrong-inputs"));
                    }
                    return;
                  }
                  OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
                  if (!target.hasPlayedBefore() && !target.isOnline()) {
                    if (audience instanceof Player player) {
                      player.sendMessage(MessageManager.get("general-invalid-player"));
                    }
                    return;
                  }
                  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                  long millis = timestamp.getTime();
                  int days = (int) TimeUnit.MILLISECONDS.toDays(millis);
                  DatabaseManager.executeSQL("INSERT INTO mail (uuidSender, uuidTarget, unixDay, message, caption) VALUES (?,?,?,?,?)", List.of(sender.getUniqueId().toString(), target.getUniqueId().toString(),days, message, caption));
                  if (audience instanceof Player player) {
                    sender.sendMessage(MessageManager.get("mail-sent", Map.of("target", target.getName(), "caption", caption)));
                    if (target.isOnline()) {
                      target.getPlayer().sendMessage(MessageManager.get("mail-received", Map.of("sender", sender.getName(), "caption", caption)));
                    }
                    Bukkit.getScheduler().runTaskLater(ArcadeNetworkCitybuild.getInstance(), () -> {
                      openMail(player, "outbox",false);
                    }, 1L);
                  }
                },
                ClickCallback.Options.builder().build()
            )
        )
        .build();
    Dialog dialog = Dialog.create(builder -> builder
        .empty()
        .base(DialogBase.builder(Component.text("Neue Mail"))
            .body(bodyContent)
            .inputs(bodyInputs)
            .canCloseWithEscape(false)
            .build()
        )
        .type(DialogType.confirmation(sendButton, cancelButton))
    );
    sender.showDialog(dialog);
  }
  public static void openMail(Player player, String postOfficeBox, boolean onlyFavorites) { //postOfficeBox = Postfach
    Component guiTitle = Component.text(postOfficeBox.equals("inbox") ? "Posteingang" : "Postausgang");
    Gui gui = new Gui(54, guiTitle);
    gui.setElementInBottomBar(new GuiButton("sendMailButton", Material.NETHER_STAR, new MailButtonExecutor(), GuiButtonType.CUSTOM)
        .title(Component.text("Neue Mail").color(NamedTextColor.GREEN)),4);
    gui.setElementInBottomBar(GuiButton.getPrevPageButton(new MailButtonExecutor()),0);
    gui.setElementInBottomBar(GuiButton.getNextPageButton(new MailButtonExecutor()),8);
    gui.setElementInBottomBar(postOfficeBox.equals("inbox") ?
        new GuiButton("outbox",Material.CHEST,new MailButtonExecutor(),GuiButtonType.CUSTOM).title(Component.text("Postausgang")) :
        new GuiButton("inbox", Material.CHEST,new MailButtonExecutor(),GuiButtonType.CUSTOM).title(Component.text("Posteingang")),3);
    gui.setElementInBottomBar(new GuiButton("onlyFavorites",onlyFavorites ? Material.ENCHANTED_BOOK : Material.BOOK, new MailButtonExecutor(), GuiButtonType.CUSTOM)
        .title(Component.text(onlyFavorites ? "Nur Favoriten" : "Alle Mails"))
        .customData(Map.of("onlyFavorites", String.valueOf(!onlyFavorites))),5);
    SQLTable sqlTable = postOfficeBox.equals("inbox") ? getInbox(player.getUniqueId(),onlyFavorites) : getOutbox(player.getUniqueId(),onlyFavorites);
    for (int i = 0; i < sqlTable.size(); i++) {
      String playerRole;
      if (sqlTable.getStringValue("uuidSender",i).equals(player.getUniqueId().toString())) {
        if (sqlTable.getStringValue("uuidTarget",i).equals(player.getUniqueId().toString())) {
          playerRole = "both";
        } else {
          playerRole = "sender";
        }
      } else {
        playerRole = "target";
      }
      if (sqlTable.getBooleanValue((playerRole.equals("sender") ? "deletedBySender" : "deletedByTarget"),i)) {
        continue;
      }
      int unixDays = sqlTable.getIntValue("unixDay",i);
      String date = Utils.formatDate((unixDays * 86400L),"dd.MM.yyyy");
      OfflinePlayer sender = Bukkit.getOfflinePlayer(UUID.fromString(sqlTable.getStringValue("uuidSender",i)));
      OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(sqlTable.getStringValue("uuidTarget",i)));
      List<Component> lore = new ArrayList<>(List.of(
          Component.text("von: " + sender.getName()),
          Component.text("an: " + target.getName()),
          Component.text(date)
      ));
      if (playerRole.equals("sender") || playerRole.equals("both")) {
        boolean isRead = sqlTable.getBooleanValue("isRead",i);
        lore.add(Component.text(isRead ? "Gelesen" : "Ungelesen").color(isRead ? NamedTextColor.GREEN : NamedTextColor.RED));
      }
      boolean favorite = sqlTable.getBooleanValue((playerRole.equals("sender") ? "favoritedBySender" : "favoritedByTarget"),i);
      GuiButton button = new GuiButton("mail"+sqlTable.getStringValue("caption",i), Material.BOOK, new MailButtonExecutor(), GuiButtonType.CUSTOM)
          .title(Component.text((favorite ? "♥ " : "") +sqlTable.getStringValue("caption",i)))
          .lore(lore)
          .controls(Map.of("Links-Klick", "Mail öffnen", "Rechts-Klick", "Mail löschen", "Shift-Klick", favorite ? "Aus Favoriten entfernen" : "Zu Favoriten hinzufügen"))
          .enchantment(favorite)
          .customData(Map.of("message",sqlTable.getStringValue("message",i),
              "caption", sqlTable.getStringValue("caption",i),
              "author", sender.getName(),
              "id", sqlTable.getIntValue("id",i)+"",
              "playerRole", playerRole,
              "isRead", (sqlTable.getBooleanValue("isRead",i)?"true":"false"),
              "favorite", (sqlTable.getBooleanValue((playerRole.equals("sender") ? "favoritedBySender" : "favoritedByTarget"),i)?"true":"false")
          )
      );
      gui.addElement(button);
    }
    gui.setCustomData(Map.of("postOfficeBox",postOfficeBox,"onlyFavorites", String.valueOf(onlyFavorites)));
    player.openInventory(gui.buildInventory());
  }

  public static void deleteMail(int id, String role) {
    DatabaseManager.executeSQL("UPDATE mail SET deletedByTarget = ? WHERE id = ?", List.of(true, id));
    if (role.equals("sender") || role.equals("both")) {
      DatabaseManager.executeSQL("UPDATE mail SET deletedBySender = ? WHERE id = ?", List.of(true, id));
    }
    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(DatabaseManager.getTable("mail","id = ?", List.of(id)).getStringValue("uuidTarget",0)));
    if (player.isOnline()) {
      ((Player) player).sendMessage(MessageManager.get("mail-deleted", Map.of("caption", DatabaseManager.getTable("mail","id = ?", List.of(id)).getStringValue("caption",0))));
    }
  }

  public static void setMailFavorite(int id, boolean isFavorite, String role) {
    String column = role.equals("sender") ? "favoritedBySender" : "favoritedByTarget";
    DatabaseManager.executeSQL("UPDATE mail SET " + column + " = ? WHERE id = ?", List.of(isFavorite, id));
    if (role.equals("both")) {
      DatabaseManager.executeSQL("UPDATE mail SET favoritedBySender = ? WHERE id = ?", List.of(isFavorite, id));
    }
  }

  public static void setMailRead(int id) {
    DatabaseManager.executeSQL("UPDATE mail SET isRead = ? WHERE id = ?", List.of(true, id));
  }

  private static SQLTable getInbox(UUID uuid, boolean onlyFavorites) {
    if (onlyFavorites) {
      return new SQLTable("mail","uuidTarget = ? AND favoritedByTarget = ?", List.of(uuid.toString(), true),"favoritedByTarget DESC, unixDay DESC");
    } else {
      return new SQLTable("mail","uuidTarget = ?", List.of(uuid.toString()), "favoritedByTarget DESC, unixDay DESC");
    }
  }

  private static SQLTable getOutbox(UUID uuid, boolean onlyFavorites) {
    if (onlyFavorites) {
      return new SQLTable("mail","uuidSender = ? AND favoritedBySender = ?", List.of(uuid.toString(), true),"favoritedBySender DESC, unixDay DESC");
    } else {
      return new SQLTable("mail","uuidSender = ?", List.of(uuid.toString()),"favoritedBySender DESC, unixDay DESC");
    }
  }
}
