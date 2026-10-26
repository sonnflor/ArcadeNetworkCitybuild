package de.FloS1211.de.RotStein20.arcadeNetwork.shop;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.Utils;
import de.FloS1211.de.RotStein20.arcadeNetwork.coins.CoinsManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.*;

public class ShopManager {
  public Map<Merchant, ShopSession> activeShops = new HashMap<>();
  public static ItemStack getShopShelf(Material shelfMaterial) {
    ItemStack result = new ItemStack(shelfMaterial);
    ItemMeta meta = result.getItemMeta();
    meta.itemName(Component.text("Shop-Shelf"));
    meta.lore(List.of(Component.text("Platzieren in einem Shop, zum Verkauf von Items").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
    meta.getPersistentDataContainer().set(Utils.key_custom, PersistentDataType.STRING, "shop-shelf");
    result.setItemMeta(meta);
    return result;
  }

  public static void registerShelfRecipes() {
    List<Material> shelfMaterials = Arrays.stream(Material.values()).filter(material -> material.name().endsWith("_SHELF")).toList();
    for (Material shelfMaterial : shelfMaterials) {
      String shelfName = shelfMaterial.name();
      String woodName = shelfName.substring(
          0,
          shelfName.length() - "_SHELF".length()
      );
      String ingredientName = switch (woodName) {
        case "BAMBOO" ->
          "STRIPPED_BAMBOO_BLOCK";
        case "CRIMSON", "WARPED" ->
            "STRIPPED_" + woodName + "_STEM";

        default ->
            "STRIPPED_" + woodName + "_LOG";

      };
      Material strippedLog = Material.matchMaterial(ingredientName);
      if (strippedLog == null) continue;
      ItemStack result = getShopShelf(shelfMaterial);
      result.setAmount(6);
      NamespacedKey key = new NamespacedKey(ArcadeNetwork.getInstance(), woodName.toLowerCase() + "_shop_shelf");
      ShapedRecipe recipe = new ShapedRecipe(key, result);
      recipe.shape(
          "---",
          "+#+",
          "---"
      );
      recipe.setIngredient('-', strippedLog);
      recipe.setIngredient('#', Material.CHEST);
      recipe.setIngredient('+', Material.EMERALD);
      Bukkit.addRecipe(recipe);
    }
  }

  public void openShopTrade(Player player, Shelf shelf, String ownerUUID) {
    Merchant merchant = Bukkit.getServer().createMerchant();
    String tradesString = shelf.getPersistentDataContainer().get(Utils.key_shop_trades, PersistentDataType.STRING);
    if (tradesString == null || tradesString.isEmpty()) {
      LanguageManager.send(player, "shop.chat.interaction-failed-no_trades");
      return;
    }
    String[] tradesArray = tradesString.split(";");
    List<MerchantRecipe> recipes = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < tradesArray.length; i++) {
      String tradeString = tradesArray[i];
      ShopTrade trade = ShopTrade.fromString(tradeString);
      if (trade.item.getType() != Material.STRUCTURE_VOID) {
        MerchantRecipe recipe = ArcadeNetwork.getInstance().shopManager.createMerchantRecipe(trade);
        recipes.add(recipe);
        indices.add(i);
      }
    }
    if (recipes.isEmpty()) {
      LanguageManager.send(player, "shop.chat.interaction-failed-no_trades");
      return;
    }
    merchant.setRecipes(recipes);

    activeShops.put(merchant, new ShopSession(shelf, indices));

    player.openInventory(
        MenuType.MERCHANT.builder()
            .merchant(merchant)
            .title(Component.text("AC-Shop: ").append(PlayerNameManager.getPlayerName(ownerUUID)))
            .build(player)
    );
  }

  private static MerchantRecipe createMerchantRecipe(ShopTrade trade) {
    ItemStack tradeItem = trade.item.clone();
    tradeItem.setAmount(trade.amount);

    ItemStack coinItem = CoinsManager.getCoinItem(BigDecimal.valueOf(trade.price), 1);

    MerchantRecipe recipe = new MerchantRecipe(
        tradeItem,
        trade.itemsInStock / trade.amount
    );
    recipe.addIngredient(coinItem);
    return recipe;
  }

  public static void openShopConfig(Player player, Shelf shelf) {
    String tradesString = shelf.getPersistentDataContainer().get(Utils.key_shop_trades, PersistentDataType.STRING);
    ShopTrade[] tradesArray = Arrays.stream((tradesString == null || tradesString.isEmpty() ? new String[0] : tradesString.split(";"))).map(ShopTrade::fromString).filter(trade -> trade != null).toArray(ShopTrade[]::new);
    ShopConfigGuiHolder holder = new ShopConfigGuiHolder(Arrays.stream(tradesArray).toList(), shelf);
    Inventory gui = Bukkit.createInventory(holder, 54, LanguageManager.get(player, "shop.gui.shop-config.title"));
    holder.setInventory(gui);
    for (int i = 0; i < 54; i++) {
      gui.setItem(i, Utils.emptyGlassPane.clone());
    }

    for (int i = 0; i < tradesArray.length; i++) {
      //item display
      ShopTrade trade = tradesArray[i];
      if (trade == null) continue;
      ItemStack tradeItem = trade.item.clone();
      tradeItem.setAmount(trade.amount);
      gui.setItem(9+(i*3+1), tradeItem);
      //price display
      ItemStack priceItem = CoinsManager.getCoinItem(BigDecimal.valueOf(trade.price), 1);
      ItemMeta priceMeta = priceItem.getItemMeta();
      if (priceMeta != null) {
        priceMeta.itemName(LanguageManager.get(player, "shop.gui.shop-config.price", "price", trade.price+""));
        priceMeta.lore(LanguageManager.getList(player, "shop.gui.shop-config.price-lore"));
        priceItem.setItemMeta(priceMeta);
      }
      gui.setItem(18+(i*3+1), priceItem);
      //storage display
      ItemStack storageItem = new ItemStack(Material.CHEST);
      ItemMeta storageMeta = storageItem.getItemMeta();
      if (storageMeta != null) {
        int max = trade.item.getMaxStackSize() * 54;
        storageMeta.displayName(LanguageManager.get(player, "shop.gui.shop-config.storage", "amount", trade.itemsInStock+"", "max", max+""));
        storageItem.setItemMeta(storageMeta);
      }
      gui.setItem(36+(i*3+1), storageItem);
      //help
      ItemStack helpItem = new ItemStack(Material.LIGHT);
      ItemMeta helpMeta = helpItem.getItemMeta();
      if (helpMeta != null) {
        helpMeta.itemName(LanguageManager.get(player, "shop.gui.shop-config.help"));
        helpItem.setItemMeta(helpMeta);
      }
      gui.setItem(45, helpItem);
    }
    player.openInventory(gui);
  }

  public static void openStorageConfig(Player player, Shelf shelf, int tradeIndex) {
    String tradesString = shelf.getPersistentDataContainer().get(Utils.key_shop_trades, PersistentDataType.STRING);
    if (tradesString == null || tradesString.isEmpty()) return;
    String[] tradesArray = tradesString.split(";");
    if (tradeIndex < 0 || tradeIndex >= tradesArray.length) return;
    ShopTrade trade = ShopTrade.fromString(tradesArray[tradeIndex]);
    ShopStorageGuiHolder holder = new ShopStorageGuiHolder(player, shelf, tradesArray, tradeIndex);
    Inventory gui = Bukkit.createInventory(holder, 54, LanguageManager.get(player, "shop.gui.storage.title"));
    holder.setInventory(gui);
    ItemStack tradeItem = trade.item.clone();
    tradeItem.setAmount(trade.itemsInStock);
    gui.addItem(tradeItem);
    player.openInventory(gui);
  }

  public static ItemStack getShopConfigPlaceholderItem(Player player) {
    ItemStack result = new ItemStack(Material.STRUCTURE_VOID);
    ItemMeta meta = result.getItemMeta();
    meta.itemName(LanguageManager.get(player, "shop.gui.shop-config.item-placeholder-name"));
    meta.lore(LanguageManager.getList(player, "shop.gui.shop-config.item-placeholder-lore"));
    result.setItemMeta(meta);
    return result;
  }
}
