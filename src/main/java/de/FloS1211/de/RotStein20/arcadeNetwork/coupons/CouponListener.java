package de.FloS1211.de.RotStein20.arcadeNetwork.coupons;

import de.FloS1211.de.RotStein20.arcadeNetwork.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CouponListener implements Listener {
  @EventHandler
  public void onRightClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
    ItemStack itemStack = event.getItem();
    if (itemStack == null || itemStack.getType() != Material.BOOK) return;
    if (!(itemStack.hasItemMeta())) return;
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (!itemMeta.getPersistentDataContainer().has(Utils.key_custom)) return;
    String couponData = itemMeta.getPersistentDataContainer().get(Utils.key_custom, PersistentDataType.STRING);
    if (couponData == null || !couponData.startsWith("coupon_")) return;
    String[] couponDataList = couponData.split("_");
    String couponName = couponDataList[1];
    String couponId = couponDataList[2];
    /*
    YamlConfiguration config = new YamlConfiguration();
    config.set("item", itemStack);
    player.openInventory(Gui.getConfirmationGui(Map.of("couponName",couponName,"couponItem",config.saveToString()), new PerkGuiButtonExecutor(), LanguageManager.getString(player, "perk.gui.confirmation.accept")).buildInventory());
    */
  }
}
