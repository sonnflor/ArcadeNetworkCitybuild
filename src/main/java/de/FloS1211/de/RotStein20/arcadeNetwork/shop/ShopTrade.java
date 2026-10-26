package de.FloS1211.de.RotStein20.arcadeNetwork.shop;

import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ShopTrade {
  public ItemStack item;
  public int price;
  public int amount;
  public int itemsInStock;

  public ShopTrade(ItemStack item, int price, int amount, int itemsInStock) {
    this.itemsInStock = itemsInStock;
    this.item = item;
    this.price = price;
    this.amount = amount;
  }

  public String toString() {
    String encodedItem = Base64.getEncoder().encodeToString(
        item.serializeAsBytes()
    );
    return encodedItem + ":" + price + ":" + amount + ":" + itemsInStock;
  }

  public static ShopTrade fromString(String str) {
    String[] parts = str.split(":");
    if (parts.length != 4) new IllegalArgumentException().printStackTrace();
    ItemStack item = ItemStack.deserializeBytes(Base64.getDecoder().decode(parts[0]));
    int price = Integer.parseInt(parts[1]);
    int amount = Integer.parseInt(parts[2]);
    int itemsInStock = Integer.parseInt(parts[3]);
    return new ShopTrade(item, price, amount, itemsInStock);
  }
}
