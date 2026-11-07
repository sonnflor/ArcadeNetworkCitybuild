package de.FloS1211.de.RotStein20.arcadeNetwork.clan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClanTabCompleter implements TabCompleter {
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    List<String> result = new ArrayList<>();
    if (!(commandSender instanceof Player player)) return result;
    String uuid = player.getUniqueId().toString();
    if (args.length == 1) {
      result.add("create");
      String clan = ClanManager.getClan(uuid);
      if (!clan.isBlank()) {
        result.add("leave");
        if (ClanManager.isOwner(player, clan))
          result.addAll(List.of("invite", "delete"));
      }
    }
    if (args.length == 3) {
      if (args[0] == "create") {
        for (String item : ClanManager.colors) {
          if (item.toLowerCase().startsWith(args[2].toLowerCase())) {
            result.add(item);
          }
        }
      }
    }
    return result;
  }
}
