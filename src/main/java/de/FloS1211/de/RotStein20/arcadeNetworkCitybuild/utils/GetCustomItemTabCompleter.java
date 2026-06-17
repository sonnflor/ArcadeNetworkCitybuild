package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GetCustomItemTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      List<String> result = new ArrayList<>();
      for (String item : GetCustomItemExecutor.validItems) {
        if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(item);
        }
      }
      return result;
    } else {
      return List.of();
    }
  }
}
