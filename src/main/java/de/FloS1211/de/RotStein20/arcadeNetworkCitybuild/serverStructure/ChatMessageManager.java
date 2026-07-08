package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.ClickableMessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;

public class ChatMessageManager implements Listener {
  private static final Pattern POS_PATTERN =
      Pattern.compile("\\[(?i:pos)]|\\((?i:pos)\\)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ITEM_PATTERN =
      Pattern.compile("\\[Item]|\\(Item\\)|\\[i]|\\(i\\)", Pattern.CASE_INSENSITIVE);
  @EventHandler
  public void onChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    Component message = event.message().color(NamedTextColor.GRAY);
    //getPrefix
    Component prefix = player.displayName().append(Component.text(" >> ",NamedTextColor.GRAY));
    //implement Colors
    if (player.hasPermission("arcadecraft.rank.experte")) {
      if (message instanceof TextComponent text) {
        message = LegacyComponentSerializer.legacyAmpersand().deserialize("&7"+text.content());
      }
    }
    //implement Item
    message = message.replaceText(builder -> {
      ItemStack itemStack = player.getInventory().getItemInMainHand();
      Component replacement;
      if (itemStack.isEmpty() || itemStack.getType().equals(Material.AIR)) replacement = Component.text("[air]");
      else {
        String token = ClickableMessageManager.registerClickableMessage("openitem", false, List.of(Base64.getEncoder().encodeToString(itemStack.serializeAsBytes())));
        replacement = Component.text("",NamedTextColor.GRAY)
            .append(itemStack.displayName()).clickEvent(ClickEvent.runCommand("/performaction " + token))
            .hoverEvent(HoverEvent.showText(text("Klicke, um dir das Item anzusehen", NamedTextColor.GRAY)));
      }
      builder
          .match(ITEM_PATTERN)
          .replacement(replacement);
    });
    //implement pos
    Location loc = player.getLocation();
    message = message.replaceText(builder -> builder
              .match(POS_PATTERN)
              .replacement(Component.text(
                  "x: "+loc.getBlockX()
                      + " y: "+loc.getBlockY()
                      + " z: "+loc.getBlockZ(),
                      NamedTextColor.GREEN)
                  .clickEvent(ClickEvent.suggestCommand("/tpa me_to "+player.getName()))
                  .hoverEvent(HoverEvent.showText(
                      Component.text("Klicke, um eine TPA zu senden",NamedTextColor.GRAY)
                  )))
    );
    //remove forbidden Words (future feature)
    //send to proxy
    message = prefix.append(message);
    event.setCancelled(true);
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(MiniMessage.miniMessage().serialize(message));

    player.sendPluginMessage(ArcadeNetworkCitybuild.getInstance(), "arcadenetwork:chat", out.toByteArray());
  }
    public static void handleOpenItemMessageClick(String[] args, CommandSender commandSender) {
      if (!(commandSender instanceof Player player)) {
        commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
        return;
      }
      ItemStack itemStack = ItemStack.deserializeBytes(Base64.getDecoder().decode(args[0]));
      Inventory gui = Bukkit.createInventory(new CustomGuiHolder(), 27, Component.text("Item: ").append(Component.text(PlainTextComponentSerializer.plainText().serialize(itemStack.displayName()))));
      ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
      ItemMeta glassMeta = glassPane.getItemMeta();
      glassMeta.displayName(Component.text(""));
      glassPane.setItemMeta(glassMeta);
      for (int i = 0; i < 27; i++) {
        gui.setItem(i, glassPane);
      }
      gui.setItem(13, itemStack);
      player.openInventory(gui);
    }
}
