package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface GuiButtonExecutor {
  void customAction(String buttonId, Gui gui, Player player, ClickType clickType);
  void switchPage(String buttonId, Gui gui, int page, Player player, ClickType clickType);
  void closeGui(String buttonId, Gui gui, Player player, ClickType clickType);
  void accept(String buttonId, Gui gui, Player player, ClickType clickType);
  void reject(String buttonId, Gui gui, Player player, ClickType clickType);
  void onSwitch(String buttonId, Gui gui, boolean state, Player player, ClickType clickType);
}
