package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import com.mojang.brigadier.Command;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FriendsManager {
    public static void openGUI(Player player){

    }

    public static void inviteFriend(Player player, OfflinePlayer target){
        String playerUuid = player.getUniqueId().toString();
        String targetUuid = target.getUniqueId().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long millis = timestamp.getTime();
        int days = (int) TimeUnit.MILLISECONDS.toDays(millis);
        SQLTable table = DatabaseManager.getTable("friends","(uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?)",List.of(playerUuid,targetUuid,targetUuid,playerUuid));

        for (int i = 0; i< table.size(); i++){
            if(table.getStringValue("uuid",i) == targetUuid && !table.getBooleanValue("approved",i)){
                acceptFriendRequest(player,target);
            } else {
                return;
            }
        }

        DatabaseManager.executeSQL("INSERT INTO friends (uuid, targetUuid, day, approved) VALUES (?,?,?,?)", List.of(playerUuid,targetUuid,days,false));
        if (target.isOnline()) {
            String token = ClickableMessageManager.registerClickableMessage("friends",true,List.of(playerUuid,targetUuid));
            target.getPlayer().sendMessage(MessageManager.get("friends-request", Map.of("sender",player.getName(),"token",token)));
        }
    }


    public static void deleteFriend(Player player, OfflinePlayer target){
        String playerUuid = player.getUniqueId().toString();
        String targetUuid = target.getUniqueId().toString();
        DatabaseManager.executeSQL("DELETE FROM friends WHERE (uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?)",List.of(playerUuid,targetUuid,targetUuid,playerUuid));
        if (player.isOnline()){
            player.getPlayer().sendMessage(MessageManager.get("friends-delete",Map.of("player", target.getName())));
        }
        if (target.isOnline()){
            target.getPlayer().sendMessage(MessageManager.get("friends-delete",Map.of("player", player.getName())));
        }
    }

    public static void acceptFriendRequest(OfflinePlayer player, OfflinePlayer target) {
        String playerUuid = player.getUniqueId().toString();
        String targetUuid = target.getUniqueId().toString();
        DatabaseManager.executeSQL("UPDATE friends SET approved=? WHERE approved = ? AND (uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?)",List.of(playerUuid,targetUuid,targetUuid,playerUuid));
        if (player.isOnline()){
            player.getPlayer().sendMessage(MessageManager.get("friends-succeed",Map.of("player", target.getName())));
        }
        if (target.isOnline()){
            target.getPlayer().sendMessage(MessageManager.get("friends-succeed",Map.of("player", player.getName())));
        }
    }
    public static void handleMessageClick(String[] args, CommandSender sender){
        acceptFriendRequest(Bukkit.getOfflinePlayer(UUID.fromString(args[0])),Bukkit.getOfflinePlayer(UUID.fromString(args[1])));
    }
}
