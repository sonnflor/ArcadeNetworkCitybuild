package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.ProxyManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PositionManager {
  public static void executeSubGet(Player player, String posName) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions","uuid = ? AND name=? AND server=?", List.of(uuid,posName, ProxyManager.getServerName()));
    if (positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-invalid-name"));
      return;
    }
    double x = positions.getDoubleValue("x",0);
    double y = positions.getDoubleValue("y",0);
    double z = positions.getDoubleValue("z",0);
    String dim = positions.getStringValue("dim",0);

    player.sendMessage(MessageManager.get("pos-get-head",Map.of("pos",posName)));
    player.sendMessage(Component.text("x: " + String.format("%.2f",x)).color(NamedTextColor.GRAY));
    player.sendMessage(Component.text("y: " + String.format("%.2f",y)).color(NamedTextColor.GRAY));
    player.sendMessage(Component.text("z: " + String.format("%.2f",z)).color(NamedTextColor.GRAY));
    player.sendMessage(Component.text("Dimension: " + dim).color(NamedTextColor.GRAY));
    Location playerLoc = player.getLocation();
    if (playerLoc.getWorld().getKey().getKey().equals(dim)) {
      int distance = (int) Math.floor(Math.sqrt((Math.pow(x-playerLoc.getBlockX(), 2)+Math.pow(y-playerLoc.getBlockY(), 2)+Math.pow(z-playerLoc.getBlockZ(), 2))));
      player.sendMessage(Component.text("Entfernung: " + distance + "m").color(NamedTextColor.GRAY));
      player.sendMessage(Component.text("Mit einer Elytra benötigt man ca. " + (Math.floor((double) distance /25/60)+1) + " Minuten für diese Strecke").color(NamedTextColor.GRAY));
    }
    if (player.hasPermission("ac.admin")) {
      player.sendMessage(Component.text("[Teleportieren]").color(NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("Klicke zum Teleportieren").color(NamedTextColor.GRAY))).clickEvent(ClickEvent.runCommand("execute in minecraft:"+dim+" run tp @s "+x+" "+y+" "+z)));
    }
  }
  public static void executeSubRemove(Player player, String posName) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions","uuid = ? AND name=?", List.of(uuid,posName));
    if (positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-invalid-name"));
      return;
    }
    DatabaseManager.executeSQL("DELETE FROM positions WHERE uuid = ? AND name=? AND server=?",List.of(uuid,posName,ProxyManager.getServerName()));
    player.sendMessage(MessageManager.get("pos-remove-succeed", Map.of("pos",posName)));
  }
  public static void executeSubAdd(Player player, String posName) {
    Location loc = player.getLocation();
    addPos(player, loc.getX(), loc.y(), loc.getZ(), loc.getWorld().getKey().getKey(), posName);
  }
  public static void addPos(Player player, double x, double y, double z, String dim, String posName) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions","uuid = ? AND name=? AND server=?", List.of(uuid,posName,ProxyManager.getServerName()));
    if (!positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-add-already-existing"));
      return;
    }
    if (DatabaseManager.getTable("positions","uuid=?",List.of(uuid)).size() >= 100) {
      player.sendMessage(MessageManager.get("pos-add-too-many"));
      return;
    }
    DatabaseManager.executeSQL("INSERT INTO positions (uuid,name,x,y,z,dim,server) VALUES (?,?,?,?,?,?,?)",
        List.of(uuid, posName, x, y, z, dim, ProxyManager.getServerName()));
    player.sendMessage(MessageManager.get("pos-add-succeed",Map.of("pos",posName)));
  }
  public static void executeSubMove(Player player, String posName) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions","uuid = ? AND name=? AND server=?", List.of(uuid,posName,ProxyManager.getServerName()));
    if (positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-invalid-name"));
      return;
    }
    Location loc = player.getLocation();
    DatabaseManager.executeSQL("UPDATE positions SET x=?,y=?,z=?,dim=? WHERE uuid = ? AND name = ? AND server = ?",
        List.of(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getKey().getKey(),uuid, posName,ProxyManager.getServerName()));
    player.sendMessage(MessageManager.get("pos-move-succeed",Map.of("pos",posName)));
  }
  public static void executeSubCompass(Player player, String posName) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions","uuid = ? AND name=? AND server=?", List.of(uuid,posName,ProxyManager.getServerName()));
    if (positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-invalid-name"));
      return;
    }
    Location target = new Location(Bukkit.getWorld(NamespacedKey.fromString((positions.getStringValue("dim",0)))),positions.getDoubleValue("x",0),positions.getDoubleValue("y",0),positions.getDoubleValue("z",0));
    ItemStack compass = new ItemStack(Material.COMPASS);
    CompassMeta meta = (CompassMeta) compass.getItemMeta();
    if (meta != null) {
      meta.setLodestone(target);
      meta.setLodestoneTracked(false);
      // Setze lesbaren Namen für das Item (Adventures Component API wird verwendet)
      meta.displayName(Component.text("Kompass zu " + posName).decoration(TextDecoration.ITALIC, false));
      // Markiere das Item per PersistentData, damit Plugins es erkennen können
      meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING,"custom_compass");

      compass.setItemMeta(meta);
      player.getInventory().addItem(compass);
    }
  }

  public static void executeSubList(Player player) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions","uuid=? AND server = ?",List.of(uuid,ProxyManager.getServerName()));
    if (positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-list-no-pos"));
      return;
    }
    player.sendMessage(MessageManager.get("pos-list-head"));
    for (String posName : positions.getStringColumn("name")) {
      player.sendMessage(Component.text("["+posName+"]").color(NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("Klicke, um Informationen zu erhalten").color(NamedTextColor.GRAY))).clickEvent(ClickEvent.suggestCommand("/position get " + posName)));
    }
  }

  public static void executeSubShare(Player player, String posName, Player targetPlayer) {
    String uuid = player.getUniqueId().toString();
    SQLTable positions = DatabaseManager.getTable("positions", "uuid = ? AND name=?", List.of(uuid, posName));
    if (positions.isEmpty()) {
      player.sendMessage(MessageManager.get("pos-invalid-name"));
      return;
    }
    String token = ClickableMessageManager.registerClickableMessage("pos", false, List.of(uuid, posName));
    Component targetMessage = MessageManager.get("pos-share-target", Map.of("name", posName, "token", token, "player", player.getName()));
    if (targetPlayer == null) {
      for (Player target : Bukkit.getOnlinePlayers()) {
        target.sendMessage(targetMessage);
      }
    } else {
      targetPlayer.sendMessage(targetMessage);
    }
    player.sendMessage(MessageManager.get("pos-share-sender", Map.of("name", posName, "targetPlayer", targetPlayer == null ? "allen" : targetPlayer.getName())));
  }

  public static void executeSubMinimap(Player player, String  posName, String minimapType) {
    if (minimapType == null) {
      player.sendMessage(MessageManager.get("pos-minimap-invalied-type"));
      return;
    }
    String uuid = player.getUniqueId().toString();
    SQLTable positionTable = new SQLTable("positions","uuid=? AND name=? AND server=?", List.of(uuid,posName, ProxyManager.getServerName()));
    int x = (int) positionTable.getDoubleValue("x",0);
    int y = (int) positionTable.getDoubleValue("y",0);
    int z = (int) positionTable.getDoubleValue("z",0);
    World world = Bukkit.getWorld(NamespacedKey.fromString(positionTable.getStringValue("dim",0)));
    String msg = switch (minimapType) {
      case "XaerosMinimap" -> "xaero-waypoint:" + posName.replaceAll(":","") + ":" + posName.substring(0,1).toUpperCase() + ":" + x + ":" + y + ":" + z + ":" + ThreadLocalRandom.current().nextInt(0, 17) + ":false:0:Internal-" + world.getKey().asString().substring(world.getKey().asString().indexOf(':') + 1);
      case "VoxelMap" -> "[name:" + posName.replaceAll(",","﹐").replaceAll("]","⟧").replaceAll("\\[","⟦") + ", x:" + x + ", y:" + y + ", z" + z + ", dim:" + world.getKey().asString() + ", icon:fish";
      case "JourneyMap" -> "[name:\"" + posName.replaceAll("\""," ") + "\", x:" + x + ", y:" + y + ", z:" + z + ", dim:" + world.getKey().asString() + "]";
      default -> LegacyComponentSerializer.legacySection().serialize(MessageManager.get("pos-minimap-invalied-type"));
    };
    player.sendMessage(msg);
  }

  public static void handleShareMessageClick(String[] args, CommandSender commandSender) {
    String uuid = args[0];
    String posName = args[1];
    SQLTable positions = DatabaseManager.getTable("positions", "uuid = ? AND name=?, server=?", List.of(uuid, posName, ProxyManager.getServerName()));
    double x = positions.getDoubleValue("x",0);
    double y = positions.getDoubleValue("y",0);
    double z = positions.getDoubleValue("z",0);
    String dim = positions.getStringValue("dim",0);
    addPos((Player) commandSender,x,y,z,dim,posName);
  }
}
