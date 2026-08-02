package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButtonExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class PerkGuiButtonExecutor implements GuiButtonExecutor {

  @Override
  public void customAction(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void switchPage(String buttonId, Gui gui, int page, Player player, ClickType clickType) {

  }

  @Override
  public void closeGui(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void accept(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void reject(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void onSwitch(String buttonId, Gui gui, boolean state, Player player, ClickType clickType) {
    PerkManager.switchPerk(player, PerkManager.perks.get(buttonId.replace("perk_state_","")), state);
  }
}
