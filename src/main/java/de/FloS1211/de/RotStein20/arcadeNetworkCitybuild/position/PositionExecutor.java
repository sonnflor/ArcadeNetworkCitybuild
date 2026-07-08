package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * PositionExecutor - Command-Handler für Positionsbefehle
 *
 * Diese Klasse implementiert verschiedene Verwaltungsfunktionen für
 * benannte Positionen eines Spielers (Speichern, Löschen, Abrufen,
 * Kompass-Erstellung, Teilen, Auflisten und Export für Minimap-Formate).
 *
 * Die Klasse verwendet die vorhandene DB-Hilfsklasse `SQLTable` und
 * `DatabaseManager.executeSQL(...)` zum Lesen/Schreiben der Tabelle
 * `positions` (erwartetes Schema: uuid,name,x,y,z,world).
 */
public class PositionExecutor implements CommandExecutor {

  private static final NamespacedKey POS_COMPASS = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "pos_compass");

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    // Nur Spieler dürfen diese Befehle ausführen
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
      return true;
    }
    if (args.length < 1) return false;
    String subCommand = args[0];
    if (args.length < 2 && !subCommand.equals("list")) {
      player.sendMessage("§f[§aPosition§f]§7 " + switch (args[0]) {
        case "get" -> "Syntax: /position get <posName>";
        case "add" -> "Syntax: /position set <posName>";
        case "move" -> "Syntax: /position move <posName>";
        case "remove" -> "Syntax: /position remove <posName>";
        case "getCompass" -> "Syntax: /position getCompass <posName>";
        case "share" -> "Syntax: /position share <posName> (<player>)";
        case "list" -> "Syntax: /position list";
        case "addToMinimap" -> "Syntax: /position addToMinimap <posName> <minimap>";
        default -> "Bitte gib als 1. Argument entweder set, get, delete, getCompass oder share an";
      });
      return true;
    }

    if (List.of("get","add","move","remove","share","getCompass","addToMinimap").contains(subCommand)) {
      String posName = args[1];
      if (subCommand.equals("get")) PositionManager.executeSubGet(player,posName);
      else if (subCommand.equals("add")) PositionManager.executeSubAdd(player,posName);
      else if (subCommand.equals("move")) PositionManager.executeSubMove(player,posName);
      else if (subCommand.equals("remove")) PositionManager.executeSubRemove(player,posName);
      else if (subCommand.equals("getCompass")) PositionManager.executeSubCompass(player,posName);
      else {
        if (subCommand.equals("share")) {
          if (args.length == 2) {
            PositionManager.executeSubShare(player,posName,null);
          } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            if (!target.isOnline()) {
              player.sendMessage(MessageManager.get("general-invalid-player"));
              return true;
            }
            PositionManager.executeSubShare(player,posName, target.getPlayer());
          }
        } else if (subCommand.equals("addToMinimap")) {
          if (args.length < 3) return false;
          String minimap = args[2];
          PositionManager.executeSubMinimap(player,posName,minimap);
        }
      }
    } else if (subCommand.equals("list")) {
      PositionManager.executeSubList(player);
    }
  return true;
  }
}
