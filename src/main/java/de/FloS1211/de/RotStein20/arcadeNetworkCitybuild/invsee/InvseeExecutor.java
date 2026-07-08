package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InvseeExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(MessageManager.get("general-invalid-executor"));
      return true;
    }

    Player player = (Player) sender;

    if (args.length < 1) {
      return false;
    }

    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
    if (!(target.hasPlayedBefore() || target.isOnline())) {
      player.sendMessage(MessageManager.get("general-invalid-player"));
      return true;
    }
    boolean shouldReplace = player.hasPermission("ac.admin") && args.length == 2 && Objects.equals(args[1], "edit");
    if (target.isOnline()) {
      player.openInventory(InvseeManager.getOnlineInv(target.getPlayer(),shouldReplace));
    } else {
      player.openInventory(InvseeManager.getOfflineInv(target.getUniqueId(),shouldReplace));
    }
    return true;
  }
}
