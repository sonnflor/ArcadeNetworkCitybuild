package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InvseeTabCompleter implements TabCompleter {
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> res = new ArrayList<>();
    if (args.length == 1) {
      for (Player p : Bukkit.getOnlinePlayers()) {
        if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
          res.add(p.getName());
        }
      }
    }
    return res;
  }
}
