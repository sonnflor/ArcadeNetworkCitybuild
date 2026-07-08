package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class TempBanExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length < 5) return false;
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    if (!(targetPlayer.hasPlayedBefore() || targetPlayer.isOnline())) {
      commandSender.sendMessage(MessageManager.get("general-invalid-player"));
    }
    String reason = String.join(" ", List.of(args).subList(4,args.length));
    String playerName = "Console";
    if (commandSender instanceof Player player) playerName = player.getName();
    int days = Integer.parseInt(args[1]);
    int hours = Integer.parseInt(args[2]);
    int minutes = Integer.parseInt(args[3]);
    targetPlayer.ban(reason, Duration.ofMinutes(minutes+hours* 60L + (long) days *25*60),playerName);
    commandSender.sendMessage(MessageManager.get("tempban-succeed", Map.of("player",args[0],"time", days+" Tage, "+hours+" Stunden und "+minutes + " Minuten")));
    return true;
  }
}
