package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mail;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MailExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
      return true;
    }
    if (args.length == 0) {
      MailManager.openMail(player,"inbox",false);
    } else {
      if (!List.of("inbox", "outbox", "send").contains(args[0].toLowerCase())) {
        return false;
      }
      if (args[0].equalsIgnoreCase("send")) {
        MailManager.sendMail(player);
      } else {
        MailManager.openMail(player, args[0], false);
      }
    }
    return true;
  }
}
