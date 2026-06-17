package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;

public class ChatMessageManager implements Listener {
  @EventHandler
  public void onItemInChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    Component msg = event.message();
    //implement Colors
    if (player.hasPermission("arcadecraft.rank.experte")) {
      if (msg instanceof TextComponent text) {
        msg = LegacyComponentSerializer.legacyAmpersand().deserialize(text.content());
      }
    }
    //implement Item
    msg = msg.replaceText(builder -> {
      builder
          .match(Pattern.compile("\\[Item]|\\(Item\\)|\\[i]|\\(i\\)", Pattern.CASE_INSENSITIVE))
          .replacement(itemStack.displayName().color(NamedTextColor.GRAY).clickEvent(ClickEvent.callback(audience -> {
                openItem(itemStack,(Player) audience);
              }))
              .hoverEvent(HoverEvent.showText(text("Klicke, um dir das Item anzusehen", NamedTextColor.GRAY))
              ));
    });
    //remove forbidden Words

    event.message(msg);
  }
  private static void openItem(ItemStack itemStack, Player player) {
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
