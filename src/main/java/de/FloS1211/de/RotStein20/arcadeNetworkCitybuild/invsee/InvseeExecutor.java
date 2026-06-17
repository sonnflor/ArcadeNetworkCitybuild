package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvseeExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(MessageManager.get("general-invalid-executor"));
      return true;
    }

    Player player = (Player) sender;

    if (args.length != 1) {
      return false;
    }

    Player target = Bukkit.getPlayerExact(args[0]);
    if (target == null) {
      player.sendMessage(MessageManager.get("general-invalid-player"));
      return true;
    }

    // create a read-only copy of the target's inventory including armor + offhand
    Inventory copy = Bukkit.createInventory(new CustomGuiHolder(), 45, Component.text("Invsee: " + target.getName()));
    // main contents (0..35)
    ItemStack[] main = target.getInventory().getContents();
    for (int i = 0; i < main.length && i < 36; i++) {
      if (main[i] != null) copy.setItem(i, main[i]);
    }
    // armor contents: boots, leggings, chestplate, helmet -> slots 36..39
    ItemStack[] armor = target.getInventory().getArmorContents();
    for (int i = 0; i < armor.length && i < 4; i++) {
      if (armor[i] != null) copy.setItem(36 + i, armor[i]);
    }
    // off-hand -> slot 40
    ItemStack off = target.getInventory().getItemInOffHand();
    if (off != null) copy.setItem(40, off);
    player.openInventory(copy);
    player.sendMessage(MessageManager.get("invsee-open", java.util.Map.of("player", target.getName())));
    return true;
  }
}
