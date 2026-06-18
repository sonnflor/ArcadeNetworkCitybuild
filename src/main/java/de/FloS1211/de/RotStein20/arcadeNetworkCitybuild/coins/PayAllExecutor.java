package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar.SidebarManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import static net.kyori.adventure.text.Component.text;

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
      commandSender.sendMessage(MessageManager.get("general-invalid-number"));
      return false;
    }
    if (amountPerPlayer.scale() > 2) {
      commandSender.sendMessage(MessageManager.get("general-invalid-number"));
      return true;
    }
    if (amountPerPlayer.compareTo(BigDecimal.ZERO) < 0) {
      commandSender.sendMessage(MessageManager.get("coins-pay-negative"));
      return true;
    }
    BigDecimal amount = new BigDecimal(Bukkit.getOnlinePlayers().size()).multiply(amountPerPlayer);
    if (commandSender instanceof Player player) {
      if (CoinsManager.getCoins(player.getUniqueId().toString()).compareTo(amount) < 0) {
        commandSender.sendMessage("§f[§aPay§f]§7 Du hast nicht genug Coins (" + amount + ")");
        return true;
      }
    }
    if (commandSender instanceof Player player) {
      CoinsManager.addCoins(player.getUniqueId().toString(), amount.negate());
    }

    for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
      CoinsManager.addCoins(targetPlayer.getUniqueId().toString(),amountPerPlayer);
      SidebarManager.setSidebar(targetPlayer);
      targetPlayer.sendMessage(Component.text("[").color(NamedTextColor.WHITE).append(Component.text("Pay").color(NamedTextColor.GREEN).append(Component.text("] ").color(NamedTextColor.WHITE).append(Component.text(commandSender.getName() + " hat allen " + amountPerPlayer + " Coins gegeben ").color(NamedTextColor.GRAY).append(Component.text("[thx zurück senden]").color(NamedTextColor.GREEN).clickEvent(ClickEvent.callback(audience -> {((Player)audience).chat("thx " + commandSender.getName());})).hoverEvent(HoverEvent.showText(text("Einfach klicken", NamedTextColor.GRAY))))))));
    }
    commandSender.sendMessage("§f[§aPay§f]§7 Transaktion wurde erfolgreich abgeschlossen");

    return true;
  }
}
