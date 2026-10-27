package de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class FeatureVisibilityListener implements Listener {

  public static final Set<String> disabledCommands = new HashSet<>();

  private final Set<String> hiddenCommands;
  private final String namespace;

  public FeatureVisibilityListener(ArcadeNetwork plugin) {
    this.namespace =
        (plugin.namespace() + ":").toLowerCase(Locale.ROOT);

    Set<String> computed = new HashSet<>();

    for (String name : disabledCommands) {
      addCommandVariants(computed, name);

      PluginCommand command = plugin.getCommand(name);

      if (command == null) {
        plugin.getLogger().warning(
            "Deaktivierter Command nicht gefunden: " + name
        );
        continue;
      }

      for (String alias : command.getAliases()) {
        addCommandVariants(computed, alias);
      }
    }

    this.hiddenCommands = Set.copyOf(computed);
  }

  private void addCommandVariants(Set<String> target, String name) {
    String normalized = name.toLowerCase(Locale.ROOT);

    target.add(normalized);
    target.add(namespace + normalized);
  }

  @EventHandler
  public void onPlayerCommandSend(PlayerCommandSendEvent event) {
    if (hiddenCommands.isEmpty()) {
      return;
    }

    event.getCommands().removeIf(command ->
        hiddenCommands.contains(
            command.toLowerCase(Locale.ROOT)
        )
    );
  }
}