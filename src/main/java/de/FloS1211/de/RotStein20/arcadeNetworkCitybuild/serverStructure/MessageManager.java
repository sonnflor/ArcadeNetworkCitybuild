package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class MessageManager {

  private static FileConfiguration messages;

  public static void load() {
    File file = new File(ArcadeNetworkCitybuild.getInstance().getDataFolder(), "messages.yml");
    messages = YamlConfiguration.loadConfiguration(file);
  }

  public static String getRaw(String key) {
    return messages.getString(key,key);
  }

  public static Component get(String key) {
    MiniMessage miniMessage = MiniMessage.miniMessage();
    return miniMessage.deserialize(getRaw(key));
  }

  public static Component get(String key, Map<String,String> placeholders) {
    String message = getRaw(key);
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      message = message.replace(
          "{" + entry.getKey() + "}",
          entry.getValue()
      );
    }
    MiniMessage miniMessage = MiniMessage.miniMessage();
    return miniMessage.deserialize(message);
  }
}
