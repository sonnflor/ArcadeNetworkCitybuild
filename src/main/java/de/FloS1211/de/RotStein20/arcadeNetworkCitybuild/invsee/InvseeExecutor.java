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
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

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

    // create a nicer read-only GUI (54 slots)
    Inventory copy = Bukkit.createInventory(new CustomGuiHolder(), 54, Component.text("Invsee: " + target.getName()));

    // fill background with gray glass panes
    ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    ItemMeta fillerMeta = filler.getItemMeta();
    if (fillerMeta != null) {
      fillerMeta.setDisplayName(" ");
      filler.setItemMeta(fillerMeta);
    }
    for (int s = 0; s < 54; s++) copy.setItem(s, filler);

    // main inventory -> slots 9..44 (36 slots)
    ItemStack[] main = target.getInventory().getContents();
    for (int i = 0; i < main.length && i < 36; i++) {
      if (main[i] != null) copy.setItem(9 + i, main[i]);
    }

    // armor contents: boots, leggings, chestplate, helmet -> slots 45..48
    ItemStack[] armor = target.getInventory().getArmorContents();
    for (int i = 0; i < armor.length && i < 4; i++) {
      if (armor[i] != null) copy.setItem(45 + i, armor[i]);
    }

    // off-hand -> slot 49
    ItemStack off = target.getInventory().getItemInOffHand();
    if (off != null) copy.setItem(49, off);

    // player head in bottom-right (slot 53)
    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skull = (SkullMeta) head.getItemMeta();
    if (skull != null) {
      skull.setOwningPlayer(target);
      skull.setDisplayName("§a" + target.getName());
      List<String> lore = new ArrayList<>();
      lore.add("§7Rüstung & Offhand werden angezeigt");
      skull.setLore(lore);
      head.setItemMeta(skull);
      copy.setItem(53, head);
    }

    // close button (slot 52)
    ItemStack close = new ItemStack(Material.BARRIER);
    ItemMeta closeMeta = close.getItemMeta();
    if (closeMeta != null) {
      closeMeta.setDisplayName("§cSchließen");
      close.setItemMeta(closeMeta);
      copy.setItem(52, close);
    }
    player.openInventory(copy);
    player.sendMessage(MessageManager.get("invsee-open", java.util.Map.of("player", target.getName())));
    return true;
  }
}
