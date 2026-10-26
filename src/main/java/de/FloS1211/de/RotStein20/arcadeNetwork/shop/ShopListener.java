package de.FloS1211.de.RotStein20.arcadeNetwork.shop;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.Utils;
import de.FloS1211.de.RotStein20.arcadeNetwork.coins.CoinsManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import io.papermc.paper.event.player.PlayerPurchaseEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ShopListener implements Listener {
  @EventHandler
  public void onShopShelfPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    EquipmentSlot slot = event.getHand();
    ItemStack placedItem = player.getInventory().getItem(slot);
    if (!placedItem.getType().name().endsWith("SHELF")) return;
    if (!Objects.equals(placedItem.getPersistentDataContainer().get(Utils.key_custom, PersistentDataType.STRING), "shop-shelf")) return;
    Block placedBlock = event.getBlockPlaced();
    Shelf shelf = (Shelf) placedBlock.getState();
    shelf.getPersistentDataContainer().set(Utils.key_shop_owner, PersistentDataType.STRING, player.getUniqueId().toString());
    shelf.getPersistentDataContainer().set(
        Utils.key_shop_trades,
        PersistentDataType.STRING,
        List.of(
            new ShopTrade(ShopManager.getShopConfigPlaceholderItem(player), 0, 1, 0),
            new ShopTrade(ShopManager.getShopConfigPlaceholderItem(player), 0, 1, 0),
            new ShopTrade(ShopManager.getShopConfigPlaceholderItem(player), 0, 1, 0)
        ).stream().map(ShopTrade::toString).reduce((a, b) -> a+";"+b).orElse("")
    );
    shelf.getPersistentDataContainer().set(Utils.key_shop_interaction, PersistentDataType.STRING, player.getUniqueId().toString());
    shelf.update();
    ShopManager.openShopConfig(player, shelf);
  }

  @EventHandler
  public void onShelfInteraction(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (event.getHand() != EquipmentSlot.HAND) return;
    if (event.getClickedBlock()==null || !(event.getClickedBlock().getState() instanceof Shelf shelf)) return;
    if (!shelf.getPersistentDataContainer().has(Utils.key_shop_owner)) return;
    Player player = event.getPlayer();
    String ownerUUID = shelf.getPersistentDataContainer().get(Utils.key_shop_owner, PersistentDataType.STRING);
    if (ownerUUID == null) return;
    event.setCancelled(true);

    String shelfInteraction = shelf.getPersistentDataContainer().get(Utils.key_shop_interaction, PersistentDataType.STRING);
    if (shelfInteraction != null
        && !shelfInteraction.isEmpty()) {
      LanguageManager.send(player, "shop.chat.interaction-failed-other_interaction");
      return;
    }
    shelf.getPersistentDataContainer().set(Utils.key_shop_interaction, PersistentDataType.STRING, player.getUniqueId().toString());
    shelf.update();

    if (player.getUniqueId().toString().equals(ownerUUID)) {
      ShopManager.openShopConfig(player, shelf);
    } else {
      ArcadeNetwork.getInstance().shopManager.openShopTrade(player, shelf, ownerUUID);
    }
  }

  @EventHandler
  public void onPurchase(PlayerPurchaseEvent event) {
    Merchant merchant = event.getMerchant();
    ShopSession session = ArcadeNetwork.getInstance().shopManager.activeShops.get(merchant);
    if (session == null) return;
    ItemStack coinItems = event.getTrade().getIngredients().getFirst();
    if (!coinItems.getPersistentDataContainer().has(Utils.key_coin_amount)) return;

    Shelf shelf = (Shelf) session.shelf().getBlock().getState();
    shelf = (Shelf) shelf.getBlock().getState();
    String tradesString = shelf.getPersistentDataContainer().get(Utils.key_shop_trades, PersistentDataType.STRING);
    if (tradesString == null || tradesString.isEmpty()) return;
    ShopTrade[] tradesArray = Arrays.stream(tradesString.split(";")).map(ShopTrade::fromString).toArray(ShopTrade[]::new);
    Inventory topInv = event.getPlayer().getOpenInventory().getTopInventory();
    if (!(topInv instanceof MerchantInventory merchantInventory)) return;
    int recipeIndex = session.indices().get(merchantInventory.getSelectedRecipeIndex());
    int boughtItems = event.getTrade().getResult().getAmount();
    ShopTrade trade = tradesArray[recipeIndex];
    trade.itemsInStock -= boughtItems;
    tradesArray[recipeIndex] = trade;
    tradesString = Arrays.stream(tradesArray).map(ShopTrade::toString).reduce((a, b) -> a+";"+b).orElse("");
    shelf.getPersistentDataContainer().set(Utils.key_shop_trades, PersistentDataType.STRING, tradesString);
    shelf.update();

    String ownerUUID = shelf.getPersistentDataContainer().get(Utils.key_shop_owner, PersistentDataType.STRING);
    double coinAmount = coinItems.getAmount() * coinItems.getPersistentDataContainer().get(Utils.key_coin_amount, PersistentDataType.DOUBLE);
    CoinsManager.addCoins(ownerUUID, BigDecimal.valueOf(coinAmount));
  }

  @EventHandler
  public void onTradeClose(InventoryCloseEvent event) {
    if (!(event.getView().getTopInventory() instanceof MerchantInventory inventory)) return;
    if (!PlainTextComponentSerializer.plainText().serialize(event.getView().title()).startsWith("AC-Shop: ")) return;
    Merchant merchant = inventory.getMerchant();
    ShopSession session = ArcadeNetwork.getInstance().shopManager.activeShops.get(merchant);
    if (session == null) return;
    ArcadeNetwork.getInstance().shopManager.activeShops.remove(merchant);
    Shelf shelf = (Shelf) session.shelf().getBlock().getState();
    shelf.getPersistentDataContainer().set(Utils.key_shop_interaction, PersistentDataType.STRING, "");
    shelf.update();
  }

  @EventHandler
  public void onDestroy(BlockBreakEvent event) {
    if (!(event.getBlock().getState() instanceof Shelf shelf)) return;
    if (!shelf.getPersistentDataContainer().has(Utils.key_shop_trades)) return;
    String tradesString = shelf.getPersistentDataContainer().get(Utils.key_shop_trades, PersistentDataType.STRING);
    ShopTrade[] trades = Arrays
        .stream(
            (tradesString == null || tradesString.isEmpty() ? new String[0] : tradesString.split(";"))
        ).map(ShopTrade::fromString)
        .toArray(ShopTrade[]::new);

    String interaction = shelf.getPersistentDataContainer().get(Utils.key_shop_interaction, PersistentDataType.STRING);
    if (interaction != null && !interaction.isEmpty()) {
      Player player = Bukkit.getPlayer(UUID.fromString(interaction));
      if (player != null) {
        Inventory openGui = player.getOpenInventory().getTopInventory();
        if (openGui.getHolder() instanceof ShopStorageGuiHolder holder) {
          trades[holder.tradeIndex].itemsInStock = Arrays.stream(openGui.getContents())
              .filter(item -> item != null && !trades[holder.tradeIndex].item.getType().isAir())
              .mapToInt(ItemStack::getAmount)
              .sum();
          player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
      }
    }

    for (ShopTrade trade : trades) {
      Utils.dropItems(shelf.getLocation(),trade.item, trade.itemsInStock);
    }
    shelf.getLocation().getWorld().dropItemNaturally(shelf.getLocation(), ShopManager.getShopShelf(shelf.getType()));
    event.setDropItems(false);
  }
}
