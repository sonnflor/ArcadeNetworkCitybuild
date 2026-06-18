package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
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
      commandSender.sendMessage("§f[§aPosition§f]§7 Dieser Command kann nur als Spieler ausgeführt werden");
      return true;
    }
    if (args.length < 1) return false;
    if (args.length < 2) {
      player.sendMessage("§f[§aPosition§f]§7 " + switch (args[0]) {
        case "get" -> "Syntax: /position get <posName>";
        case "set" -> "Syntax: /position set <posName>";
        case "delete" -> "Syntax: /position delete <posName>";
        case "getCompass" -> "Syntax: /position getCompass <posName>";
        case "share" -> "Syntax: /position share <posName> (<player>)";
        case "list" -> "Syntax: /position list <player>";
        case "addToMinimap" -> "Syntax: /position addToMinimap <posName> <minimap>";
        default -> "Bitte gib als 1. Argument entweder set, get, delete, getCompass oder share an";
      });
      return true;
    }

    // Dispatch auf die jeweilige Unterfunktion
    switch (args[0]) {
      case "set" -> set(player, args[1]);
      case "delete" -> delete(player, args[1]);
      case "get" -> get(player, args[1], player, false);
      case "getCompass" -> getCompass(player, args[1]);
      case "share" -> share(player, args[1], args.length >= 3 ? args[2] : null);
      case "list" -> list(player, args[1]);
      case "addToMinimap" -> addToMinimap(player, args[1], args.length >= 3 ? args[2] : null);
      default -> player.sendMessage(Component.text("[Position] Bitte gib ein gültiges Argument an").color(NamedTextColor.GRAY));
    }
    return true;
  }

  /**
   * Gibt eine gespeicherte Position aus und bietet ggf. administrative Aktionen an.
   *
   * @param player           OfflinePlayer mit der gespeicherten Position (Besitzer)
   * @param positionName     Name der gespeicherten Position
   * @param targetPlayer     Spieler, an den die Ausgabe gesendet wird (meist identisch mit owner)
   * @param isSeenFromAnOtherAdmin Wenn true, wird zusätzlich ein "Location Speichern"-Button angeboten
   */
  private void get (OfflinePlayer player, String positionName, Player targetPlayer, boolean isSeenFromAnOtherAdmin) {
    // Lade Position aus DB (Tabelle 'positions'). Schlüssel: uuid + name
    String uuid = player.getUniqueId().toString();
    SQLTable positionTable = new SQLTable("positions","uuid=? AND name=?", List.of(uuid,positionName));

    // Wenn nicht vorhanden, kurze Fehlermeldung
    if (positionTable.size() == 0) {
      targetPlayer.sendMessage("§f[§aPosition§f]§7 Es gibt keine gespeicherte Position namens " + positionName);
      return;
    }

    // Lese Koordinaten und Welt
    int x = positionTable.getIntValue("x",0);
    int y = positionTable.getIntValue("y",0);
    int z = positionTable.getIntValue("z",0);
    World world = Bukkit.getWorld(positionTable.getStringValue("world",0));

    // Gib die Grunddaten aus
    targetPlayer.sendMessage("§f[§aPosition§f]§7 Position " + positionName + ": ");
    targetPlayer.sendMessage("§7x: " + x);
    targetPlayer.sendMessage("§7y: " + y);
    targetPlayer.sendMessage("§7z: " + z);
    targetPlayer.sendMessage("§7Welt: " + (world != null ? world.getName() : "unknown"));

    // Falls der anfragende Spieler in derselben Welt ist, berechne Distanz
    Location playerLocation = player.getLocation();
    if (world != null && world.equals(playerLocation.getWorld())) {
      int distance = (int) Math.floor(Math.sqrt((Math.pow(x-playerLocation.blockX(), 2)+Math.pow(y-playerLocation.blockY(), 2)+Math.pow(z-playerLocation.blockZ(), 2))));
      targetPlayer.sendMessage("§7Die Entfernung beträgt " + distance + " Blöcke");
      targetPlayer.sendMessage("§7Mit einer Elytra benötigt man ca. " + (Math.floor((double) distance /25/60)+1) + " Minuten für diese Strecke");
    }

    // Wenn der Empfänger Operator ist und im Kreativmodus, biete Teleport-Shortcut als klickbaren Text an
    if (targetPlayer.isOp() && targetPlayer.getGameMode().equals(GameMode.CREATIVE)) {
      Component tp = Component.text("Teleportieren").color(NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("Einfach klicken").color(NamedTextColor.GRAY))).clickEvent(ClickEvent.suggestCommand("/execute in " + (world!=null?world.getKey().asString():"minecraft:overworld") + " run tp " + x + " " + y + " " + z));
      targetPlayer.sendMessage(tp);
    }

    // Falls diese Ansicht von einem Admin für einen fremden Spieler aufgerufen wurde,
    // biete die Möglichkeit, die Location in die eigene Positionsliste zu übernehmen
    if (isSeenFromAnOtherAdmin) {
      Component save = Component.text("Location Speichern").color(NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("Einfach klicken").color(NamedTextColor.GRAY))).clickEvent(ClickEvent.runCommand("/position set " + positionName));
      targetPlayer.sendMessage(save);
    }
  }

  private void delete(Player player, String positionName) {
    // Lösche eine gespeicherte Position des Spielers
    String uuid = player.getUniqueId().toString();
    SQLTable positionTable = new SQLTable("positions","uuid=? AND name=?", List.of(uuid,positionName));
    if (positionTable.size() == 0) {
      player.sendMessage("§f[§aPosition§f]§7 Es gibt keine gespeicherte Position namens " + positionName);
      return;
    }
    DatabaseManager.executeSQL("DELETE FROM positions WHERE uuid = ? AND name=?",List.of(uuid,positionName));
    player.sendMessage("§f[§aPosition§f]§7 Die gespeicherte Position " +positionName + " wurde gelöscht");
  }

  private void set(Player player, String positionName) {
    // Speichert die aktuelle Spielerposition in der Datenbank (INSERT oder UPDATE)
    Location loc = player.getLocation();
    String uuid = player.getUniqueId().toString();
    DatabaseManager.executeSQL("INSERT INTO positions (uuid,name,x,y,z,world) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE x=?,y=?,z=?,world=?",
        List.of(uuid, positionName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
    player.sendMessage("§f[§aPosition§f]§7 Deine Position an x: " + loc.getBlockX() + " y: "+ loc.getBlockY()+" z: "+loc.getBlockZ()+" in der Welt " + loc.getWorld().getName() + " wurde unter dem Namen " + positionName + " gespeichert");
  }

  private void getCompass(Player player, String positionName) {
    // Erzeuge einen Kompass, der auf die gespeicherte Position zeigt
    String uuid = player.getUniqueId().toString();
    SQLTable positionTable = new SQLTable("positions","uuid=? AND name=?", List.of(uuid,positionName));
    if (positionTable.size() == 0) {
      player.sendMessage("§f[§aPosition§f]§7 Es gibt keine gespeicherte Position namens " + positionName);
      return;
    }
    int x = positionTable.getIntValue("x",0);
    int y = positionTable.getIntValue("y",0);
    int z = positionTable.getIntValue("z",0);
    World world = Bukkit.getWorld(positionTable.getStringValue("world",0));
    Location target = new Location(world,x,y,z);

    ItemStack compass = new ItemStack(Material.COMPASS);
    CompassMeta meta = (CompassMeta) compass.getItemMeta();
    if (meta != null) {
      meta.setLodestone(target);
      meta.setLodestoneTracked(false);
      // Setze lesbaren Namen für das Item (Adventures Component API wird verwendet)
      meta.displayName(Component.text("Kompass zu " + positionName).decoration(TextDecoration.ITALIC, false));
      // Markiere das Item per PersistentData, damit Plugins es erkennen können
      meta.getPersistentDataContainer().set(POS_COMPASS, PersistentDataType.BOOLEAN,true);

      compass.setItemMeta(meta);
      player.getInventory().addItem(compass);
    }
  }

  private void share(Player player, String positionName, String targetPlayerName) {
    // Teile eine gespeicherte Position mit allen Spielern oder mit einem bestimmten Spieler
    String uuid = player.getUniqueId().toString();
    SQLTable positionTable = new SQLTable("positions","uuid=? AND name=?", List.of(uuid,positionName));
    if (positionTable.size() == 0) {
      player.sendMessage("§f[§aPosition§f]§7 Es gibt keine gespeicherte Position namens " + positionName);
      return;
    }
    List<Player> targetPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
    if (targetPlayerName !=null) {
      Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
      if (targetPlayer == null) {
        player.sendMessage("Bitte gib einen gültigen Spieler an");
        return;
      }
      targetPlayers = List.of(targetPlayer);
    }

    // Erzeuge eine klickbare Nachricht, die den Empfänger die Position übernehmen lässt
    Component message = LegacyComponentSerializer.legacySection().deserialize("§f[§aPosition§f]§7 Der Spieler " + player.getName() + " möchte seine gespeicherte Position ")
        .append(Component.text(positionName).color(NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("Klicken zum Hinzufügen"))).clickEvent(ClickEvent.runCommand("/position set " + positionName)))
        .append(Component.text(" mit dir Teilen").color(NamedTextColor.GRAY));
    for (Player targetPlayer : targetPlayers) {
      targetPlayer.sendMessage(message);
    }
  }

  private void list(Player player, String targetPlayerName) {
    // Admin-only: listet alle gespeicherten Positionen eines Ziel-Spielers
    if (!player.isOp()) {
      player.sendMessage("Dieser Command ist nur für Admins");
      return;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);
    if (!targetPlayer.hasPlayedBefore() && !player.isOp()) {
      player.sendMessage("Bitte gib einen gültigen Spieler an");
      return;
    }
    String uuid = targetPlayer.getUniqueId().toString();
    SQLTable sqlTable = new SQLTable("positions", "uuid=?", List.of(uuid));
    Component msg = Component.text("§f[§aPosition§f]§7 Alle Positions von " + targetPlayerName + " sind: ");
    Component comma = Component.text(", ").color(NamedTextColor.GRAY);
    for (int i = 0; i < sqlTable.size(); i++) {
      String name = sqlTable.getStringValue("name",i);
      // Jedes Listenelement ist klickbar und ruft /position get <name> auf
      msg = msg.append(Component.text(name).color(NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("Klicken, um es anzuschauen"))).clickEvent(ClickEvent.runCommand("/position get " + name))).append(comma);
    }
    player.sendMessage(msg);
  }

  private void addToMinimap(Player player, String  positionName, String minimapType) {
    // Exportiert eine Position in verschiedene Minimap-Formate (nützlich für Client-Plugins)
    if (minimapType == null) {
      player.sendMessage("Bitte gib einen Minimap-Typ an");
      return;
    }
    String uuid = player.getUniqueId().toString();
    SQLTable positionTable = new SQLTable("positions","uuid=? AND name=?", List.of(uuid,positionName));
    int x = positionTable.getIntValue("x",0);
    int y = positionTable.getIntValue("y",0);
    int z = positionTable.getIntValue("z",0);
    World world = Bukkit.getWorld(positionTable.getStringValue("world",0));
    String msg = switch (minimapType) {
      case "XaerosMinimap" -> "xaero-waypoint:" + positionName.replaceAll(":","") + ":" + positionName.substring(0,1).toUpperCase() + ":" + x + ":" + y + ":" + z + ":" + ThreadLocalRandom.current().nextInt(0, 17) + ":false:0:Internal-" + world.getKey().asString().substring(world.getKey().asString().indexOf(':') + 1);
      case "VoxelMap" -> "[name:" + positionName.replaceAll(",","﹐").replaceAll("]","⟧").replaceAll("\\[","⟦") + ", x:" + x + ", y:" + y + ", z" + z + ", dim:" + world.getKey().asString() + ", icon:fish";
      case "JourneyMap" -> "[name:\"" + positionName.replaceAll("\""," ") + "\", x:" + x + ", y:" + y + ", z:" + z + ", dim:" + world.getKey().asString() + "]";
      default -> "Bitte gib einen gültigen Minimap-Typ an";
    };
    player.sendMessage(msg);
  }
}
