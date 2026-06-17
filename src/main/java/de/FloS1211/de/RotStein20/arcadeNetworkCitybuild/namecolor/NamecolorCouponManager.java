package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.RankManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NamecolorCouponManager implements Listener {
  @EventHandler
  public static void onCouponUse(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_AIR) {
      return;
    }
    ItemStack itemStack = event.getItem();
    if (itemStack == null || itemStack.getType() != Material.BOOK) {
      return;
    }
    if (!(itemStack.hasItemMeta())) {
      return;
    }
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (!itemMeta.getPersistentDataContainer().has(Utils.key_custom)) return;
    String couponName = itemMeta.getPersistentDataContainer().get(Utils.key_custom, PersistentDataType.STRING);
    if (!couponName.startsWith("namecolor_")) {
      return;
    }
    player.showDialog(Utils.getConsentDialog(player, Component.text("Sind sie sicher, dass sie diesen Namensfarben-Coupon einlösen möchten?"), p -> {
      String colName = couponName.substring(10);
      try {
        NamecolorManager.unlockNamecolor(player.getUniqueId().toString(), colName);
        player.sendMessage("§f[§aNamecolor§f]§7 Du hast die Namensfarbe " + colName.replace('_',' ') + " freigeschaltet. Aktiviere sie mit /namensfarben");
        if (!player.getGameMode().equals(org.bukkit.GameMode.CREATIVE)) itemStack.setAmount(itemStack.getAmount() - 1);
      } catch (IllegalArgumentException e) {
        player.sendMessage("§f[§aNamecolor§f]§7 Ein kritischer Fehler ist aufgetreten. Bitte informiere einen Supporter oder Admin");
      }
    }));
  }

  public static ItemStack getCoupon(String colName) {
    ItemStack itemStack = new ItemStack(Material.BOOK);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(Component.text("§aNamensfarben-Coupon: " + colName.replace('_',' ')));
    itemMeta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, "namecolor_" + colName);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
