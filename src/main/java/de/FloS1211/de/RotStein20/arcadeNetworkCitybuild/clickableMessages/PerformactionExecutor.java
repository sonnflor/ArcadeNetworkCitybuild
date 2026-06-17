package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PerformactionExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length != 1) {
      return false;
    }
    String token = args[0];
    if (!ClickableMessageManager.performAction(token, commandSender)) {
      commandSender.sendMessage(MessageManager.get("clickablemessage-invalid-token", Map.of("token",token)));
      return true;
    }
    return true;
  }
}
