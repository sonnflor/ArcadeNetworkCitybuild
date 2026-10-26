package de.FloS1211.de.RotStein20.arcadeNetwork.coins;

import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class PayAllExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length != 1) {
      return false;
    }
    BigDecimal amountPerPlayer;
    try {
      amountPerPlayer = new BigDecimal(args[0]);
    } catch (NumberFormatException e) {
      LanguageManager.send(commandSender, "general.invalid-number");
      return false;
    }
    if (amountPerPlayer.scale() > 2) {
      LanguageManager.send(commandSender, "general.invalid-number");
      return true;
    }
    if (amountPerPlayer.compareTo(BigDecimal.ZERO) < 0) {
      LanguageManager.send(commandSender, "coins.chat.pay-failed-negative");
      return true;
    }
    BigDecimal amount = new BigDecimal(Bukkit.getOnlinePlayers().size()).multiply(amountPerPlayer);
    if (commandSender instanceof Player player) {
      if (CoinsManager.getCoins(player.getUniqueId().toString()).compareTo(amount) < 0) {
        LanguageManager.send(commandSender, "coins.chat.pay-failed-poor");
        return true;
      }
    }
    if (commandSender instanceof Player player) {
      CoinsManager.addCoins(player.getUniqueId().toString(), amount.negate());
    }

    for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
      CoinsManager.addCoins(targetPlayer.getUniqueId().toString(),amountPerPlayer);
      SidebarManager.setSidebar(targetPlayer);
      LanguageManager.send(targetPlayer, "coins.chat.payall-succeed", "amount", amountPerPlayer+"", "player", commandSender.getName());
      //targetPlayer.sendMessage(Component.text("[").color(NamedTextColor.WHITE).append(Component.text("Pay").color(NamedTextColor.GREEN).append(Component.text("] ").color(NamedTextColor.WHITE).append(Component.text(commandSender.getName() + " hat allen " + amountPerPlayer + " Coins gegeben ").color(NamedTextColor.GRAY).append(Component.text("[thx zurück senden]").color(NamedTextColor.GREEN).clickEvent(ClickEvent.callback(audience -> {((Player)audience).chat("thx " + commandSender.getName());})).hoverEvent(HoverEvent.showText(text("Einfach klicken", NamedTextColor.GRAY))))))));
    }
    LanguageManager.send(commandSender, "coins.chat.pay-succeed");

    return true;
  }
}
