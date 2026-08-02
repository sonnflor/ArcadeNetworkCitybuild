package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.Namecolor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class Gui {
  private final Map<Integer, Map<Integer, GuiElement<?>>> pages = new HashMap<>();
  private final Component title;
  private final int size;
  private int aktPage = 0;
  private boolean isTopBarActive = false;
  private boolean isBottomBarActive = false;
  private final Map<Integer,GuiElement<?>> topBar = new HashMap<>();
  private final Map<Integer,GuiElement<?>> bottomBar = new HashMap<>();
  public Map<String, String> customData = new HashMap<>();

  public Gui(int size, Component title) {
    this.size = size;
    this.title = title;
  }

  public void setElement(GuiElement<?> element, int pageIndex, int slot) {
    if (slot >= size || slot < 0) throw new IllegalArgumentException("slot is out of the bounds of the gui");
    pages.computeIfAbsent(pageIndex, k -> new HashMap<>())
        .put(slot, element);
  }
  public void addElement(GuiElement<?> element) {
    int page = pages.isEmpty() ? 0 : pages.keySet().stream().max(Integer::compareTo).get();
    int slot = pages.getOrDefault(page, Map.of()).size();

    if (slot >= size-(isBottomBarActive?9:0)) {
      page++;
      slot = 0;
    }

    setElement(element,page,slot);
  }
  public void addElements(List<GuiElement<?>> elements) {
    for (GuiElement<?> element : elements) {
      addElement(element);
    }
  }
  public void setElementInTopBar(GuiElement<?> element, int slot){
    if (slot < 0 || slot > 8) throw new IllegalArgumentException("invalid slot");
    isTopBarActive = true;
    topBar.put(slot,element);
  }
  public void setElementInBottomBar(GuiElement<?> element, int slot){
    if (slot < 0 || slot > 8) throw new IllegalArgumentException("invalid slot");
    isBottomBarActive = true;
    bottomBar.put(slot,element);
  }

  public GuiElement<?> getElement(int pageIndex, int slot) {
    return pages.getOrDefault(pageIndex, Collections.emptyMap()).get(slot);
  }

  public GuiElement<?> getElement(String elId) {
    // Top-Bar
    for (GuiElement<?> element : topBar.values()) {
      if (element != null && Objects.equals(element.id, elId)) {
        return element;
      }
    }
    // Seiten
    for (Map<Integer, GuiElement<?>> page : pages.values()) {
      for (GuiElement<?> element : page.values()) {
        if (element != null && Objects.equals(element.id, elId)) {
          return element;
        }
      }
    }
    // Bottom-Bar
    for (GuiElement<?> element : bottomBar.values()) {
      if (element != null && Objects.equals(element.id, elId)) {
        return element;
      }
    }
    return null;
  }

  public GuiElement<?> getElementByInvSlot(int slot) {
    if (isTopBarActive && slot < 9) { //top
      return topBar.get(slot);
    } else if (isBottomBarActive && slot >= size - 9) { //bottom
      GuiElement<?> el = bottomBar.get(slot-size+9);
      if (el instanceof GuiButton button && button.type == GuiButtonType.PREV_PAGE && aktPage == 0) {
        return GuiDisplay.getPlaceholder();
      }
      if (el instanceof GuiButton button && button.type == GuiButtonType.NEXT_PAGE && aktPage == Math.max(getPageAmount(),1)-1) {
        return GuiDisplay.getPlaceholder();
      }
      return el;
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
    if (getPageAmount() > 1) guiTitle = title.append(Component.text(" Seite " + (aktPage + 1) + "/" + getPageAmount()));
    Inventory inv = Bukkit.createInventory(holder,size,guiTitle);
    Map<Integer,GuiElement<?>> page = pages.getOrDefault(aktPage, new HashMap<>());
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
        inv.setItem(size-9+i, barEl.buildItem());
        if (aktPage == 0 && barEl instanceof GuiButton && ((GuiButton) barEl).getType() == GuiButtonType.PREV_PAGE) {
          inv.setItem(size-9+i, placeholder.buildItem());
        } else if (aktPage == Math.max(getPageAmount(),1)-1 && barEl instanceof GuiButton && ((GuiButton) barEl).getType() == GuiButtonType.NEXT_PAGE) {
          inv.setItem(size-9+i, placeholder.buildItem());
        }
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

  public void setCustomData(Map<String, String> customData) {
    this.customData = customData;
  }

  public static Gui getConfirmationGui(Map<String, String> customData, GuiButtonExecutor executor, String acceptDescription) {
    GuiButton acceptButton = new GuiButton("accept", Material.LIME_DYE,executor, GuiButtonType.ACCEPT)
        .title(Component.text("Akzeptieren").color(NamedTextColor.GREEN))
        .lore(List.of(Component.text(acceptDescription)))
        .sound(GuiSound.SUCCESS);
    GuiButton rejectButton = new GuiButton("reject", Material.RED_DYE, executor, GuiButtonType.REJECT)
        .title(Component.text("Abbrechen").color(NamedTextColor.RED))
        .sound(GuiSound.FAILURE);
    Gui gui = new Gui(27, Component.text("Bestätigung"));
    gui.setElement(acceptButton,0,12);
    gui.setElement(rejectButton,0,14);
    gui.setCustomData(customData);
    return gui;
  }
}
