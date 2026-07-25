package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
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

        DatabaseManager.executeSQL("INSERT INTO friends (uuid, targetUuid, day, accepted) VALUES (?,?,?,?)", List.of(playerUuid,targetUuid,days,false));
        if (target.isOnline()) {

        }
    }


    public static void deleteFriend(Player player, OfflinePlayer target){

    }

    public static void acceptFriendRequest(Player player, OfflinePlayer target) {

    }
}
