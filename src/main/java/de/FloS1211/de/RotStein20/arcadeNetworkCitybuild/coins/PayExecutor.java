package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.SidebarManager;
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
      commandSender.sendMessage("§f[§aPay§f]§7 Bitte gib einen gültigen Spieler an");
      return false;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    BigDecimal amount;
    try {
      amount = new BigDecimal(args[1]);
    } catch (NumberFormatException e) {
      commandSender.sendMessage("§f[§aPay§f]§7 Bitte gib eine gültige Zahl an");
      return false;
    }
    if (amount.scale() > 2) {
      commandSender.sendMessage("§f[§aPay§f]§7 Bitte gib eine gültige Zahl an");
      return true;
    }
    if (commandSender instanceof Player player) {
      if (!player.getGameMode().equals(GameMode.CREATIVE)) {
        if (CoinsManager.getCoins(player.getUniqueId().toString()).compareTo(amount) < 0) {
          commandSender.sendMessage("§f[§aPay§f]§7 Du hast nicht genug Coins");
          return true;
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
          commandSender.sendMessage("§f[§aPay§f]§7 Du kannst keine Coins klauen");
          return true;
        }
      }
    }
    CoinsManager.addCoins(targetPlayer.getUniqueId().toString(), amount);
    if ((commandSender instanceof Player player)&&!player.getGameMode().equals(GameMode.CREATIVE)) {
      CoinsManager.addCoins(player.getUniqueId().toString(),amount.negate());
    }
    commandSender.sendMessage("§f[§aPay§f]§7 Transaktion wurde erfolgreich abgeschlossen");
    if (targetPlayer.isOnline()) {
      ((Player) targetPlayer).sendMessage("§f[§aPay§f]§7 " + commandSender.getName() + " hat dir " + amount + " Coins gegeben");
      SidebarManager.setSidebar((Player) targetPlayer);
    }
    return true;
  }
}
