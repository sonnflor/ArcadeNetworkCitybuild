package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamecolorManager {
  public static List<String> namecolorNames = List.of("red","orange", "yellow", "green", "darkgreen", "cyan", "blue", "purple", "magenta","white", "gray", "sea", "redstone", "lava","forest", "chess", "space", "constuction", "diamond", "enchanted","stone", "cobblestone", "discord", "gold", "hacker", "candycane","sky","flamingo", "iron", "copper","sun", "duck","twitch");
  public static Map<String,Namecolor> namecolors = Map.ofEntries(
      Map.entry("red",new Namecolor("red", NamedTextColor.RED,NamedTextColor.RED, Material.RED_DYE,"solid")),
      Map.entry("orange", new Namecolor("orange", NamedTextColor.GOLD,NamedTextColor.GOLD, Material.RED_DYE,"solid")),
      Map.entry("yellow", new Namecolor("yellow", NamedTextColor.YELLOW,NamedTextColor.YELLOW, Material.RED_DYE,"solid")),
      Map.entry("green", new Namecolor("green", NamedTextColor.GREEN,NamedTextColor.GREEN, Material.RED_DYE,"solid")),
      Map.entry("darkgreen", new Namecolor("darkgreen", NamedTextColor.DARK_GREEN,NamedTextColor.DARK_GREEN, Material.RED_DYE,"solid")),
      Map.entry("cyan", new Namecolor("cyan", NamedTextColor.DARK_AQUA,NamedTextColor.DARK_AQUA, Material.RED_DYE,"solid")),
      Map.entry("blue", new Namecolor("blue", NamedTextColor.BLUE,NamedTextColor.BLUE, Material.RED_DYE,"solid")),
      Map.entry("purple", new Namecolor("purple", NamedTextColor.DARK_PURPLE,NamedTextColor.DARK_PURPLE, Material.RED_DYE,"solid")),
      Map.entry("magenta", new Namecolor("magenta", NamedTextColor.LIGHT_PURPLE,NamedTextColor.LIGHT_PURPLE, Material.RED_DYE,"solid")),
      Map.entry("white", new Namecolor("white", NamedTextColor.WHITE,NamedTextColor.WHITE, Material.RED_DYE,"solid")),
      Map.entry("gray", new Namecolor("gray", NamedTextColor.DARK_GRAY,NamedTextColor.DARK_GRAY, Material.RED_DYE,"solid")),
      Map.entry("sea", new Namecolor("sea",NamedTextColor.DARK_BLUE,NamedTextColor.BLUE,Material.WATER_BUCKET,"gradient")),
      Map.entry("redstone", new Namecolor("redstone",NamedTextColor.RED,NamedTextColor.DARK_RED,Material.REDSTONE,"gradient")),
      Map.entry("lava", new Namecolor("lava",NamedTextColor.RED,NamedTextColor.GOLD,Material.LAVA_BUCKET,"gradient")),
      Map.entry("forest", new Namecolor("forest",NamedTextColor.GREEN,NamedTextColor.DARK_GREEN,Material.OAK_SAPLING,"gradient")),
      Map.entry("chess", new Namecolor("chess",NamedTextColor.BLACK,NamedTextColor.WHITE,Material.DAYLIGHT_DETECTOR,"alternate1")),
      Map.entry("space", new Namecolor("space",NamedTextColor.DARK_PURPLE,NamedTextColor.BLACK,Material.SCULK,"gradient")),
      Map.entry("constuction", new Namecolor("constuction",NamedTextColor.YELLOW,NamedTextColor.BLACK,Material.DEEPSLATE_GOLD_ORE,"alternate2")),
      Map.entry("diamond", new Namecolor("diamond",NamedTextColor.DARK_AQUA,NamedTextColor.AQUA,Material.DIAMOND,"gradient")),
      Map.entry("enchanted", new Namecolor("enchanted",NamedTextColor.LIGHT_PURPLE,NamedTextColor.DARK_PURPLE,Material.ENCHANTING_TABLE,"gradient")),
      Map.entry("stone", new Namecolor("stone",NamedTextColor.GRAY,NamedTextColor.DARK_GRAY,Material.STONE,"gradient")),
      Map.entry("cobblestone", new Namecolor("cobblestone",NamedTextColor.GRAY,NamedTextColor.DARK_GRAY,Material.COBBLESTONE,"alternate2")),
      Map.entry("discord", new Namecolor("discord",NamedTextColor.BLUE,NamedTextColor.WHITE,Material.REPEATING_COMMAND_BLOCK,"alternate2")),
      Map.entry("gold", new Namecolor("gold",NamedTextColor.GOLD,NamedTextColor.YELLOW,Material.GOLD_INGOT,"gradient")),
      Map.entry("hacker", new Namecolor("hacker",NamedTextColor.BLACK,NamedTextColor.GREEN,Material.COMMAND_BLOCK,"special0010110101011011")),
      Map.entry("candycane", new Namecolor("candycane",NamedTextColor.RED,NamedTextColor.WHITE,Material.FIREWORK_ROCKET,"alternate1")),
      Map.entry("sky", new Namecolor("sky",NamedTextColor.AQUA,NamedTextColor.WHITE,Material.LIGHT_BLUE_STAINED_GLASS_PANE,"gradient")),
      Map.entry("flamingo", new Namecolor("flamingo",NamedTextColor.LIGHT_PURPLE,NamedTextColor.WHITE,Material.MAGENTA_CARPET,"gradient")),
      Map.entry("iron", new Namecolor("iron",NamedTextColor.GRAY,NamedTextColor.WHITE,Material.IRON_INGOT,"gradient")),
      Map.entry("copper", new Namecolor("copper",NamedTextColor.GOLD,NamedTextColor.DARK_AQUA,Material.COPPER_INGOT,"gradient")),
      Map.entry("sun", new Namecolor("sun",NamedTextColor.YELLOW,NamedTextColor.WHITE,Material.SUNFLOWER,"gradient")),
      Map.entry("duck", new Namecolor("duck",NamedTextColor.DARK_GREEN,NamedTextColor.GRAY,Material.SUNFLOWER,"gradient")),
      Map.entry("twitch", new Namecolor("twitch",NamedTextColor.DARK_PURPLE,NamedTextColor.WHITE,Material.REPEATING_COMMAND_BLOCK,"alternate2")),
      Map.entry("default", new Namecolor("default",NamedTextColor.GRAY,NamedTextColor.GRAY,Material.STRUCTURE_VOID,"solid"))
  );

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
    NamedTextColor col1 = col.getCol1();
    NamedTextColor col2 = col.getCol2();
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
    switch (col.getType().replaceAll("\\d","")) {
      case "solid" -> {
        result = Component.text(name).color(col.getCol1());
      }
      case "gradient" -> {
        result = getGradient(name,col,isSmooth);
      }
      case "alternate" -> {
        result = Component.text("");
        int sequenceLength = Integer.parseInt(col.getType().replaceAll("\\D",""));
        for (int i = 0; i < name.length(); i++) {
          boolean firstColor = ((i / sequenceLength) % 2 == 0);

          result = result.append(
              Component.text(name.substring(i, i + 1))
                  .color(firstColor ? col.getCol1() : col.getCol2())
          );
        }
      }
      case "special" -> {
        result = Component.text("");
        String sequence = col.getType().replaceAll("\\D","");
        if (sequence.length() != 16) Bukkit.getLogger().warning("[Namensfarben] Special-sequence from " + col.getName() + " is not 16 chars long");
        for (int i = 0; i < name.length(); i++) {
          result = result.append(Component.text(name.charAt(i)).color(sequence.charAt(i%sequence.length()) == '0' ? col.getCol1() : col.getCol2()));
        }
      }
      case null, default -> result = Component.text(name).color(NamedTextColor.GRAY);
    }
    return result;
  }

  public static String getColoredNameAsString(String colName,String name) {
    return LegacyComponentSerializer.legacySection().serialize(getColoredName(colName,name,false));
  }
}
