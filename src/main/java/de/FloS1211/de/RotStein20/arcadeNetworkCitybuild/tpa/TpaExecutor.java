package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa;

import com.comphenix.protocol.PacketType;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpaExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
      return true;
    }
    if (args.length != 2) {
      return false;
    }
    String type = args[0];
    if (!List.of("to_me","me_to").contains(type)) {
      return false;
    }
    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
    if (!target.isOnline() && !target.hasPlayedBefore()) {
      player.sendMessage(MessageManager.get("general-invalid-player"));
      return false;
    }
    TpaManager.sendTpa(type,player,(Player) target);
    return true;
  }
}
