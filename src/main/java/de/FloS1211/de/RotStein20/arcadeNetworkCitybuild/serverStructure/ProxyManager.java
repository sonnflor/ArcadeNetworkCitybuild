package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

public class ProxyManager {
  public static String getServerName() {
    Map<Integer,String> serverMap = new HashMap<>();
    serverMap.put(11140, "Arcade Attack");
    serverMap.put(11141, "Citybuild");
    serverMap.put(11142, "Farming");
    serverMap.put(11143, "Lobby");
    serverMap.put(11144, "Kreativ Server");
    return serverMap.get(Bukkit.getPort());
  }

  public static void teleportToServer(Player player, String server) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(server);
    player.sendPluginMessage(ArcadeNetworkCitybuild.getInstance(), "BungeeCord", out.toByteArray());
  }
}
