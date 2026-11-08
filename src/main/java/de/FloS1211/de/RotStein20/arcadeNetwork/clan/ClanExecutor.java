package de.FloS1211.de.RotStein20.arcadeNetwork.clan;

import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.SQLTable;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ClanExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(LanguageManager.get(Locale.ENGLISH, "general.chat.invalid-executor"));
      return true;
    }
    if (args.length == 0) return false;
    if (args[0].equalsIgnoreCase("create")) {
      if (args.length < 2) {
        LanguageManager.send(player, "general.chat.invalid-player");
        return true;
      }
      if (args[1].length() > 6) {
        LanguageManager.send(player, "clan.chat.create-failed-name");
        return true;
      }
      Integer col = null;
      if (args.length == 3) {
        col = ClanManager.colors.indexOf(args[2].toLowerCase(Locale.ROOT));
        if (col == -1) col = null;
      }
      try {
        ClanManager.createClan(args[1], player, col);
      } catch (IllegalArgumentException e) {
        LanguageManager.send(player,e.getMessage(), "clan", args[1]);
      }
    } else if (args[0].equalsIgnoreCase("delete")) {
      String clan = ClanManager.getClan(player.getUniqueId().toString());
      if (!ClanManager.isOwner(player, clan)) {
        LanguageManager.send(player, "clan.chat.delete_failed_no-permission",
            "clan", MiniMessage.miniMessage().serialize(ClanManager.getPrefix(clan)));
        return true;
      }
      ClanManager.deleteClan(clan);
    } else if (args[0].equalsIgnoreCase("leave")) {
      ClanManager.leaveClan(ClanManager.getClan(player.getUniqueId().toString()), player);
    } else if (args[0].equalsIgnoreCase("invite")) {
      if (args.length != 2) {
        LanguageManager.send(player, "general.chat.invalid-player");
        return true;
      }
      Player targetPlayer = Bukkit.getPlayerExact(args[1]);
      if (targetPlayer == null) {
        LanguageManager.send(player, "general.chat.invalid_player");
        return false;
      }
      ClanManager.inviteClan(player, targetPlayer);
    }
    return true;
  }
}
