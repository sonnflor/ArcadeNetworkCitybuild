package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorGuiButtonExecutor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class RankCouponManager implements Listener {
  @EventHandler
  public static void onCouponUse(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
    ItemStack itemStack = event.getItem();
    if (itemStack == null || itemStack.getType() != Material.BOOK) return;
    if (!(itemStack.hasItemMeta())) return;
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (!itemMeta.getPersistentDataContainer().has(Utils.key_custom)) return;
    String couponName = itemMeta.getPersistentDataContainer().get(Utils.key_custom, PersistentDataType.STRING);
    if (!couponName.startsWith("rank_")) {
      return;
    }
    YamlConfiguration config = new YamlConfiguration();
    config.set("item", itemStack);
    player.openInventory(Gui.getConfirmationGui(Map.of("couponName",couponName,"couponItem",config.saveToString()), new NamecolorGuiButtonExecutor(), "Akzeptiere, um den Rang-Gutschein einzulösen").buildInventory());
  }

  public static ItemStack getRankCoupon(String rank) {
    ItemStack itemStack = new ItemStack(Material.BOOK);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(Component.text("§aRang-Coupon: " + rank.replace('_',' ')));
    itemMeta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, "rank_" + rank);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
