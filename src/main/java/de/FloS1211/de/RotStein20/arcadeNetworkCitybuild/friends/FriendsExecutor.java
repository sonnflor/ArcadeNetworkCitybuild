package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FriendsExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage("Dieser Command kann nur von Spielern ausgeführt werden!");
            return true;
        }

        // Öffne GUI auf Seite 0
        player.openInventory(Utils.getFriendsGui(player,0));

        return true;
    }
}
