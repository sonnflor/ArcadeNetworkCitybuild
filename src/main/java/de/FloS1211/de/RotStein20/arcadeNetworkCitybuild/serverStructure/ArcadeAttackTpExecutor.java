package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArcadeAttackTpExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage("Dieser Command kann nur von einem Spieler ausgeführt werden.");
    } else {
      ProxyManager.teleportToServer(player, "arcadeattack");
    }
    return true;
  }
}
