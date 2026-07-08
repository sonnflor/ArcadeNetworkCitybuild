package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.vanish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage("Dieser Befehl ist nur für Spieler.");
      return true;
    }
    VanishManager.hidePlayer(player);
    return true;
  }
}
