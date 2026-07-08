package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.vanish.VanishManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class RankManager {
  public static List<String> ranks = List.of("default", "crafter", "expert", "elite", "king", "master", "admin", "owner");
  public static List<String> rankPrefixes = List.of(
      "", "§9§lCrafter§7 • ", "§a§lExperte§7 • ", "§e§lElite§7 • ", "§6§lKing§7 • ", "§c§lMaster§7 • ", "§c§lA§6§ld§e§lm§a§li§9§ln§7 • ", "§c§lO§6§lw§e§ln§a§le§9§lr§7 • "
  );
  public static List<String> extraRanks = List.of("none","testsupporter","supporter","testmoderator","moderator","builder","creator","developer");
  public static List<String> rankSuffixes = List.of(
      "","§7 • §9§lT§b§l-§9§lS§b§lu§9§lp","§7 • §9§lS§b§lu§9§lp","§7 • §2§lT§a§l-§2§lM§a§lo§2§ld","§7 • §2§lM§a§lo§2§ld","§7 • §c§lBuilder","§7 • §5§lCreator","§7 • §4§lDev"
  );

  public static void setRank(String uuid, String rank) {
    if (ranks.contains(rank)) {
      DatabaseManager.executeSQL("INSERT INTO player_data (uuid,rank) VALUES (?,?) ON DUPLICATE KEY UPDATE rank = ?", List.of(uuid, rank, rank));
      if (Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
        PermissionManager.setPermission(uuid, rank, getExtraRank(uuid));
        PlayerNameManager.updatePlayerName(uuid);
        VanishManager.updateAllHiddenStates();
      }
    } else if (extraRanks.contains(rank)) {
      DatabaseManager.executeSQL("INSERT INTO player_data (uuid,extra_rank) VALUES (?,?) ON DUPLICATE KEY UPDATE extra_rank = ?", List.of(uuid, rank, rank));
      if (Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
        PermissionManager.setPermission(uuid,getRank(uuid),rank);
        PlayerNameManager.updatePlayerName(uuid);
      }
    } else {
      throw new IllegalArgumentException("Ungültiger Rang: " + rank);
    }
  }

  public static String getRank(String uuid) {
    SQLTable rankData = new SQLTable("player_data","uuid = ?", List.of(uuid));
    if (!rankData.isEmpty()) {
      return rankData.getStringValue("rank", 0);
    } else {
      return "default";
    }
  }

  public static String getExtraRank(String uuid) {
    SQLTable rankData = new SQLTable("player_data","uuid = ?", List.of(uuid));
    if (!rankData.isEmpty()) {
      return rankData.getStringValue("extra_rank", 0);
    } else {
      return "none";
    }
  }

  public static boolean improveRank(String uuid, String newRank) {
    String currentRank = getRank(uuid);
    if (ranks.contains(newRank)) {
      if (ranks.indexOf(newRank) > ranks.indexOf(currentRank)) {
        setRank(uuid, newRank);
        return true;
      } else {
        return false;
      }
    } else if (extraRanks.contains(newRank)) {
      setRank(uuid,newRank);
      return true;
    } else {
      throw new IllegalArgumentException("Ungültiger Rang: " + newRank);
    }
  }

  public static Component getRankPrefix(String rank) {
    if (ranks.contains(rank)) {
      return LegacyComponentSerializer.legacySection().deserialize(rankPrefixes.get(ranks.indexOf(rank)));
    } else {
      throw new IllegalArgumentException("Ungültiger Rang: " + rank);
    }
  }

  public static Component getRankSuffix(String rank) {
    if (extraRanks.contains(rank)) {
      return LegacyComponentSerializer.legacySection().deserialize(rankSuffixes.get(extraRanks.indexOf(rank)));
    } else {
      throw new IllegalArgumentException("Ungültiger Rang: " + rank);
    }
  }

  public static int getRankPriority(UUID uuid) {
    String rank = getRank(uuid.toString());
    if (ranks.contains(rank)) {
      return ranks.size() - ranks.indexOf(rank) - 1;
    } else {
      throw new IllegalArgumentException("Ungültiger Rang: " + rank);
    }
  }
}
