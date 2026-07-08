package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TpaTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      for (String item : List.of("me_to_player","player_to_me")) {
        if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(item);
        }
      }
    }
    if (args.length == 2) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
          result.add(player.getName());
        }
      }
    }
    return result;
  }
}
