package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.vanish;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import org.bukkit.GameMode;
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
        onlinePlayer.showPlayer(ArcadeNetworkCitybuild.getInstance(), player);
        continue;
      }
      if (vanished) {
        onlinePlayer.hidePlayer(ArcadeNetworkCitybuild.getInstance(), player);
      } else {
        onlinePlayer.showPlayer(ArcadeNetworkCitybuild.getInstance(), player);
      }
    }
  }

  public static void updateAllHiddenStates() {
    for (Player player : ArcadeNetworkCitybuild.getInstance().getServer().getOnlinePlayers()) {
      updateHiddenState(player, null);
    }
  }

  public static void showPlayer(Player player) {
    DatabaseManager.executeSQL("UPDATE player_data SET vanished = 0 WHERE uuid = ?", List.of(player.getUniqueId().toString()));
    updateHiddenState(player, false);
  }
}
