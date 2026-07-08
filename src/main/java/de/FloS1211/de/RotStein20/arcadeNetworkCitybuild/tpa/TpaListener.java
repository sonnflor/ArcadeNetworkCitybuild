package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TpaListener implements PluginMessageListener {
  @Override
  public void onPluginMessageReceived(String channel, @NotNull Player player, byte @NotNull [] message) {
    Bukkit.getLogger().info("0");
    if (!channel.equals("arcadenetwork:tpa")) return;
    Bukkit.getLogger().info("1");
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String action = in.readUTF();
    Bukkit.getLogger().info(action);
    if (action.equals("SEND_TPA")) {
      Bukkit.getLogger().info("2");
      String senderUuid = in.readUTF();
      String targetUuid = in.readUTF();
      String  direction = in.readUTF();
      TpaManager.sendTpa(direction,senderUuid, Bukkit.getPlayer(UUID.fromString(targetUuid)));
    } else if (action.equals("EXECUTE_TELEPORT")) {
      boolean succeed = in.readBoolean();
      UUID senderUuid = UUID.fromString(in.readUTF());
      UUID targetUuid = UUID.fromString(in.readUTF());
      String  direction = in.readUTF();

      if (succeed) {
        TpaManager.teleport(senderUuid,targetUuid,direction);
      } else {
        TpaManager.sendRejection(senderUuid,targetUuid,direction);
      }
    }
  }
}
