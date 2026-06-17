package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.SidebarManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public class AuszahlenExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length < 1) {
      return false;
    }
    int coinValue = 1;
    if (args.length >= 2) {
      try {
        coinValue = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        commandSender.sendMessage(MessageManager.get("general-invalid-number"));
        return true;
      }
    }

    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage("§f[§aCoins§f]§7 Kann nur von einem Spieler ausgeführt werden!");
      return true;
    }
    String uuid = player.getUniqueId().toString();
    Inventory inventory = player.getInventory();

    BigDecimal amount;
    try {
      amount = new BigDecimal(args[0]);
    } catch (NumberFormatException e) {
      commandSender.sendMessage(MessageManager.get("general-invalid-number"));
      return true;
    }
    if (amount.scale() > 2) {
      commandSender.sendMessage(MessageManager.get("general-invalid-number"));
      return true;
    }
    BigDecimal playerCoins = CoinsManager.getCoins(uuid);
    if (amount.compareTo(playerCoins)>0) {
      commandSender.sendMessage(MessageManager.get("coins-auszahlen-failed-poor"));
      return true;
    }
    if (amount.doubleValue() <= 0) {
      commandSender.sendMessage(MessageManager.get("coins-auszahlen-failed-negative"));
      return true;
    }
    int itemAmount = (int) Math.floor(amount.doubleValue() / coinValue);
    int stackAmount = (int) Math.floor((double) itemAmount / 64);
    CoinsManager.addCoins(uuid,BigDecimal.valueOf((long) itemAmount *coinValue).negate());
    SidebarManager.setSidebar(player);
    ItemStack coinStackItem = CoinsManager.getCoinItem(amount, 64);
    for (int i = 0; i < stackAmount; i++) {
      if (inventory.firstEmpty() == -1) {
        Item item = player.getWorld().dropItem(player.getLocation(), coinStackItem.clone());
        item.setPickupDelay(0);
      } else {
        inventory.addItem(coinStackItem.clone());
      }
    }
    if (itemAmount % 64 != 0) {
      ItemStack lastStack = CoinsManager.getCoinItem(BigDecimal.valueOf(coinValue), itemAmount % 64);
      if (inventory.firstEmpty() == -1) {
        Item item = player.getWorld().dropItem(player.getLocation(), lastStack);
        item.setPickupDelay(0);
      } else {
        inventory.addItem(lastStack);
      }
    }
    player.sendMessage(MessageManager.get("coins-auszahlen-succeed", Map.of("amount",amount+"","coinValue",coinValue+"")));
    return true;
  }
}
