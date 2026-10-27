package de.FloS1211.de.RotStein20.arcadeNetwork.tpa;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

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
    if (!List.of("player_to_me","me_to_player").contains(type)) {
      return false;
    }
    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
    if (target.isOnline()) {
      TpaManager.sendTpa(type, player.getUniqueId().toString(), target.getPlayer(), false);
    } else {
      if (ArcadeNetwork.getInstance().featureManager.getState("proxy")) TpaManager.requestTpaCrossServer(type,player,target);
    }
    LanguageManager.send(player, "tpa.chat."+type+"-sender", "player", target.getName());
    return true;
  }
}
