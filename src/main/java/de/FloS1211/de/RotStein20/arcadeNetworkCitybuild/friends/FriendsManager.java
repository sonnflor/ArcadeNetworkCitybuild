package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FriendsManager {
    public static void inviteFriend(Player player, OfflinePlayer target){
        String playerUuid = player.getUniqueId().toString();
        String targetUuid = target.getUniqueId().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long millis = timestamp.getTime();
        int days = (int) TimeUnit.MILLISECONDS.toDays(millis);
        SQLTable table = DatabaseManager.getTable("friends","(uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?)",List.of(playerUuid,targetUuid,targetUuid,playerUuid));
        player.sendMessage(MessageManager.get("friends-request-information",Map.of("target",target.getName())));
        for (int i = 0; i< table.size(); i++){
            if(table.getStringValue("uuid",i).equals(targetUuid) && !table.getBooleanValue("approved",i)){
                acceptFriendRequest(player,target);
            } else {
                player.sendMessage(MessageManager.get("friends-failed-already_befriended"));
            }
        }
        DatabaseManager.executeSQL("INSERT INTO friends (uuid, targetUuid, day, approved) VALUES (?,?,?,?)", List.of(playerUuid,targetUuid,days,false));
        if (target.isOnline()) {
            String token = ClickableMessageManager.registerClickableMessage("friends",true,List.of(playerUuid,targetUuid));
            target.getPlayer().sendMessage(MessageManager.get("friends-request", Map.of("sender",player.getName(),"token",token)));
        }
    }


    public static void deleteFriend(OfflinePlayer player, OfflinePlayer target, boolean sendMessages){
        String playerUuid = player.getUniqueId().toString();
        String targetUuid = target.getUniqueId().toString();
        if (DatabaseManager.getTable("friends","(uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?)", List.of(playerUuid,targetUuid,targetUuid,playerUuid)).isEmpty()) {
            if (player.isOnline()) player.getPlayer().sendMessage(MessageManager.get("friends-failed-no_friend"));
            return;
        }
        DatabaseManager.executeSQL("DELETE FROM friends WHERE (uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?)",List.of(playerUuid,targetUuid,targetUuid,playerUuid));
        if (player.isOnline() && sendMessages){
            player.getPlayer().sendMessage(MessageManager.get("friends-delete",Map.of("player", target.getName())));
        }
        if (target.isOnline() && sendMessages){
            target.getPlayer().sendMessage(MessageManager.get("friends-delete",Map.of("player", player.getName())));
        }
    }

    public static void acceptFriendRequest(OfflinePlayer player, OfflinePlayer target) {
        String playerUuid = player.getUniqueId().toString();
        String targetUuid = target.getUniqueId().toString();
        DatabaseManager.executeSQL("UPDATE friends SET approved=? WHERE approved = ? AND ((uuid=? AND targetUuid = ?) OR (uuid=? AND targetUuid = ?))",List.of(true,false,playerUuid,targetUuid,targetUuid,playerUuid));
        if (player.isOnline()){
            player.getPlayer().sendMessage(MessageManager.get("friends-succeed",Map.of("player", target.getName())));
        }
        if (target.isOnline()){
            target.getPlayer().sendMessage(MessageManager.get("friends-succeed",Map.of("player", player.getName())));
        }
    }

    public static void rejectFriendRequest(OfflinePlayer player, OfflinePlayer target) {
        deleteFriend(player, target, false);
        if (player.isOnline()){
            player.getPlayer().sendMessage(MessageManager.get("friends-rejected-sender",Map.of("target", target.getName())));
        }
        if (target.isOnline()){
            target.getPlayer().sendMessage(MessageManager.get("friends-rejected-target",Map.of("player", player.getName())));
        }
    }

    public static void handleMessageClick(String[] args, CommandSender sender){
        acceptFriendRequest(Bukkit.getOfflinePlayer(UUID.fromString(args[0])),Bukkit.getOfflinePlayer(UUID.fromString(args[1])));
    }
    public static void openGui(Player player) {
        String uuid = player.getUniqueId().toString();
        SQLTable friends = getFriends(uuid);
        Gui gui = new Gui(new HashMap<>(),54, Component.text("Freunde"));
        gui.addElementToBottomBar(GuiButton.getPrevPageButton(new FriendsGuiButtonExecutor()),0);
        gui.addElementToBottomBar(GuiButton.getNextPageButton(new FriendsGuiButtonExecutor()),8);
        gui.addElementToBottomBar(new GuiButton("invite_button",Material.NETHER_STAR,new FriendsGuiButtonExecutor(),GuiButtonType.CUSTOM).title(Component.text("Freund hinzufügen").color(NamedTextColor.GREEN)),4);
        gui.addElementToBottomBar(new GuiButton("delete_button",Material.BARRIER,new FriendsGuiButtonExecutor(),GuiButtonType.CUSTOM).title(Component.text("Freund löschen").color(NamedTextColor.RED)),3);
        gui.addElementToBottomBar(new GuiDisplay("friend_count_display",Material.PLAYER_HEAD).title(Component.text("Anzahl Freunde: "+friends.size()).color(NamedTextColor.YELLOW)).amount(Math.max(friends.size(),1)),5);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long millis = timestamp.getTime();
        int todayUnixDays = (int) TimeUnit.MILLISECONDS.toDays(millis);

        for (int i = 0; i < friends.size(); i++) {
            String friendUuid = friends.getStringValue("uuid",i);
            if (Objects.equals(friendUuid, uuid)) friendUuid = friends.getStringValue("targetUuid",i);
            OfflinePlayer friend = Bukkit.getOfflinePlayer(UUID.fromString(friendUuid));
            int unixDays = friends.getIntValue("day",i);
            String date = Utils.formatDate((unixDays * 86400L),"dd.MM.yyyy");
            int days = todayUnixDays-unixDays;
            List<Component> lore = List.of(
                Component.empty(),
                friend.isOnline() ? Component.text("§aOnline"):Component.text("§cOffline"),
                Component.text("Befreundet seit " + days + " Tagen (" + date + ")")
            );
            gui.addElement(new GuiDisplay("friendDisplay"+friendUuid,Material.PLAYER_HEAD).playerHead(friendUuid).title(Component.text(friend.getName())).lore(lore),(int) Math.round((float)(i)/45.0),i%45);
        }
        SQLTable requests = DatabaseManager.getTable("friends","targetUuid = ? AND approved = ?", List.of(uuid, false));
        for (int i = friends.size(); i < friends.size() + requests.size(); i++) {
            String friendUuid = requests.getStringValue("uuid",i);
            if (Objects.equals(friendUuid, uuid)) friendUuid = requests.getStringValue("targetUuid",i);
            OfflinePlayer friend = Bukkit.getOfflinePlayer(UUID.fromString(friendUuid));
            List<Component> lore = List.of(
                Component.empty(),
                Component.text("Klicke, um die Einladung anzunehmen"),
                Component.text("Rechtsklicke, um die Einladung abzulehnen")
            );
            gui.addElement(new GuiButton("requestButton"+friendUuid,Material.PAPER,new FriendsGuiButtonExecutor(),GuiButtonType.CUSTOM).title(Component.text("Einladung: " + friend.getName())).lore(lore),(int) Math.round((float)(i)/45.0),i%45);
        }
        player.openInventory(gui.buildInventory());
    }
    public static SQLTable getFriends(String uuid) {
        return DatabaseManager.getTable("friends","(uuid = ? OR targetUuid = ?) AND approved = ?", List.of(uuid,uuid, true));
    }
}
