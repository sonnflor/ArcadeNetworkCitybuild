package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MailTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      for (String item : List.of("inbox", "outbox", "send")) {
        if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
          result.add(item);
        }
      }
    }
    return result;
  }
}
