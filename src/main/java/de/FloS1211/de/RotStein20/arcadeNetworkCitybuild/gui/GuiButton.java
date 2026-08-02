package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

public class GuiButton extends GuiElement<GuiButton> {
  protected GuiButtonExecutor executor;
  protected GuiSound sound = GuiSound.CLICK;
  protected GuiButtonType type;
  public Map<String, String> customData = new HashMap<>();
  public Map<String, String> controls;

  public GuiButton(String id, Material icon, GuiButtonExecutor executor, GuiButtonType type) {
    super(id, icon);
    this.executor = executor;
    this.type = type;
  }

  public GuiButton sound(GuiSound sound) {
    this.sound = sound;
    return this;
  }

  public GuiButton customData(Map<String, String> customData) {
    this.customData = customData;
    return this;
  }

  public GuiButton controls(Map<String, String> controls) {
    this.controls = controls;
    return this;
  }

  public void onClick(Player player, Gui gui, ClickType clickType) {
    sound.play(player);
    switch (type) {
      case CUSTOM -> executor.customAction(id, gui, player, clickType);
      case NEXT_PAGE -> {
        gui.setPage(gui.getPage()+1);
        executor.switchPage(id, gui, gui.getPage(), player, clickType);
        player.openInventory(gui.buildInventory());
      }
      case PREV_PAGE -> {
        gui.setPage(gui.getPage()-1);
        executor.switchPage(id, gui, gui.getPage(), player, clickType);
        player.openInventory(gui.buildInventory());
      }
      case CLOSE -> executor.closeGui(id, gui, player, clickType);
      case ACCEPT -> executor.accept(id, gui, player, clickType);
      case REJECT -> executor.reject(id, gui, player, clickType);
    }
  }

  public GuiButtonExecutor getExecutor() {
    return executor;
  }

  public GuiButtonType getType() {
    return type;
  }

  public static GuiButton getNextPageButton(GuiButtonExecutor executor) {
    return new GuiButton("next_page_button", Material.PAPER, executor, GuiButtonType.NEXT_PAGE).sound(GuiSound.PAGE_FLIP).title(Component.text("Nächste Seite").color(NamedTextColor.GRAY));
  }

  public static GuiButton getPrevPageButton(GuiButtonExecutor executor) {
    return new GuiButton("prev_page_button", Material.PAPER, executor, GuiButtonType.PREV_PAGE).sound(GuiSound.PAGE_FLIP).title(Component.text("Vorherige Seite").color(NamedTextColor.GRAY));
  }
}
