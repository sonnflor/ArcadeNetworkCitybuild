package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PerkEffectListener implements Listener {
  public static void updateBirdPerk(Player player) {
    if (!PerkManager.getPerkStates(player).get("bird").equals(0)) {
      switchBirdPerk(player, PerkManager.getPerkStates(player).get("bird") == 2);
    }
  }
  public static void switchBirdPerk(Player player, boolean state) {
    player.setAllowFlight(state);
  }
  @EventHandler
  public void onJoinGeneral(PlayerJoinEvent event) {
    PerkManager.updatePerks(event.getPlayer());
    updateBirdPerk(event.getPlayer());
  }
  @EventHandler
  public void onRespawnGeneral(PlayerRespawnEvent event) {
    PerkManager.updatePerks(event.getPlayer());
    updateBirdPerk(event.getPlayer());
  }
}
