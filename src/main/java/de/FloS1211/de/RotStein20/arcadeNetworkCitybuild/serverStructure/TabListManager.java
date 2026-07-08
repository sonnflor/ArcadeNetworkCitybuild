package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.RankManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListManager implements Listener {
  public static void sendTabUpdateToProxy(Player player, Component name) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(player.getUniqueId().toString());
    out.writeUTF(LegacyComponentSerializer.legacySection().serialize(name));
    out.writeInt(RankManager.getRankPriority(player.getUniqueId()));

    player.sendPluginMessage(ArcadeNetworkCitybuild.getInstance(), "arcadenetwork:tab", out.toByteArray());
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Bukkit.getScheduler().runTaskLater(ArcadeNetworkCitybuild.getInstance(), () -> {
      sendTabUpdateToProxy(event.getPlayer(), PlayerNameManager.getPlayerName(event.getPlayer().getUniqueId().toString()));
    }, 2L);
  }
}
