package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends.FriendsGuiButtonExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
      case NEXT_PAGE -> {
        gui.setPage(gui.getPage()+1);
        executor.switchPage(id, gui, gui.getPage(), player);
        player.openInventory(gui.buildInventory());
      }
      case PREV_PAGE -> {
        gui.setPage(gui.getPage()-1);
        executor.switchPage(id, gui, gui.getPage(), player);
        player.openInventory(gui.buildInventory());
      }
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

  public static GuiButton getNextPageButton(GuiButtonExecutor executor) {
    return new GuiButton("next_page_button", Material.PAPER, new FriendsGuiButtonExecutor(), GuiButtonType.NEXT_PAGE).sound(GuiSound.PAGE_FLIP).title(Component.text("Nächste Seite").color(NamedTextColor.GRAY));
  }

  public static GuiButton getPrevPageButton(GuiButtonExecutor executor) {
    return new GuiButton("prev_page_button", Material.PAPER, new FriendsGuiButtonExecutor(), GuiButtonType.PREV_PAGE).sound(GuiSound.PAGE_FLIP).title(Component.text("Vorherige Seite").color(NamedTextColor.GRAY));
  }
}
