package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InvseeExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
    if (!(sender instanceof Player player)) {
      LanguageManager.send(sender, "general.invalid-executor");
      return true;
    }

    if (args.length < 1) {
      return false;
    }

    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
    if (!(target.hasPlayedBefore() || target.isOnline())) {
      LanguageManager.send(sender, "general.invalid-player");
      return true;
    }
    boolean shouldReplace = player.hasPermission("ac.admin") && args.length == 2 && Objects.equals(args[1], "edit");
    if (target.isOnline()) {
      player.openInventory(InvseeManager.getOnlineInv(target.getPlayer(),shouldReplace, player));
    } else {
      player.openInventory(InvseeManager.getOfflineInv(target.getUniqueId(),shouldReplace, player));
    }
    return true;
  }
}
