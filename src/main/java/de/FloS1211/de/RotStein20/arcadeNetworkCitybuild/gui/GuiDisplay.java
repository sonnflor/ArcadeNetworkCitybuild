package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class GuiDisplay extends GuiElement<GuiDisplay> {
  public GuiDisplay(String id, Material icon) {
    super(id,icon);
  }

  public static GuiDisplay getPlaceholder() {
    return new GuiDisplay("placeholder", Material.GRAY_STAINED_GLASS_PANE).title(Component.text(""));
  }
}
