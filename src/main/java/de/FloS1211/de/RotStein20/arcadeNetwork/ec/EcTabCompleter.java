package de.FloS1211.de.RotStein20.arcadeNetwork.ec;

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

public class EcTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
        if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(player.getName());
        }
      }
    }
    if (args.length == 2) {
      if (commandSender instanceof Player player) {
        if (player.isOp()) result.add("edit");
      }
    }
    return result;
  }
}
