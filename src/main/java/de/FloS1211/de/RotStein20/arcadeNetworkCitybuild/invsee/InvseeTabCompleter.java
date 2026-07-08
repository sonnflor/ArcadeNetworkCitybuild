package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee;

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

public class InvseeTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
    List<String> result = new ArrayList<>();
    if (strings.length == 1) {
      for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
        if (player.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
          result.add(player.getName());
        }
      }
    }
    if (strings.length == 2) {
      if ((commandSender instanceof Player player) && player.hasPermission("ac.admin")) {
        for (String item : List.of("edit","watch")) {
          if (item.toLowerCase().startsWith(strings[1].toLowerCase())) {
            result.add(item);
          }
        }
      }
    }
    return result;
  }
}
