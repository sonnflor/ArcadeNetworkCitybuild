package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.shop;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ShopManager {
  public static void registerShelfRecipes() {
    ItemStack result = new ItemStack(Material.OAK_SHELF);
    ItemMeta meta = result.getItemMeta();
    meta.itemName(Component.text("Shop-Shelf"));
    meta.lore(List.of(Component.text("Platziren in einem Shop, zum Verkauf von Items").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
    meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, "shop-shelf");
    result.setItemMeta(meta);
    NamespacedKey key = new NamespacedKey(ArcadeNetworkCitybuild.getInstance(), "oak_shop_shelf");
    ShapedRecipe recipe = new ShapedRecipe(key, result);
    recipe.shape(
        "---",
        " # ",
        "---"
    );
    recipe.setIngredient('-', Material.STRIPPED_OAK_LOG);
    recipe.setIngredient('#', Material.CHEST);
    getServer().addRecipe(recipe);
  }
}
