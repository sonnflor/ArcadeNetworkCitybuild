package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GuiButton extends GuiElement<GuiButton> {
  protected GuiButtonExecutor executor;
  protected GuiSound sound = GuiSound.CLICK;
  protected GuiButtonType type;

  public GuiButton(String id, Material icon, GuiButtonExecutor executor, GuiButtonType type) {
    super(id, icon);
    this.executor = executor;
    this.type = type;
  }

  public GuiButton sound(GuiSound sound) {
    this.sound = sound;
    return this;
  }

  public void onClick(Player player, Gui gui) {
    sound.play(player);
    switch (type) {
      case CUSTOM -> executor.customAction(id, gui, player);
      case NEXT_PAGE -> {if (gui.getPage() + 1 <= gui.getPageAmount()) executor.switchPage(id, gui, gui.getPage() + 1, player);}
      case PREV_PAGE -> {if (gui.getPage() - 1 >= 0) executor.switchPage(id, gui, gui.getPage() - 1, player);}
      case CLOSE -> executor.closeGui(id, gui, player);
      case ACCEPT -> executor.accept(id, gui, player);
      case REJECT -> executor.reject(id, gui, player);
    }
  }

  public GuiButtonExecutor getExecutor() {
    return executor;
  }

  public GuiButtonType getType() {
    return type;
  }
}
