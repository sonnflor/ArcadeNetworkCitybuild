package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LanguageManager {

  private static final MiniMessage miniMessage = MiniMessage.miniMessage();

  private static final Map<String, YamlConfiguration> languages = new HashMap<>();

  private static String defaultLanguage = "en";

  public static void load() {
    languages.clear();

    loadLanguage("de");
    loadLanguage("en");
  }

  private static void loadLanguage(String language) {
    ArcadeNetworkCitybuild.getInstance().saveResource("lang/" + language + ".yml", true);
    File file = new File(
        ArcadeNetworkCitybuild.getInstance().getDataFolder(),
        "lang/" + language + ".yml"
    );

    languages.put(
        language,
        YamlConfiguration.loadConfiguration(file)
    );
  }

  public static String getString(CommandSender player, String key, String... placeholders) {
    return PlainTextComponentSerializer.plainText().serialize(get(player, key, placeholders));
  }

  public static String getString(Player player, String key, String... placeholders) {
    return PlainTextComponentSerializer.plainText().serialize(get(player, key, placeholders));
  }

  public static String getString(Locale locale, String key, String... placeholders) {
    return PlainTextComponentSerializer.plainText().serialize(get(locale, key, placeholders));
  }

  public static Component get(CommandSender player, String key, String... placeholders) {
    if (player instanceof Player player1) return get(player1, key, placeholders);
    else {
      return get(Locale.GERMAN, key, placeholders);
    }
  }

  public static Component get(Player player, String key, String... placeholders) {
    return get(player.locale(), key, placeholders);
  }

  public static Component get(Locale locale, String key, String... placeholders) {
    YamlConfiguration configuration = getConfiguration(locale);

    if (configuration == null) {
      return Component.text("[" + key + "]");
    }

    String value = configuration.getString(key);

    if (value == null) {
      YamlConfiguration fallback = languages.get(defaultLanguage);

      if (fallback != null && !fallback.equals(configuration)) {
        value = fallback.getString(key);
      }
    }

    if (value == null) {
      ArcadeNetworkCitybuild.getInstance().getLogger().warning(
          "Missing translation: " + key
      );

      return Component.text("[" + key + "]");
    }

    return miniMessage.deserialize(
        value,
        createResolver(placeholders)
    );
  }

  public static List<Component> getList(
      Player player,
      String key,
      String... placeholders
  ) {
    return getList(player.locale(), key, placeholders);
  }

  public static List<Component> getList(
      Locale locale,
      String key,
      String... placeholders
  ) {

    YamlConfiguration configuration = getConfiguration(locale);

    List<String> values = configuration != null
        ? configuration.getStringList(key)
        : List.of();

    // Fallback auf Standardsprache
    if (values.isEmpty()) {
      YamlConfiguration fallback = languages.get(defaultLanguage);

      if (fallback != null) {
        values = fallback.getStringList(key);
      }
    }

    // Key nicht vorhanden
    if (values.isEmpty()) {
      ArcadeNetworkCitybuild.getInstance().getLogger().warning(
          "Missing translation list: " + key
      );

      return List.of(Component.text("[" + key + "]"));
    }

    return values.stream()
        .map(value -> miniMessage.deserialize(value, createResolver(placeholders)))
        .toList();
  }

  public static void send(
      CommandSender player,
      String key,
      String... placeholders
  ) {
    player.sendMessage(
        get(player, key, placeholders)
    );
  }

  public static void send(
      Player player,
      String key,
      String... placeholders
  ) {
    player.sendMessage(
        get(player, key, placeholders)
    );
  }

  private static YamlConfiguration getConfiguration(Locale locale) {
    String language = locale.getLanguage();

    YamlConfiguration configuration = languages.get(language);

    if (configuration == null) {
      configuration = languages.get(defaultLanguage);
    }

    return configuration;
  }

  private static TagResolver createResolver(String... placeholders) {
    if (placeholders.length % 2 != 0) {
      throw new IllegalArgumentException(
          "Placeholders müssen aus Name/Wert-Paaren bestehen."
      );
    }

    TagResolver.Builder resolver = TagResolver.builder();

    for (int i = 0; i < placeholders.length; i += 2) {
      resolver.resolver(
          Placeholder.unparsed(
              placeholders[i],
              placeholders[i + 1]
          )
      );
    }

    return resolver.build();
  }
}