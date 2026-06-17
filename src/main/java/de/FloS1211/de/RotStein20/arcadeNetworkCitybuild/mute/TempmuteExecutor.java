package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class TempmuteExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length < 5) {
      return false;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
      commandSender.sendMessage(MessageManager.get("general-invalid-player"));
      return false;
    }
    int days = Integer.parseInt(args[1]);
    int hours = Integer.parseInt(args[2]);
    int minutes = Integer.parseInt(args[3]);
    long muteDuration = minutes* 60L + (long) hours *60*60+ (long) days *24*60*60;
    String reason = String.join(" ", Arrays.asList(args).subList(4,args.length));
    String uuid;
    if (commandSender instanceof Player player) {
      uuid = player.getUniqueId().toString();
    } else {
      uuid = "console";
    }
    MuteManager.mutePlayer(targetPlayer.getUniqueId().toString(),muteDuration,uuid,reason);
    commandSender.sendMessage(MessageManager.get("mute-tempmute-sender", Map.of("player",targetPlayer.getName(),"days",days+"","hours",hours+"","minutes",minutes+"","reason",reason)));
    if (targetPlayer.isOnline()) {
      ((Player) targetPlayer).sendMessage(MessageManager.get("mute-tempmute-target", Map.of("days",days+"","hours",hours+"","minutes",minutes+"","reason",reason)));
    }
    return true;
  }
}
