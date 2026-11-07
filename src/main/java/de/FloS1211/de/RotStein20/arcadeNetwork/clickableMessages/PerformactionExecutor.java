package de.FloS1211.de.RotStein20.arcadeNetwork.clickableMessages;

import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.MessageManager;
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
      LanguageManager.send(commandSender, "clickablemessage.chat.invalid-token", "token", token);
      return true;
    }
    return true;
  }
}
