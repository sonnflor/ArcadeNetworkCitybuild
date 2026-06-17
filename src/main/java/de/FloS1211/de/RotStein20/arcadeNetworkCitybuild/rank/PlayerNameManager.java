package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
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
    Player player = Bukkit.getPlayer(UUID.fromString(uuid));
    String rank = RankManager.getRank(uuid);
    String extraRank = RankManager.getExtraRank(uuid);
    Component prefix = RankManager.getRankPrefix(rank);
    Component suffix = RankManager.getRankSuffix(extraRank);
    Component coloredName = NamecolorManager.getColoredName(NamecolorManager.getActiveNamecolor(uuid),player.getName(),true);
    return prefix.append(coloredName).append(suffix);
  }
  public static String getPlayerNameString(String uuid) {
    Player player = Bukkit.getPlayer(UUID.fromString(uuid));
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
  }
}
