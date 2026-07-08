package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TpaManager {
  public static void sendTpa(String direction, String senderUuid, Player targetPlayer) {
    Bukkit.getLogger().info("+0");
    if (targetPlayer == null) {
      Bukkit.getLogger().severe("target = null (TpaManager)");
      return;
    }
    Bukkit.getLogger().info("+1");
    String targetUuid = targetPlayer.getUniqueId().toString();
    Bukkit.getLogger().info(targetUuid);
    String acceptToken = ClickableMessageManager.registerClickableMessage("tpa",true,List.of(
        targetUuid,
        senderUuid,
        "true",
        direction
    ));
    String rejetToken = ClickableMessageManager.registerClickableMessage("tpa",true,List.of(
        targetUuid,
        senderUuid,
        "false",
        direction
    ));
    Component targetMessage = MessageManager.get("tpa-" + direction + "-target", Map.of("acceptToken",acceptToken,"rejectToken",rejetToken,"player",Bukkit.getOfflinePlayer(UUID.fromString(senderUuid)).getName()));
    targetPlayer.sendMessage(targetMessage);
  }
  public static void requestTpaCrossServer(String direction, Player sender, OfflinePlayer targetPlayer) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("REQUEST_TPA");
    out.writeUTF(sender.getUniqueId().toString());
    out.writeUTF(targetPlayer.getUniqueId().toString());
    out.writeUTF(direction);
    sender.sendPluginMessage(ArcadeNetworkCitybuild.getInstance(),"arcadenetwork:tpa", out.toByteArray());
  }
  public static void handleMessageClick(String[] args, CommandSender commandSender) {
    String clickerUuid = args[0];
    String originalSenderUuid = args[1];
    boolean succeed = args[2].equals("true");
    String direction = args[3];

    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("TELEPORT_PLAYER");
    out.writeUTF(originalSenderUuid);
    out.writeUTF(clickerUuid);
    out.writeBoolean(succeed);
    out.writeUTF(direction);

    Player clicker = Bukkit.getPlayer(UUID.fromString(clickerUuid));
    clicker.sendPluginMessage(ArcadeNetworkCitybuild.getInstance(), "arcadenetwork:tpa",out.toByteArray());
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
      sender.getPlayer().sendMessage(MessageManager.get("tpa-reject-target",Map.of("player", target.getName())));
    }
    if (target.getPlayer() != null) {
      target.getPlayer().sendMessage(MessageManager.get("tpa-reject-rejecter",Map.of("player", sender.getName())));
    }

  }
}
