package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class PermissionManager implements Listener {
  public static final Map<UUID,PermissionAttachment> ATTACHMENTS = new HashMap<>();

  public static void initialize() {
    for (Player player : ArcadeNetworkCitybuild.getInstance().getServer().getOnlinePlayers()) {
      PermissionAttachment attachment = player.addAttachment(ArcadeNetworkCitybuild.getInstance());
      ATTACHMENTS.put(player.getUniqueId(), attachment);
      updatePermissions(player.getUniqueId().toString());
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    PermissionAttachment attachment = player.addAttachment(ArcadeNetworkCitybuild.getInstance());
    ATTACHMENTS.put(player.getUniqueId(), attachment);
    updatePermissions(player.getUniqueId().toString());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    clearPermissions(event.getPlayer());
  }

  public static void updatePermissions(String uuid) {
    SQLTable playerData = new SQLTable("player_data","uuid = ?", List.of(uuid));
    if (!playerData.isEmpty()) {
      String rank = playerData.getStringValue("rank", 0);
      String extraRank = playerData.getStringValue("extra_rank", 0);
      setPermission(uuid, rank, extraRank);
    }
  }
  public static void setPermission(String uuidString, String rank, String extraRank) {
    UUID uuid = UUID.fromString(uuidString);
    PermissionAttachment attachment = ATTACHMENTS.get(uuid);
    if (attachment == null) {
      Bukkit.getLogger().warning("error");
      return;
    }
    for (String permission : new HashSet<>(attachment.getPermissions().keySet())) {
      attachment.unsetPermission(permission);
    }
    if (!Objects.equals(rank, "default")) {
      attachment.setPermission("ac."+rank,true);
    }
    if (!Objects.equals(extraRank, "none")) {
      attachment.setPermission("ac."+extraRank,true);
    }
  }

  public static void clearPermissions(Player player) {
    PermissionAttachment attachment = ATTACHMENTS.remove(player.getUniqueId());
    if (attachment != null) {
      player.removeAttachment(attachment);
    }
  }
}
