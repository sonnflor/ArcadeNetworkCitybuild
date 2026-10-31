package de.FloS1211.de.RotStein20.arcadeNetwork.rank;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.namecolor.NamecolorManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.FeatureManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.TabListManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerNameManager implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    updatePlayerName(player.getUniqueId().toString());
  }
  public static Component getPlayerName(String uuid) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
    Component result = Component.text("");
    FeatureManager featureManager = ArcadeNetwork.getInstance().featureManager;
    if (featureManager.getState("rank")) {
      String rank = RankManager.getRank(uuid);
      Component prefix = RankManager.getRankPrefix(rank);
      result = result.append(prefix);
    }
    if (featureManager.getState("namecolor")) {
      result = result.append(NamecolorManager.getColoredName(
          NamecolorManager.getActiveNamecolor(uuid),
          player.getName(),
          true
      ));
    } else {
      result = result.append(Component.text(player.getName(), NamedTextColor.GRAY));
    }
    if (featureManager.getState("rank")) {
      String extraRank = RankManager.getExtraRank(uuid);
      Component suffix = RankManager.getRankSuffix(extraRank);
      result = result.append(suffix);
    }
    return result;
  }
  public static String getPlayerNameString(String uuid) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
    String rank = RankManager.getRank(uuid);
    String extraRank = RankManager.getExtraRank(uuid);
    String prefix = LegacyComponentSerializer.legacySection().serialize(RankManager.getRankPrefix(rank));
    String suffix = LegacyComponentSerializer.legacySection().serialize(RankManager.getRankSuffix(extraRank));
    String coloredName = NamecolorManager.getColoredNameAsString(NamecolorManager.getActiveNamecolor(uuid),player.getName());
    String fullName = prefix+coloredName+suffix;
    return fullName;
  }
  public static void updatePlayerName(String uuid) {
    Player player = Bukkit.getPlayer(UUID.fromString(uuid));
    Component name = getPlayerName(uuid);
    player.displayName(name);
    player.playerListName(name);
    player.customName(name);
    TabListManager.sendTabUpdateToProxy(player,name);
  }
}
