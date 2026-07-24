package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class GuiDisplay extends GuiElement {
  public GuiDisplay(String id, Material icon) {
    super(id,icon);
  }

  public static GuiDisplay getPlaceholder(int slot) {
    GuiDisplay guiDisplay = new GuiDisplay("placeholder"+slot, Material.GRAY_STAINED_GLASS_PANE);
    guiDisplay.setTitle(Component.text(""));
    return guiDisplay;
  }
}
