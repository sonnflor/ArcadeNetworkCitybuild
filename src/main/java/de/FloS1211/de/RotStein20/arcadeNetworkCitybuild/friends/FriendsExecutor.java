package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FriendsExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.get("general-invalid-executor"));
            return true;
        }
        if (args.length == 0){
            FriendsManager.openGui(player);
        } else if (args.length >1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            switch (args[0]) {
                case "invite":
                    if (!target.hasPlayedBefore() && !target.isOnline()){
                        player.sendMessage(MessageManager.get("general-invalid-player"));
                        return false;
                    }
                    FriendsManager.inviteFriend(player,target);
                    break;
                case "delete":
                    if (!target.hasPlayedBefore() && !target.isOnline()){
                        player.sendMessage(MessageManager.get("general-invalid-player"));
                        return false;
                    }
                    FriendsManager.deleteFriend(player,target);
                    break;
            }
        }
        return true;
    }
}
