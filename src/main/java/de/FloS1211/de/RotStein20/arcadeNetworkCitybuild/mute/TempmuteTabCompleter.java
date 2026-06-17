package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TempmuteTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      for (int i = 0; i < Bukkit.getOfflinePlayers().length; i++) {
        if (Bukkit.getOfflinePlayers()[i].getName().toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(Bukkit.getOfflinePlayers()[i].getName());
        }
      }
    }
    if (args.length == 2) {
      result.add("0");
      result.add("1");
      result.add("7");
      result.add("30");
    }
    if (args.length == 3) {
      result.add("0");
      result.add("1");
      result.add("2");
      result.add("12");
    }
    if (args.length == 4) {
      result.add("0");
      result.add("15");
      result.add("30");
      result.add("45");
    }
    if (args.length > 5) {
      result.add("Wirbt für sich");
      result.add("Beleidigt andere");
      result.add("Spammt");
      result.add("einfach so halt");
    }
    return result;
  }
}
