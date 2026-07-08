package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MuteinfoExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    if (args.length < 1) {
      return false;
    }
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
    if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
      commandSender.sendMessage(MessageManager.get("general-invalid-player"));
      return false;
    }
    String uuid = targetPlayer.getUniqueId().toString();
    SQLTable playerData = DatabaseManager.getTable("player_data","uuid=?", List.of(uuid));
    if (playerData.isEmpty() || !playerData.getBooleanValue("is_muted",0)) {
      commandSender.sendMessage(MessageManager.get("mute-muteinfo-isnt-muted", Map.of("player", targetPlayer.getName())));
      return true;
    }
    String muteDataString = playerData.getStringValue("mute_data",0);
    if (muteDataString == "") {
      return true;
    }
    MuteEntry mute = MuteEntry.fromString(muteDataString);
    String playerName = targetPlayer.getName();
    String unmuteTime = Utils.formatDate(mute.getMuteTimestamp()+ mute.getMuteDuration());
    String muteDuration = Utils.formatDuration(mute.getMuteDuration());
    String reason = mute.getReason();
    String muteTimestamp = Utils.formatDate(mute.getMuteTimestamp());
    String muterUuid = mute.getMuterUuid();
    String muterName = Bukkit.getOfflinePlayer(UUID.fromString(mute.getMuterUuid())).getName();
    if (commandSender instanceof Player player) {
      Inventory gui = Bukkit.createInventory(new CustomGuiHolder(),9, Component.text("Mute Informationen"));
      gui.addItem(createItemStack("Spieler Name",playerName,Material.PLAYER_HEAD));
      gui.addItem(createItemStack("Spieler UUID",uuid,Material.NAME_TAG));
      gui.addItem(createItemStack("Unmute Zeitpunkt",unmuteTime,Material.CLOCK));
      gui.addItem(createItemStack("Mute Dauer",muteDuration,Material.CLOCK));
      gui.addItem(createItemStack("Reason",reason,Material.NAME_TAG));
      gui.addItem(createItemStack("Mute Zeitpunkt",muteTimestamp,Material.CLOCK));
      gui.addItem(createItemStack("Muter Name",muterName,Material.OBSERVER));
      gui.addItem(createItemStack("Muter UUID",muterUuid,Material.OBSERVER));
      player.openInventory(gui);
    } else {
      Bukkit.getLogger().info("player-name=\""+playerName+"\"");
      Bukkit.getLogger().info("player-uuid=\""+uuid+"\"");
      Bukkit.getLogger().info("unmute-time=\""+ unmuteTime +"\"");
      Bukkit.getLogger().info("mute-duration=\""+muteDuration+"\"");
      Bukkit.getLogger().info("reason=\""+reason+"\"");
      Bukkit.getLogger().info("mute-timestamp=\""+muteTimestamp+"\"");
      Bukkit.getLogger().info("muter-uuid=\""+muterUuid+"\"");
      Bukkit.getLogger().info("muter-name=\""+muterName+"\"");
    }
    return true;
  }

  private ItemStack createItemStack(String itemName, String lore, Material material) {
    ItemStack itemStack = new ItemStack(material);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.lore(List.of(
        Component.text(lore).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false)
    ));
    itemMeta.itemName(Component.text(itemName).color(NamedTextColor.WHITE));
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
