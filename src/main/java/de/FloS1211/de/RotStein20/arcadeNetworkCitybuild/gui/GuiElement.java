package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiElement {
  protected Material icon = Material.DIRT;
  protected boolean hasEnchantmentGlintOverride = false;
  protected Component title;
  protected List<Component> lore = new ArrayList<>();
  protected int amount = 1;
  protected String id;

  protected GuiElement(String id, Material icon) {
    this.id = id;
    this.icon = icon;
  }

  public void setIcon(Material icon) {
    this.icon = icon;
  }

  public void setTitle(Component title) {
    this.title = title;
  }

  public void setLore(List<Component> lore) {
    this.lore = lore;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public void setEnchantment(boolean hasEnchantmentGlintOverride) {
    this.hasEnchantmentGlintOverride = hasEnchantmentGlintOverride;
  }

  public ItemStack buildItem() {
    ItemStack item = ItemStack.of(icon, amount);

    ItemMeta meta = item.getItemMeta();
    if (title != null) meta.displayName(title);
    meta.lore(lore);
    if (hasEnchantmentGlintOverride) meta.setEnchantmentGlintOverride(true);
    item.setItemMeta(meta);

    return item;
  }
}
