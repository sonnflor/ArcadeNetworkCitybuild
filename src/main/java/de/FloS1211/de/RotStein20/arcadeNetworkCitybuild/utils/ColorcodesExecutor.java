package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ColorcodesExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    commandSender.sendMessage("Color Codes:\n §4dark_red: §f&4\n §cred: §f&c\n §6gold: §f&6\n §eyellow: §f&e\n §2dark_green: §f&2\n §agreen: §f&a\n §baqua: §f&b\n §3dark_aqua: §f&3\n §1dark_blue: §f&1\n §9blue: §f&9\n §dlight_purple: §f&d\n §5dark_purple: §f&5\n white: &f\n §7gray: §f&7\n §8dark_gray: §f&8\n §0black: §f&0\n §rreset: §f&r\n §lfett: §r§f&l\n §okursiv: §r§f&o\n §nunterstrichen: §r§f&n\n §mdurchgestrichen: §r§f&m\n §kbugy§r§f: &k\n §7Item-Display: [item]\n §7Position: [pos]");
    return true;
  }
}
