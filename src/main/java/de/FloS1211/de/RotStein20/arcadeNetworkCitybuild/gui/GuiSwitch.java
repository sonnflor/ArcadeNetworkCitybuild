package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuiSwitch extends GuiButton{
  private final GuiDisplay enabledDisplay;
  private final GuiDisplay disabledDisplay;
  private boolean state = true;

  public GuiSwitch(
      String id,
      GuiDisplay enabledDisplay,
      GuiDisplay disabledDisplay,
      GuiButtonExecutor executor
  ) {
    super(id, enabledDisplay.icon, executor);

    this.enabledDisplay = enabledDisplay;
    this.disabledDisplay = disabledDisplay;
  }

  @Override
  public ItemStack buildItem() {
    return state
        ? enabledDisplay.buildItem()
        : disabledDisplay.buildItem();
  }

  public void onClick(Player player, Gui gui) {
    state = !state;
    player.openInventory(gui.buildInventory());
    super.onClick(player, gui, state);
  }
}

