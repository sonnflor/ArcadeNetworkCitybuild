package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
    List<String> result = new ArrayList<>();
    if (strings.length == 1) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
          result.add(player.getName());
        }
      }
    }
    return result;
  }
}
