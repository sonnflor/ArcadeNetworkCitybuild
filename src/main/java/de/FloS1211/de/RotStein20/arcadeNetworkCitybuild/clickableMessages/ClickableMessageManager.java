package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position.PositionManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.ChatMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaManager;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.*;

public class ClickableMessageManager {
  public static final Cache<String, ClickAction> cache = Caffeine.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(10))
      .build();

  public static String registerClickableMessage(String eventType, boolean expiresAfterUse,List<String> args) {
    String token = UUID.randomUUID().toString();
    cache.put(token, new ClickAction(eventType, args.toArray(new String[0]), expiresAfterUse));
    return token;
  }

  public static boolean performAction(String token, CommandSender commandSender) {
    ClickAction clickAction = cache.getIfPresent(token);
    if (clickAction == null) {
      return false;
    }
    switch (clickAction.type()) {
      case "tpa" -> TpaManager.handleMessageClick(clickAction.args(),commandSender);
      case "pos" -> PositionManager.handleShareMessageClick(clickAction.args(),commandSender);
      case "openitem" -> ChatMessageManager.handleOpenItemMessageClick(clickAction.args(),commandSender);
      default -> {
        return false;
      }
    }
    if (clickAction.expiresAfterUse()) {
      cache.invalidate(token);
    }
    return true;
  }
}
