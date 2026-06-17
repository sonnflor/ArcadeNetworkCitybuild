package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinsManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.RankCouponManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GetCustomItemExecutor implements CommandExecutor {
  public static List<String> validItems = List.of("coin","rankbook","namecolorbook");
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length < 1) return false;
    String itemName = args[0];
    String[] itemProperties = Arrays.asList(args).subList(1, args.length).toArray(new String[0]);
    if (!validItems.contains(itemName.toLowerCase())) {
      commandSender.sendMessage("Ungültiger Itemname. Verfügbare Items: " + String.join(", ", validItems));
      return true;
    }
    if (itemName.equalsIgnoreCase("coin")) {
      ((Player)commandSender).getInventory().addItem(CoinsManager.getCoinItem(BigDecimal.valueOf(Double.parseDouble(itemProperties[0])), Integer.parseInt(itemProperties[2])));
    } else if (itemName.equalsIgnoreCase("rankbook")) {
      ((Player)commandSender).getInventory().addItem(RankCouponManager.getRankCoupon(itemProperties[0]));
    } else if (itemName.equalsIgnoreCase("namecolorbook")) {
      ((Player)commandSender).getInventory().addItem(NamecolorCouponManager.getCoupon(itemProperties[0]));
    }
    return true;
  }
}
