package de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Locale;

public class FeatureManager {
  ConfigurationSection features;

  public Boolean getState(String feature) {
    return features.getBoolean(feature);
  }

  public FeatureManager(FileConfiguration config) {
    features = config.getConfigurationSection("features");
  }

  public static void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter, boolean enabled) {
    PluginCommand command = ArcadeNetwork.getInstance().getCommand(name);
    if (command == null || !command.isRegistered()) return;
    if (enabled) {
      if (executor != null) command.setExecutor(executor);
      if (tabCompleter != null) command.setTabCompleter(tabCompleter);
    } else {
      FeatureVisibilityListener.disabledCommands.add(command.getName().toLowerCase(Locale.ROOT));
      for (String alias : command.getAliases()) {
        FeatureVisibilityListener.disabledCommands.add(
            alias.toLowerCase(Locale.ROOT)
        );
      }
      command.setExecutor((sender, cmd, label, args) -> {
        sender.sendMessage("Dieser Command ist deaktiviert.");
        return true;
      });
      command.setTabCompleter((sender, cmd, alias, args) -> List.of());
    }
  }
}
