package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
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
    player.showDialog(Utils.getConsentDialog(player, Component.text("Sind sie sicher, dass sie diesen Rang-Coupon einlösen möchten?"), p -> {
      String rank = couponName.substring(5);
      try {
        RankManager.improveRank(player.getUniqueId().toString(), rank);
        player.sendMessage("§f[§aRank§f]§7 Dein Rang wurde auf " + rank.replace('_',' ') + " gesetzt");
        if (!player.getGameMode().equals(org.bukkit.GameMode.CREATIVE)) itemStack.setAmount(itemStack.getAmount() - 1);
      } catch (IllegalArgumentException e) {
        player.sendMessage("§f[§aRank§f]§7 Ein kritischer Fehler ist aufgetreten. Bitte informiere einen Supporter oder Admin");
      }
    }));
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
