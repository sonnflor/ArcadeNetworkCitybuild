package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.List;

public class CoinsManager {
  public static BigDecimal getCoins(String uuid) {
    SQLTable playerData = new SQLTable("player_data","uuid = ?", List.of(uuid));
    if (!playerData.isEmpty()) {
      return BigDecimal.valueOf(playerData.getIntValue("coins", 0) / 100.0);
    } else {
      return BigDecimal.ZERO;
    }
  }

  public static void setCoins(String uuid, BigDecimal amount) {
    int subCoins = amount.multiply(new BigDecimal(100)).intValue();
    DatabaseManager.executeSQL("INSERT INTO player_data (uuid,coins) VALUES (?,?) ON DUPLICATE KEY UPDATE coins = ?", List.of(uuid, subCoins, subCoins));
  }

  public static void addCoins(String uuid, BigDecimal amount) {
    setCoins(uuid, getCoins(uuid).add(amount));
  }

  public static ItemStack getCoinItem(BigDecimal amount, int itemAmount) {
    ItemStack itemStack = new ItemStack(Material.SUNFLOWER, itemAmount);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.itemName(Component.text(amount + " Coins").color(NamedTextColor.GOLD));
    itemMeta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, "Coin");
    itemMeta.getPersistentDataContainer().set(Utils.key_coin_amount, PersistentDataType.DOUBLE, amount.doubleValue());
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
