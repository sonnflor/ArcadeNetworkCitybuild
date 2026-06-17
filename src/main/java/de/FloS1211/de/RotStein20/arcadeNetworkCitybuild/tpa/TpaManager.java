package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TpaManager {
  public static void sendTpa(String type, Player player, Player targetPlayer) {
    String uuid = player.getUniqueId().toString();
    String targetUuid = targetPlayer.getUniqueId().toString();
    if (type.equalsIgnoreCase("to_me")) {
      String targetToken = ClickableMessageManager.registerClickableMessage("tpa",List.of("to_me",uuid,targetUuid));
      targetPlayer.sendMessage(MessageManager.get("tpa-to_me-target", Map.of("player", player.getName(),"token",targetToken)));
      player.sendMessage(MessageManager.get("tpa-to_me-sender", Map.of("targetPlayer",targetPlayer.getName())));
    } else if (type.equalsIgnoreCase("me_to")) {
      String targetToken = ClickableMessageManager.registerClickableMessage("tpa",List.of("me_to",uuid,targetUuid));
      targetPlayer.sendMessage(MessageManager.get("tpa-me_to-target", Map.of("player", player.getName(),"token",targetToken)));
      player.sendMessage(MessageManager.get("tpa-me_to-sender", Map.of("targetPlayer",targetPlayer.getName())));
    }
  }
  public static void handleMessageClick(List<String> args, CommandSender commandSender) {
    String type = args.get(0);
    String uuid = args.get(1);
    String targetUuid = args.get(2);
    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(UUID.fromString(targetUuid));
    if (player.isOnline() && targetPlayer.isOnline()) {
      if (type.equalsIgnoreCase("me_to")) {
        ((Player) player).teleport(((Player)targetPlayer));
      } else {
        ((Player) targetPlayer).teleport(((Player)player));
      }
    }
  }
}
