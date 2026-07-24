package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class Gui {
  private final Map<Integer, Map<Integer, GuiElement>> pages;
  private final Component title;
  private final int size;
  private int aktPage = 0;
  private final int maxPage;

  public Gui(Map<Integer, Map<Integer, GuiElement>> pages, int size, Component title) {
    this.size = size;
    this.maxPage = pages.size()-1;
    this.title = title;
    this.pages = pages;
  }

  public void setElement(GuiElement element, int pageIndex, int slot) {
    if (slot >= size) throw new IllegalArgumentException("Slot is out of the bounds of the gui");
    pages.computeIfAbsent(pageIndex, k -> new HashMap<>())
        .put(slot, element);
  }

  public GuiElement getElement(int pageIndex, int slot) {
    return pages.get(pageIndex).get(slot);
  }

  public void setPage(Map<Integer, GuiElement> page, int pageIndex) {
    pages.put(pageIndex,page);
  }

  public Map<Integer, GuiElement> getPage(int pageIndex) {
    return pages.get(pageIndex);
  }

  public void setPage(int pageIndex) {
    this.aktPage = pageIndex;
  }

  public Inventory buildInventory() {
    GuiHolder holder = new GuiHolder(this);
    Inventory inv = Bukkit.createInventory(holder,size,title);
    Map<Integer,GuiElement> page = pages.get(aktPage);
    for (int i : page.keySet()) {
      inv.setItem(i,page.get(i).buildItem());
    }
    return inv;
  }

  public int getSize() {
    return size;
  }

  public int getPage() {
    return aktPage;
  }

  public int getMaxPage() {
    return maxPage;
  }
}
