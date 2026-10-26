package de.FloS1211.de.RotStein20.arcadeNetwork.vanish;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.SQLTable;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishManager {
  public static void hidePlayer(Player player) {
    DatabaseManager.executeSQL("UPDATE player_data SET vanished = 1 WHERE uuid = ?", List.of(player.getUniqueId().toString()));
    updateHiddenState(player, true);
  }

  public static void updateHiddenState(Player player, Boolean vanished) {
    if (vanished == null) {
      SQLTable playerData = DatabaseManager.getTable("player_data","uuid = ?",List.of(player.getUniqueId().toString()));
      if (playerData.isEmpty()) vanished = false;
      else vanished = playerData.getBooleanValue("vanished", 0);
    }
    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
      if (onlinePlayer.equals(player)) continue;
      if (onlinePlayer.hasPermission("ac.admin")) {
        onlinePlayer.showPlayer(ArcadeNetwork.getInstance(), player);
        continue;
      }
      if (vanished) {
        onlinePlayer.hidePlayer(ArcadeNetwork.getInstance(), player);
      } else {
        onlinePlayer.showPlayer(ArcadeNetwork.getInstance(), player);
      }
    }
  }

  public static void updateAllHiddenStates() {
    for (Player player : ArcadeNetwork.getInstance().getServer().getOnlinePlayers()) {
      updateHiddenState(player, null);
    }
  }

  public static void showPlayer(Player player) {
    DatabaseManager.executeSQL("UPDATE player_data SET vanished = 0 WHERE uuid = ?", List.of(player.getUniqueId().toString()));
    updateHiddenState(player, false);
  }
}
