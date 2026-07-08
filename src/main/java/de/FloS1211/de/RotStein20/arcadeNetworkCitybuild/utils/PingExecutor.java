package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.Buffer;
import java.util.Map;

public class PingExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    // ping (<player>)
    Player targetPlayer;
    if (args.length == 0) {
      if (!(commandSender instanceof Player player)) {
        commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
        return true;
      }
      targetPlayer = player;
    } else if (args.length == 1) {
      OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
      if (!offlineTarget.isOnline()) {
        commandSender.sendMessage(MessageManager.get("general-invalid-player"));
        return false;
      }
      targetPlayer = offlineTarget.getPlayer();
    } else {
      return false;
    }

    int ping = targetPlayer.getPing();
    Component msg;
    if (args.length == 0) {
      msg = MessageManager.get("ping-own", Map.of("ping",ping+""));
    } else {
      msg = MessageManager.get("ping-other", Map.of("ping",ping+"","targetPlayer", targetPlayer.getName()));
    }
    commandSender.sendMessage(msg);
    return true;
  }
}
