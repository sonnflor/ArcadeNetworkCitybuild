package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UnmuteExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length != 1) {
      return false;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
      commandSender.sendMessage(MessageManager.get("general-invalid-player"));
      return false;
    }
    MuteManager.unmutePlayer(targetPlayer.getUniqueId().toString());
    commandSender.sendMessage(MessageManager.get("mute-unmute-sender", Map.of("player", targetPlayer.getName())));
    if (targetPlayer.isOnline()) {
      ((Player) targetPlayer).sendMessage(MessageManager.get("mute-unmute-target"));
    }
    return true;
  }
}
