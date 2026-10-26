package de.FloS1211.de.RotStein20.arcadeNetwork.perks;

import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PerkExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      LanguageManager.send(commandSender, "general.invalid-executor");
      return true;
    }
    PerkManager.openPerkMenu(player);
    return true;
  }
}
