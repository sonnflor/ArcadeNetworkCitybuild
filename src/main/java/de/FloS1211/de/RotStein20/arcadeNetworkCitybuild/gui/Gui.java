package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class Gui {
  private final Map<Integer, Map<Integer, GuiElement<?>>> pages;
  private final Component title;
  private final int size;
  private int aktPage = 0;
  private boolean isTopBarActive = false;
  private boolean isBottomBarActive = false;
  private final Map<Integer,GuiElement<?>> topBar = new HashMap<>();
  private final Map<Integer,GuiElement<?>> bottomBar = new HashMap<>();;

  public Gui(Map<Integer, Map<Integer, GuiElement<?>>> pages, int size, Component title) {
    this.size = size;
    this.title = title;
    this.pages = pages;
  }

  public void addElement(GuiElement<?> element, int pageIndex, int slot) {
    if (slot >= size || slot < -1) throw new IllegalArgumentException("slot is out of the bounds of the gui");
    pages.computeIfAbsent(pageIndex, k -> new HashMap<>())
        .put(slot, element);
  }
  public void addElementToTopBar(GuiElement<?> element, int slot){
    if (slot < 0 || slot > 8) throw new IllegalArgumentException("invalid slot");
    isTopBarActive = true;
    topBar.put(slot,element);
  }
  public void addElementToBottomBar(GuiElement<?> element, int slot){
    if (slot < 0 || slot > 8) throw new IllegalArgumentException("invalid slot");
    isBottomBarActive = true;
    bottomBar.put(slot,element);
  }

  public GuiElement<?> getElement(int pageIndex, int slot) {
    return pages.get(pageIndex).get(slot);
  }

  public GuiElement<?> getElementByInvSlot(int slot) {
    if (isTopBarActive && slot < 9) { //top
      return topBar.get(slot);
    } else if (isBottomBarActive && slot >= size - 9) { //bottom
      return bottomBar.get(slot-size+9);
    } else { //main
      Map<Integer, GuiElement<?>> page = pages.get(aktPage);
      if (isTopBarActive) {
        return page.get(slot-9);
      } else {
        return page.get(slot);
      }
    }
  }

  public void setPage(Map<Integer, GuiElement<?>> page, int pageIndex) {
    pages.put(pageIndex,page);
  }

  public Map<Integer, GuiElement<?>> getPage(int pageIndex) {
    return pages.get(pageIndex);
  }

  public void setPage(int pageIndex) {
    this.aktPage = pageIndex;
  }

  public Inventory buildInventory() {
    GuiHolder holder = new GuiHolder(this);
    Component guiTitle = title;
    if (getPageAmount() > 1) guiTitle = title.append(Component.text(" Seite " + aktPage + "/" + getPageAmount()));
    Inventory inv = Bukkit.createInventory(holder,size,guiTitle);
    Map<Integer,GuiElement<?>> page = pages.get(aktPage);
    for (int i : page.keySet()) {
      int slot = i;
      if (isTopBarActive) slot += 9;
      if (isBottomBarActive && slot > size-9) throw new IllegalArgumentException("item overlaps with bottom bar");
      inv.setItem(slot,page.get(i).buildItem());
    }
    if (isTopBarActive) {
      GuiDisplay placeholder = GuiDisplay.getPlaceholder();
      for (int i = 0; i < 9; i++) {
          inv.setItem(i, topBar.getOrDefault(i, placeholder).buildItem());
      }
    }
    if (isBottomBarActive) {
      GuiDisplay placeholder = GuiDisplay.getPlaceholder();
      for (int i = 0; i < 9; i++) {
        GuiElement<?> barEl =bottomBar.getOrDefault(i, placeholder);
        if (aktPage == 0 && barEl instanceof GuiButton && ((GuiButton) barEl).getType() == GuiButtonType.PREV_PAGE) {
          barEl = placeholder;
        } else if (aktPage == getPageAmount()-1 && barEl instanceof GuiButton && ((GuiButton) barEl).getType() == GuiButtonType.NEXT_PAGE) {
          barEl = placeholder;
        }
        inv.setItem(size-9+i, barEl.buildItem());
      }
    }
    return inv;
  }

  public int getSize() {
    return size;
  }

  public int getPage() {
    return aktPage;
  }

  public int getPageAmount() {
    return pages.size();
  }
}
