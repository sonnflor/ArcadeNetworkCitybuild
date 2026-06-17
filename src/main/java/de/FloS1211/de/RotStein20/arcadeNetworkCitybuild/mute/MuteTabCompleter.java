package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MuteTabCompleter implements TabCompleter {
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
    if (args.length >= 2) {
      result.add("Wirbt für sich");
      result.add("Beleidigt andere");
      result.add("Spammt");
      result.add("einfach so halt");
    }
    return result;
  }
}
