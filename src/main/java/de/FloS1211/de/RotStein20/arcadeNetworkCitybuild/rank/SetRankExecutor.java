package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetRankExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length != 3) {
      if (args.length == 2) {
        if (!args[1].equalsIgnoreCase("clear")) {
          return false;
        }
      } else {
        return false;
      }
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
      commandSender.sendMessage("Bitte gib einen gültigen Spielernamen an.");
      return true;
    }
    String rankType = args[1];
    if (rankType.equalsIgnoreCase("clear")) {
      RankManager.setRank(targetPlayer.getUniqueId().toString(),"default");
      commandSender.sendMessage("Der Rang von " + targetPlayer.getName() + " wurde gelöscht.");
      if (targetPlayer.isOnline()) {
        ((Player) targetPlayer).sendMessage("Dein Rang wurde gelöscht.");
      }
      return true;
    } else if (rankType.equalsIgnoreCase("rank") || rankType.equalsIgnoreCase("extrarank")) {
      String rankName = args[2];
      try {
        if (rankType.equalsIgnoreCase("rank")) {
          RankManager.setRank(targetPlayer.getUniqueId().toString(),rankName);
        } else {
          RankManager.setRank(targetPlayer.getUniqueId().toString(),rankName);
        }
        commandSender.sendMessage("Der Rang von " + targetPlayer.getName() + " wurde auf " + rankName + " gesetzt.");
        if (targetPlayer.isOnline()) {
          ((Player) targetPlayer).sendMessage("Dein Rang wurde auf " + rankName + " gesetzt.");
        }
      } catch (IllegalArgumentException e) {
        commandSender.sendMessage("Bitte gib einen gültigen Rang an");
      }

    }
    return true;
  }
}
