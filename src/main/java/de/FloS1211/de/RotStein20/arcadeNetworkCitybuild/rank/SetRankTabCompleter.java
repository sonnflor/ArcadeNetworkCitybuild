package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetRankTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
        if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(player.getName());
        }
      }
    } else if (args.length == 2) {
      result.add("rank");
      result.add("extrarank");
      result.add("clear");
    } else if (args.length == 3) {
      if (args[1].equalsIgnoreCase("rank")) {
        result.add("crafter");
        result.add("expert");
        result.add("elite");
        result.add("king");
        result.add("master");
        result.add("admin");
        result.add("owner");
      } else if (args[1].equalsIgnoreCase("extrarank")) {
        result.add("builder");
        result.add("creator");
        result.add("supporter");
        result.add("testsupporter");
        result.add("moderator");
        result.add("testmoderator");
        result.add("developer");
      }
    }
    return result;
  }
}
