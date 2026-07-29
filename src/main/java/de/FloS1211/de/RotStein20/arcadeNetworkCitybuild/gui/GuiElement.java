package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class GuiElement<T extends GuiElement<T>> {
  protected Material icon;
  protected boolean hasEnchantmentGlintOverride = false;
  protected Component title;
  protected List<Component> lore = new ArrayList<>();
  protected int amount = 1;
  protected String id;
  protected String headUuid;

  protected GuiElement(String id, Material icon) {
    this.id = id;
    this.icon = icon;
  }

  public T icon(Material icon) {
    this.icon = icon;
    return self();
  }

  public T title(Component title) {
    this.title = Component.text("").decoration(TextDecoration.ITALIC, false).append(title);
    return self();
  }

  public T lore(List<Component> lore) {
    List<Component> grayLore = new ArrayList<>();
    for (Component line : lore) {
      grayLore.add(Component.text("").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(line));
    }
    this.lore = grayLore;
    return self();
  }

  public T amount(int amount) {
    this.amount = amount;
    return self();
  }

  public T enchantment(boolean hasEnchantmentGlintOverride) {
    this.hasEnchantmentGlintOverride = hasEnchantmentGlintOverride;
    return self();
  }

  public T playerHead(String uuid) {
    this.headUuid = uuid;
    return self();
  }

  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }

  public ItemStack buildItem() {
    ItemStack item;
    if (headUuid != null && icon == Material.PLAYER_HEAD) {
      item = Utils.getHeadFromOfflinePlayer(Bukkit.getOfflinePlayer(UUID.fromString(headUuid)));
    } else {
      item = ItemStack.of(icon,amount);
    }
    ItemMeta meta = item.getItemMeta();
    if (title != null) meta.displayName(title);
    meta.lore(lore);
    if (hasEnchantmentGlintOverride) meta.setEnchantmentGlintOverride(true);
    item.setItemMeta(meta);

    return item;
  }
}
