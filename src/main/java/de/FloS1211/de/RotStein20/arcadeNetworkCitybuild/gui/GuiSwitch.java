package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiSwitch extends GuiElement<GuiSwitch>{
  private final GuiDisplay enabledDisplay;
  private final GuiDisplay disabledDisplay;
  private final GuiButtonExecutor executor;
  private boolean state = true;
  private final GuiSound enabledSound = GuiSound.TOGGLE_ON;
  private final GuiSound disabledSound = GuiSound.TOGGLE_OFF;

  public GuiSwitch(
      String id,
      GuiDisplay enabledDisplay,
      GuiDisplay disabledDisplay,
      GuiButtonExecutor executor
  ) {
    super(id, enabledDisplay.icon);
    this.enabledDisplay = enabledDisplay;
    this.disabledDisplay = disabledDisplay;
    this.executor = executor;
  }

  @Override
  public ItemStack buildItem() {
    return state
        ? enabledDisplay.buildItem()
        : disabledDisplay.buildItem();
  }

  public void onClick(Player player, Gui gui, ClickType clickType) {
    state = !state;
    if (state) enabledSound.play(player);
    else disabledSound.play(player);
    player.openInventory(gui.buildInventory());
    executor.onSwitch(id,gui,state, player, clickType);
  }
}

