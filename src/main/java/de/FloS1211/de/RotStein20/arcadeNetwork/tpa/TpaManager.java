package de.FloS1211.de.RotStein20.arcadeNetwork.tpa;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.MessageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TpaManager {
  public static void sendTpa(String direction, String senderUuid, Player targetPlayer, boolean crossServer) {
    if (targetPlayer == null) {
      Bukkit.getLogger().severe("target = null (TpaManager)");
      return;
    }
    String targetUuid = targetPlayer.getUniqueId().toString();
    String acceptToken = ClickableMessageManager.registerClickableMessage("tpa",true,List.of(
        targetUuid,
        senderUuid,
        "true",
        direction,
        crossServer ? "cross-server" : "local"
    ));
    String rejetToken = ClickableMessageManager.registerClickableMessage("tpa",true,List.of(
        targetUuid,
        senderUuid,
        "false",
        direction,
        crossServer ? "cross-server" : "local"
    ));
    LanguageManager.send(targetPlayer, "tpa.chat."+direction+"-target", "accept_token", acceptToken, "reject_token", rejetToken, "player", Bukkit.getOfflinePlayer(UUID.fromString(senderUuid)).getName());
  }
  public static void requestTpaCrossServer(String direction, Player sender, OfflinePlayer targetPlayer) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("REQUEST_TPA");
    out.writeUTF(sender.getUniqueId().toString());
    out.writeUTF(targetPlayer.getUniqueId().toString());
    out.writeUTF(direction);
    sender.sendPluginMessage(ArcadeNetwork.getInstance(),"arcadenetwork:tpa", out.toByteArray());
  }
  public static void handleMessageClick(String[] args, CommandSender commandSender) {
    String clickerUuid = args[0];
    String originalSenderUuid = args[1];
    boolean succeed = args[2].equals("true");
    String direction = args[3];
    boolean crossServer = Objects.equals(args[4], "cross-server");

    if (crossServer) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("TELEPORT_PLAYER");
      out.writeUTF(originalSenderUuid);
      out.writeUTF(clickerUuid);
      out.writeBoolean(succeed);
      out.writeUTF(direction);

      Player clicker = Bukkit.getPlayer(UUID.fromString(clickerUuid));
      clicker.sendPluginMessage(ArcadeNetwork.getInstance(), "arcadenetwork:tpa", out.toByteArray());
    } else {
     if (succeed) {
       teleport(UUID.fromString(originalSenderUuid), UUID.fromString(clickerUuid), direction);
     } else {
       sendRejection(UUID.fromString(originalSenderUuid), UUID.fromString(clickerUuid), direction);
     }
    }
  }

  public static void teleport(UUID senderUuid, UUID targetUuid, String direction) {
    OfflinePlayer sender = Bukkit.getOfflinePlayer(senderUuid);
    OfflinePlayer target = Bukkit.getOfflinePlayer(targetUuid);
    if (!sender.isOnline() || !target.isOnline()) return;
    if (sender.getPlayer() == null || target.getPlayer() == null) return;
    if (direction.equals("me_to_player")) {
      sender.getPlayer().teleport(target.getPlayer());
    } else {
      target.getPlayer().teleport(sender.getPlayer());
    }
  }

  public static void sendRejection(UUID senderUuid, UUID targetUuid, String direction) {
    OfflinePlayer sender = Bukkit.getOfflinePlayer(senderUuid);
    OfflinePlayer target = Bukkit.getOfflinePlayer(targetUuid);
    if (sender.getPlayer() != null) {
      LanguageManager.send(sender.getPlayer(), "tpa.chat.reject-target", "player", target.getName());
    }
    if (target.getPlayer() != null) {
      LanguageManager.send(target.getPlayer(), "tpa.chat.reject-rejecter", "player", sender.getName());
    }
  }
}
