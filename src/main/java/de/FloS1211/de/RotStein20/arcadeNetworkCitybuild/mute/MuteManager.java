package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.util.List;

public class MuteManager implements Listener {
  public static void mutePlayer(String uuid, long muteDuration, String muterUuid, String reason) {
    long muteTimestamp = Instant.now().getEpochSecond();
    SQLTable playerData = DatabaseManager.getTable("player_data","uuid=?", List.of(uuid));
    MuteEntry muteEntry = new MuteEntry(uuid,muteTimestamp,muteDuration,muterUuid,reason);
    if (playerData.isEmpty()) {
      DatabaseManager.executeSQL("INSERT INTO player_data (uuid,is_muted,mute_data) VALUES (?,?,?)",List.of(uuid,true,muteEntry.toString()));
    } else {
      if (playerData.getBooleanValue("is_muted",0)) {
        MuteEntry oldMuteEntry = MuteEntry.fromString(playerData.getStringValue("mute_data", 0));
        long newUnmuteTime = muteTimestamp + muteDuration;
        long oldUnmuteTime = oldMuteEntry.getMuteTimestamp() + oldMuteEntry.getMuteDuration();
        if (newUnmuteTime > oldUnmuteTime || muteDuration == -1) {
          DatabaseManager.executeSQL("UPDATE player_data SET is_muted = ?,mute_data = ? WHERE uuid = ?", List.of(true, muteEntry.toString(), uuid));
        }
      } else {
        DatabaseManager.executeSQL("UPDATE player_data SET is_muted = ?,mute_data = ? WHERE uuid = ?", List.of(true, muteEntry.toString(), uuid));
      }
    }
  }

  public static void unmutePlayer(String uuid) {
    SQLTable playerData = DatabaseManager.getTable("player_data","uuid=?", List.of(uuid));
    if (playerData.isEmpty()) {
      DatabaseManager.executeSQL("INSERT INTO player_data (uuid,is_muted,mute_data) VALUES (?,?,?)",List.of(uuid,false,""));
    } else {
      DatabaseManager.executeSQL("UPDATE player_data SET is_muted = ?,mute_data = ? WHERE uuid = ?",List.of(false,"",uuid));
    }
  }

  public static boolean isMuteValid(long muteTimestamp, long muteDuration,String uuid) {
    long timestamp = Instant.now().getEpochSecond();
    long unmuteTime = muteTimestamp+muteDuration;
    if (unmuteTime > timestamp || muteDuration == -1) {
      return true;
    } else {
      DatabaseManager.executeSQL("UPDATE player_data SET is_muted = ?,mute_data = ? WHERE uuid = ?",List.of(false,"",uuid));
      return false;
    }
  }

  public static boolean checkWhetherPlayerIsMuted(String uuid) {
    SQLTable playerData = DatabaseManager.getTable("player_data","uuid=?", List.of(uuid));
    if (playerData.isEmpty()) {
      return false;
    } else {
      boolean isMuted = playerData.getBooleanValue("is_muted",0);
      if (isMuted) {
        String muteData = playerData.getStringValue("mute_data",0);
        if (muteData == null) {
          Bukkit.getLogger().warning("Inconsistency at Mute at Player with uuid "+uuid);
          return false;
        }
        MuteEntry mute = MuteEntry.fromString(muteData);
        return isMuteValid(mute.getMuteTimestamp(), mute.getMuteDuration(), uuid);
      } else {
        return false;
      }
    }
  }

  @EventHandler
  public void onChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String uuid = player.getUniqueId().toString();
    if (checkWhetherPlayerIsMuted(uuid)) {
      event.setCancelled(true);
      player.sendMessage(MessageManager.get("mute-reject-message"));
    }
  }
}
