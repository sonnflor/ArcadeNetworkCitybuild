package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FriendsTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      for (String item : List.of("show","invite","delete")) {
        if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(item);
        }
      }
    } else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("delete")) {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
          if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
            result.add(player.getName());
          }
        }
      }
    }

    return result;
  }
}
