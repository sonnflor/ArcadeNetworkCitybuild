package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class PayExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length != 2) {
      return false;
    }
    if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && !Bukkit.getOfflinePlayer(args[0]).isOnline()) {
      LanguageManager.send(commandSender, "general.invalid-player");
      return false;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    BigDecimal amount;
    try {
      amount = new BigDecimal(args[1]);
    } catch (NumberFormatException e) {
      LanguageManager.send(commandSender, "general.invalid-number");
      return false;
    }
    if (amount.scale() > 2) {
      LanguageManager.send(commandSender, "general.invalid-number");
      return true;
    }
    if (commandSender instanceof Player player) {
      if (!player.getGameMode().equals(GameMode.CREATIVE)) {
        if (CoinsManager.getCoins(player.getUniqueId().toString()).compareTo(amount) < 0) {
          LanguageManager.send(commandSender, "coins.chat.pay-failed-poor", "amount", amount+"");
          return true;
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
          LanguageManager.send(commandSender, "coins.chat.pay-failed-steel", "amount", amount+"");
          return true;
        }
      }
    }
    CoinsManager.addCoins(targetPlayer.getUniqueId().toString(), amount);
    if ((commandSender instanceof Player player)&&!player.getGameMode().equals(GameMode.CREATIVE)) {
      CoinsManager.addCoins(player.getUniqueId().toString(),amount.negate());
    }
    LanguageManager.send(commandSender, "coins.chat.pay-succeed", "amount", amount+"");
    if (targetPlayer.isOnline()) {
      LanguageManager.send(((Player) targetPlayer), "coins.chat.pay-succeed-target", "player", commandSender.getName(),"amount", amount+"");
      SidebarManager.setSidebar((Player) targetPlayer);
    }
    return true;
  }
}
