package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigSidebarTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      List<String> result = new ArrayList<>();
      for (String item : SidebarManager.features) {
        if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(item);
        }
      }
      return result;
    }
    if (args.length == 2) {
      return List.of("on","off");
    }
    return List.of();
  }
}
