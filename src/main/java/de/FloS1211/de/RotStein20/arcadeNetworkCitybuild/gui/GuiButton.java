package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GuiButton extends GuiElement {
  protected GuiButtonExecutor executor;
  protected GuiSound sound = GuiSound.CLICK;
  protected GuiButtonType type = GuiButtonType.CUSTOM;

  public GuiButton(String id, Material icon, GuiButtonExecutor executor
  ) {
    super(id, icon);
    this.executor = executor;
  }

  public void setSound(GuiSound sound) {
    this.sound = sound;
  }

  public void setType(GuiButtonType type) {
    this.type = type;
  }

  public void onClick(Player player, Gui gui, Boolean state) {
    sound.play(player);
    if (this instanceof GuiSwitch) {
      executor.onSwitch(id,gui,state);
      return;
    }
    switch (type) {
      case CUSTOM -> executor.customAction(id, gui);
      case NEXT_PAGE -> {if (gui.getPage() + 1 <= gui.getMaxPage()) executor.switchPage(id, gui, gui.getPage() + 1);}
      case PREV_PAGE -> {if (gui.getPage() - 1 >= 0) executor.switchPage(id, gui, gui.getPage() - 1);}
      case CLOSE -> executor.closeGui(id, gui);
      case ACCEPT -> executor.accept(id, gui);
      case REJECT -> executor.reject(id, gui);
    }
  }
}
