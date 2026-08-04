package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.remoteConsole;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class FastPollingExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length == 0) {
      commandSender.sendMessage(Component.text("FastPolling: " + (CommandPoller.fastPollingUntil == 0 ? "§cfalse" : "§atrue")));
      commandSender.sendMessage(Component.text("PollingSpeed: 1 request every " + (CommandPoller.fastPollingUntil == 0 ? "2 minutes" : "5 seconds")));
    }
    if (args.length == 1) {
      long fastPollingTimeSpan = Long.parseLong(args[0]);
      if (fastPollingTimeSpan < 0 || fastPollingTimeSpan > 1200) {
        commandSender.sendMessage(fastPollingTimeSpan + " is an invalid timespan. ");
        commandSender.sendMessage("Please enter a time span between 0 and 1200 seconds. ");
        return true;
      }
      CommandPoller.fastPollingUntil = System.currentTimeMillis() + fastPollingTimeSpan * 1000;
    }
    return true;
  }
}

