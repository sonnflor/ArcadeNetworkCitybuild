package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaManager;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ClickableMessageManager {
  public static final Map<String, List<String>> validTokens = new HashMap<>();

  public static String registerClickableMessage(String eventType, List<String> args) {
    String token = UUID.randomUUID().toString();
    List<String> resultArgs = new ArrayList<>();
    resultArgs.add(eventType);
    resultArgs.addAll(args);
    validTokens.put(token, resultArgs);
    return token;
  }

  public static boolean performAction(String token, CommandSender commandSender) {
    List<String> args = validTokens.get(token);
    if (args == null) {
      return false;
    }
    String type = args.get(0);
    switch (type) {
      case "tpa" -> TpaManager.handleMessageClick(args.subList(1, args.size()),commandSender);
      case null, default -> {
        return false;
      }
    }
    validTokens.remove(token);
    return true;
  }
}
