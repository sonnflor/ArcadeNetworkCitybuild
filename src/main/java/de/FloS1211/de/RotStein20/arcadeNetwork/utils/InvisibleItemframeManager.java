package de.FloS1211.de.RotStein20.arcadeNetwork.utils;

import de.FloS1211.de.RotStein20.arcadeNetwork.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InvisibleItemframeManager implements Listener {
  @EventHandler
  public void onHangingPlace(HangingPlaceEvent event) {
    Entity ent = event.getEntity();
    if (!(ent instanceof ItemFrame frame)) return;
    NamespacedKey invisibleKey = Utils.key_invisible_itemframe;
    ItemStack origin = event.getItemStack();
    if (origin == null) return;
    ItemMeta meta = origin.getItemMeta();
    if (meta == null) return;
    if (meta.getPersistentDataContainer().has(invisibleKey, PersistentDataType.BYTE)) {
      frame.setVisible(false);
    }
  }
  private static ItemStack itemFrame;
  public static ItemStack getInvisibleItemFrame() {
    if (itemFrame == null) {
      ItemStack item = new ItemStack(Material.ITEM_FRAME);
      ItemMeta meta = item.getItemMeta();
      if (meta == null) return item;
      meta.itemName(Component.text("Invisible Itemframe"));
      meta.setEnchantmentGlintOverride(true);
      meta.setRarity(ItemRarity.EPIC);
      meta.getPersistentDataContainer().set(Utils.key_invisible_itemframe, PersistentDataType.BYTE, (byte) 1);
      item.setItemMeta(meta);
      itemFrame = item;
    }
    return itemFrame.clone();
  }
  public static void registerRecipe() {
    NamespacedKey recipeKey = Utils.key_invisible_itemframe;
    ItemStack item = getInvisibleItemFrame();
    ShapedRecipe recipe = new ShapedRecipe(recipeKey, item);
    recipe.shape(
        "...",
        ".#.",
        "...");
    recipe.setIngredient('.', Material.GLASS);
    recipe.setIngredient('#', Material.ITEM_FRAME);
    Bukkit.addRecipe(recipe);
  }
}
