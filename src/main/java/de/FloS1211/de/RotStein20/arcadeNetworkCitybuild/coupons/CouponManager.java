package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coupons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.function.Consumer;

public class CouponManager {
  public ItemStack createCoupon(Consumer<String> acceptFunktion, Component title, Component... lore) {
    ItemStack coupon = new ItemStack(Material.BOOK);
    return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
  }
}
