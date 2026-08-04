package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NamecolorExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage("Dieser Command kann nur als Spieler ausgeführt werden!");
      return true;
    }
    player.openInventory(NamecolorManager.getNamecolorGUI(player).buildInventory());
    return true;
  }
}
