package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mail;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Map;

public class MailListener implements Listener {
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    SQLTable newMails = DatabaseManager.getTable("mail","uuidTarget = ? AND isRead = ?", List.of(event.getPlayer().getUniqueId().toString(), false));
    if (!newMails.isEmpty()) {
      event.getPlayer().sendMessage(MessageManager.get("mail-new-mail", Map.of("count", String.valueOf(newMails.size()))));
    }
  }
}
