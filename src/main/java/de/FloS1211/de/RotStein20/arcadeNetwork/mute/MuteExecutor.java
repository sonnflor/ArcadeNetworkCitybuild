package de.FloS1211.de.RotStein20.arcadeNetwork.mute;

import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MuteExecutor implements CommandExecutor{
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length < 2) {
      return false;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
      LanguageManager.send(commandSender, "general.invalid-player");
      return false;
    }
    String reason = String.join(" ",Arrays.asList(args).subList(1,args.length));
    String uuid;
    if (commandSender instanceof Player player) {
      uuid = player.getUniqueId().toString();
    } else {
      uuid = "console";
    }
    MuteManager.mutePlayer(targetPlayer.getUniqueId().toString(),-1,uuid,reason);
    LanguageManager.send(commandSender, "mute.chat.mute-succeed-sender", "player", targetPlayer.getName(), "reason", reason);
    if (targetPlayer.isOnline()) {
      LanguageManager.send(targetPlayer.getPlayer(), "mute.chat.mute-succeed-target", "reason", reason);
    }
    return true;
  }
}
