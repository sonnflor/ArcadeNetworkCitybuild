package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class NamecolorManager {
  public static LinkedHashMap<String,Namecolor> namecolors = new LinkedHashMap<>(){
    {
      put("red", new Namecolor("Rot", NamedTextColor.RED, NamedTextColor.RED, Material.RED_DYE, "solid"));
      put("orange", new Namecolor("Orange", NamedTextColor.GOLD, NamedTextColor.GOLD, Material.ORANGE_DYE, "solid"));
      put("yellow", new Namecolor("Gelb", NamedTextColor.YELLOW, NamedTextColor.YELLOW, Material.YELLOW_DYE, "solid"));
      put("green", new Namecolor("Grün", NamedTextColor.GREEN, NamedTextColor.GREEN, Material.LIME_DYE, "solid"));
      put("darkgreen", new Namecolor("Dunkelgrün", NamedTextColor.DARK_GREEN, NamedTextColor.DARK_GREEN, Material.GREEN_DYE, "solid"));
      put("cyan", new Namecolor("Türkis", NamedTextColor.DARK_AQUA, NamedTextColor.DARK_AQUA, Material.CYAN_DYE, "solid"));
      put("blue", new Namecolor("Blau", NamedTextColor.BLUE, NamedTextColor.BLUE, Material.BLUE_DYE, "solid"));
      put("purple", new Namecolor("Lila", NamedTextColor.DARK_PURPLE, NamedTextColor.DARK_PURPLE, Material.PURPLE_DYE, "solid"));
      put("magenta", new Namecolor("Magenta", NamedTextColor.LIGHT_PURPLE, NamedTextColor.LIGHT_PURPLE, Material.MAGENTA_DYE, "solid"));
      put("white", new Namecolor("Weiß", NamedTextColor.WHITE, NamedTextColor.WHITE, Material.WHITE_DYE, "solid"));
      put("gray", new Namecolor("Grau", NamedTextColor.DARK_GRAY, NamedTextColor.DARK_GRAY, Material.GRAY_DYE, "solid"));
      put("sea", new Namecolor("Meer", NamedTextColor.DARK_BLUE, NamedTextColor.BLUE, Material.WATER_BUCKET, "gradient"));
      put("redstone", new Namecolor("Redstone", NamedTextColor.RED, NamedTextColor.DARK_RED, Material.REDSTONE, "gradient"));
      put("lava", new Namecolor("Lava", NamedTextColor.RED, NamedTextColor.GOLD, Material.LAVA_BUCKET, "gradient"));
      put("forest", new Namecolor("Wald", NamedTextColor.GREEN, NamedTextColor.DARK_GREEN, Material.OAK_SAPLING, "gradient"));
      put("chess", new Namecolor("Schach", NamedTextColor.BLACK, NamedTextColor.WHITE, Material.DAYLIGHT_DETECTOR, "alternate1"));
      put("space", new Namecolor("Space", NamedTextColor.DARK_PURPLE, NamedTextColor.BLACK, Material.SCULK, "gradient"));
      put("construction", new Namecolor("Baustelle", NamedTextColor.YELLOW, NamedTextColor.BLACK, Material.DEEPSLATE_GOLD_ORE, "alternate2"));
      put("diamond", new Namecolor("Diamant", NamedTextColor.DARK_AQUA, NamedTextColor.AQUA, Material.DIAMOND, "gradient"));
      put("enchanted", new Namecolor("Verzaubert", NamedTextColor.LIGHT_PURPLE, NamedTextColor.DARK_PURPLE, Material.ENCHANTING_TABLE, "gradient"));
      put("stone", new Namecolor("Stein", NamedTextColor.GRAY, NamedTextColor.DARK_GRAY, Material.STONE, "gradient"));
      put("cobblestone", new Namecolor("Bruchstein", NamedTextColor.GRAY, NamedTextColor.DARK_GRAY, Material.COBBLESTONE, "alternate2"));
      put("discord", new Namecolor("Discord", NamedTextColor.BLUE, NamedTextColor.WHITE, Material.REPEATING_COMMAND_BLOCK, "alternate2"));
      put("gold", new Namecolor("Gold", NamedTextColor.GOLD, NamedTextColor.YELLOW, Material.GOLD_INGOT, "gradient"));
      put("hacker", new Namecolor("Gacker", NamedTextColor.BLACK, NamedTextColor.GREEN, Material.COMMAND_BLOCK, "special0010110101011011"));
      put("candycane", new Namecolor("Zuckerstange", NamedTextColor.RED, NamedTextColor.WHITE, Material.FIREWORK_ROCKET, "alternate1"));
      put("sky", new Namecolor("Himmel", NamedTextColor.AQUA, NamedTextColor.WHITE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, "gradient"));
      put("flamingo", new Namecolor("Flamingo", NamedTextColor.LIGHT_PURPLE, NamedTextColor.WHITE, Material.MAGENTA_CARPET, "gradient"));
      put("iron", new Namecolor("Eisen", NamedTextColor.GRAY, NamedTextColor.WHITE, Material.IRON_INGOT, "gradient"));
      put("copper", new Namecolor("Kupfer", NamedTextColor.GOLD, NamedTextColor.DARK_AQUA, Material.COPPER_INGOT, "gradient"));
      put("sun", new Namecolor("Sonne", NamedTextColor.YELLOW, NamedTextColor.WHITE, Material.SUNFLOWER, "gradient"));
      put("duck", new Namecolor("Ente", NamedTextColor.DARK_GREEN, NamedTextColor.GRAY, Material.SUNFLOWER, "gradient"));
      put("twitch", new Namecolor("Twitch", NamedTextColor.DARK_PURPLE, NamedTextColor.WHITE, Material.REPEATING_COMMAND_BLOCK, "alternate2"));
      put("default", new Namecolor("default", NamedTextColor.GRAY, NamedTextColor.GRAY, Material.STRUCTURE_VOID, "solid"));
    }};

  public static void setNamecolor(String uuid, String namecolor) {
    DatabaseManager.executeSQL("INSERT INTO player_data (uuid,namecolor) VALUES (?,?) ON DUPLICATE KEY UPDATE namecolor = ?", java.util.List.of(uuid, namecolor, namecolor));
    PlayerNameManager.updatePlayerName(uuid);
  }

  public static void unlockNamecolor(String uuid, String namecolor) {
    List<String> unlocked = new ArrayList<>(getUnlockedNamecolors(uuid));
    if (!unlocked.contains(namecolor)) {
      unlocked.add(namecolor);
    }
    String result = String.join(",",unlocked);
    DatabaseManager.executeSQL("INSERT INTO player_data (uuid,unlocked_namecolors) VALUES (?,?) ON DUPLICATE KEY UPDATE unlocked_namecolors = ?", java.util.List.of(uuid, result, result));
  }

  public static String getActiveNamecolor(String uuid) {
    SQLTable playerData = new SQLTable("player_data", "uuid = ?", List.of(uuid));
    if (!playerData.isEmpty()) {
      return playerData.getStringValue("namecolor",0);
    } else {
      return "default";
    }
  }

  public static List<String> getUnlockedNamecolors(String uuid) {
    SQLTable playerData = new SQLTable("player_data", "uuid = ?", List.of(uuid));
    if (!playerData.isEmpty()) {
      String unlockedColorListString = playerData.getStringValue("unlocked_namecolors",0);
      return List.of(unlockedColorListString.split(","));
    } else {
      return List.of();
    }
  }

  public static Component getGradient(String name, Namecolor col, boolean isSmooth) {
    NamedTextColor col1 = col.col1();
    NamedTextColor col2 = col.col2();
    Component result = Component.text("");
    if (isSmooth) {
      int r1 = col1.red();
      int g1 = col1.green();
      int b1 = col1.blue();

      int r2 = col2.red();
      int g2 = col2.green();
      int b2 = col2.blue();

      for (int i = 0; i < name.length(); i++) {
        double t = (double) i / (double) (name.length() - 1);

        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);

        result = result.append(Component.text(name.charAt(i)).color(TextColor.color(r, g, b)));
      }
    } else {
      String part1 = name.substring(0,name.length()/2);
      String part2 = name.replaceAll(part1,"");
      result = Component.text(part1).color(col1).append(Component.text(part2).color(col2));
    }
    return result;
  }

  public static Component getColoredName(String colName,String name, boolean isSmooth) {
    Namecolor col = namecolors.get(colName);
    Component result;
    switch (col.type().replaceAll("\\d","")) {
      case "solid" -> {
        result = Component.text(name).color(col.col1());
      }
      case "gradient" -> {
        result = getGradient(name,col,isSmooth);
      }
      case "alternate" -> {
        result = Component.text("");
        int sequenceLength = Integer.parseInt(col.type().replaceAll("\\D",""));
        for (int i = 0; i < name.length(); i++) {
          boolean firstColor = ((i / sequenceLength) % 2 == 0);

          result = result.append(
              Component.text(name.substring(i, i + 1))
                  .color(firstColor ? col.col1() : col.col2())
          );
        }
      }
      case "special" -> {
        result = Component.text("");
        String sequence = col.type().replaceAll("\\D","");
        if (sequence.length() != 16) Bukkit.getLogger().warning("[Namensfarben] Special-sequence from " + col.name() + " is not 16 chars long");
        for (int i = 0; i < name.length(); i++) {
          result = result.append(Component.text(name.charAt(i)).color(sequence.charAt(i%sequence.length()) == '0' ? col.col1() : col.col2()));
        }
      }
      case null, default -> result = Component.text(name).color(NamedTextColor.GRAY);
    }
    return result;
  }

  public static String getColoredNameAsString(String colName,String name) {
    return LegacyComponentSerializer.legacySection().serialize(getColoredName(colName,name,false));
  }

  public static Gui getNamecolorGUI(Player player) {
    String uuid = player.getUniqueId().toString();
    Gui gui = new Gui(54, Component.text("Namensfarben"));
    SequencedCollection<Namecolor> namecolorValues = NamecolorManager.namecolors.sequencedValues();
    namecolorValues.removeLast();
    List<String> unlockedNamecolors = NamecolorManager.getUnlockedNamecolors(uuid);
    for (Namecolor namecolor : namecolorValues) {
      if (unlockedNamecolors.contains(namecolor.name())) {
        gui.addElement(new GuiButton(namecolor.name(), namecolor.item(), new NamecolorGuiButtonExecutor(), GuiButtonType.CUSTOM)
            .title(Component.text("Namensfarbe").color(NamedTextColor.GREEN).append(Component.text(" • ").color(NamedTextColor.GRAY).append(NamecolorManager.getColoredName(namecolor.name(),namecolor.name(),true))))
            .lore(List.of(Component.text("Klicke, um die Namensfarbe zu aktivieren").color(NamedTextColor.GRAY)))
            .sound(GuiSound.SUCCESS));
      } else {
        gui.addElement(new GuiDisplay(namecolor.name(), Material.BARRIER)
            .title(Component.text("Namensfarbe").color(NamedTextColor.RED).append(Component.text(" • ").color(NamedTextColor.RED).append(NamecolorManager.getColoredName(namecolors.entrySet().stream().filter(entry -> entry.getValue().equals(namecolor)).map(Map.Entry::getKey).findFirst().orElse(null),namecolor.name(),true))))
            .lore(List.of(
                Component.text("Du hast diese Namensfarbe noch nicht freigeschaltet.")
                    .color(NamedTextColor.RED),
                Component.text("Du kannst Namensfarben über Crates erhalten. ")
                    .color(NamedTextColor.RED))));
      }
    }
    gui.setElement(new GuiButton("default",Material.STRUCTURE_VOID,new NamecolorGuiButtonExecutor(),GuiButtonType.CUSTOM)
        .title(Component.text("Namensfarbe deaktivieren").color(NamedTextColor.GRAY))
        .lore(List.of(Component.text("Klicke, um deine Namensfarbe zu deaktivieren").color(NamedTextColor.GRAY))),0,40);
    return gui;
  }
}
