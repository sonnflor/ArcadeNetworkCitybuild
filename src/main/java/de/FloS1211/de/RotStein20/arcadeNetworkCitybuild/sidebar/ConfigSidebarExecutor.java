package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigSidebarExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
      return true;
    }
    if (args.length != 2) {
      return false;
    }
    String item = args[0];
    if (!SidebarManager.features.contains(item)) {
      player.sendMessage(MessageManager.get("sidebar-invalid-feature"));
      return false;
    }
    String mode = args[1];
    if (!List.of("on","off").contains(mode)) {
      player.sendMessage(MessageManager.get("sidebar-invalid-mode"));
      return false;
    }
    String oldFeatures = player.getPersistentDataContainer().get(Utils.key_sidebar, PersistentDataType.STRING);
    if (oldFeatures == null) oldFeatures = "";
    if (mode.equals("on")) {
      if (!oldFeatures.contains(item)) {
        player.getPersistentDataContainer().set(Utils.key_sidebar,PersistentDataType.STRING,oldFeatures+","+item);
      }
    } else {
      if (oldFeatures.contains(item)) {
        player.getPersistentDataContainer().set(Utils.key_sidebar,PersistentDataType.STRING,oldFeatures.replace(item,"").replace(",,",","));
      }
    }
    return true;
  }
}
