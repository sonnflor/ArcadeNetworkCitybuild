package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.ProxyManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PositionTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
    List<String> result = new ArrayList<>();
    if (strings.length == 1) {
      for (String item : List.of("get", "add", "move","remove", "getCompass", "share", "list", "addToMinimap")) {
        if (item.toLowerCase().startsWith(strings[0].toLowerCase())) {
          result.add(item);
        }
      }
    }
    if (strings.length == 2 && List.of("get","remove","move","getCompass","share","addToMinimap").contains(strings[0])) {
      SQLTable sqlTable = new SQLTable("positions", "uuid=? AND server=?", List.of(((Player) commandSender).getUniqueId().toString(), ProxyManager.getServerName()));
      for (String item : sqlTable.getStringColumn("name")) {
        if (item.toLowerCase().startsWith(strings[1].toLowerCase())) {
          result.add(item);
        }
      }
    }
    if (strings.length == 3 && strings[0].equals("share")) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.getName().toLowerCase().startsWith(strings[2].toLowerCase())) {
          result.add(player.getName());
        }
      }
    }
    if (strings.length == 3 && strings[0].equals("addToMinimap")) {
      for (String item : List.of("XaerosMinimap", "VoxelMap", "JourneyMap")) {
        if (item.toLowerCase().startsWith(strings[2].toLowerCase())) {
          result.add(item);
        }
      }
    }
    return result;
  }
}
