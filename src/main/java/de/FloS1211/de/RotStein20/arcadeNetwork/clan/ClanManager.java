package de.FloS1211.de.RotStein20.arcadeNetwork.clan;

import de.FloS1211.de.RotStein20.arcadeNetwork.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class ClanManager {
  public static void createClan(String clan, OfflinePlayer owner, Integer color) {
    if (!new SQLTable("clans", "name = ?", List.of(clan)).isEmpty()) {
      throw new IllegalArgumentException("clan.chat.create-failed-already_exists");
    }
    String currentClan = getClan(owner.getUniqueId().toString());
    if (currentClan != null && !currentClan.isBlank()) {
      throw new IllegalArgumentException("clan.chat.create-failed-already_in_clan");
    }
    if (color == null) {
      color = (int) (Math.random()*16);
    }
    DatabaseManager.executeSQL(
        "INSERT INTO clans (name, owner, color) VALUES (?,?,?)",
        List.of(clan, owner.getUniqueId().toString(), color));
    joinClan(clan, owner);
  }

  public static void deleteClan(String clan) {
    DatabaseManager.executeSQL(
        "DELETE FROM clans WHERE name = ?",
        List.of(clan));
    List<String> playersInClan = new SQLTable("player_data", "clan = ?",List.of(clan)).getStringColumn("uuid");
    DatabaseManager.executeSQL(
        "UPDATE player_data SET clan = ? WHERE clan = ?",
        List.of("", clan));
    for (String uuid : playersInClan) {
      PlayerNameManager.updatePlayerName(uuid);
    }
  }

  public static void inviteClan(Player owner, Player targetPlayer) {
    String clan = new SQLTable(
        "player_data",
        "uuid = ?",
        List.of(owner.getUniqueId().toString())).getStringValue("clan", 0);
    if (!isOwner(owner, clan)) {
      throw new IllegalArgumentException("sender is no owner");
    }
    String token = ClickableMessageManager.registerClickableMessage("clan",true,List.of(clan));
    LanguageManager.send(
        targetPlayer,
        "clan.chat.invitation",
        "player", MiniMessage
            .miniMessage()
            .serialize(PlayerNameManager.getPlayerName(owner.getUniqueId().toString())),
        "clan", MiniMessage.miniMessage().serialize(getPrefix(clan)),
        "token", token);
  }

  public static void joinClan(String clan, OfflinePlayer player) {
    if (new SQLTable("clans", "name = ?", List.of(clan)).isEmpty()) {
      throw new IllegalArgumentException("clan does nt exists");
    }
    DatabaseManager.executeSQL(
        "UPDATE player_data SET clan = ? WHERE uuid = ?",
        List.of(clan, player.getUniqueId().toString()));
    PlayerNameManager.updatePlayerName(player.getUniqueId().toString());
  }

  public static void leaveClan(String clan, OfflinePlayer player) {
    String uuid = player.getUniqueId().toString();
    if (clan == null || clan.isBlank() || !Objects.equals(getClan(uuid), clan)) {
      throw new IllegalArgumentException("player is not in this clan");
    }
    if (isOwner(player, clan)) {
      deleteClan(clan);
      return;
    }
    DatabaseManager.executeSQL(
        "UPDATE player_data SET clan = ? WHERE uuid = ?",
        List.of("", uuid)
    );
    PlayerNameManager.updatePlayerName(uuid);
  }

  public static String getClan(String uuid) {
    return new SQLTable("player_data", "uuid = ?", List.of(uuid)).getStringValue("clan", 0);
  }
  public static boolean isOwner(OfflinePlayer player, String clan) {
    if (clan == null || clan.isBlank()) return false;

    return !new SQLTable(
        "clans",
        "owner = ? AND name = ?",
        List.of(player.getUniqueId().toString(), clan)
    ).isEmpty();
  }
  public static Component getPrefix(String clan) {
    int color = new SQLTable("clans", "name = ?", List.of(clan)).getIntValue("color", 0);
    return MiniMessage.miniMessage().deserialize("<gray>[<" + ClanManager.colors.get(color) + ">" + clan + "<gray>]");
  }
  public static List<String> colors = List.of("black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray","dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white");
}
