package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.entity.Player;

public interface GuiButtonExecutor {
  void customAction(String buttonId, Gui gui, Player player);
  void switchPage(String buttonId, Gui gui, int page, Player player);
  void closeGui(String buttonId, Gui gui, Player player);
  void accept(String buttonId, Gui gui, Player player);
  void reject(String buttonId, Gui gui, Player player);
  void onSwitch(String buttonId, Gui gui, boolean state, Player player);
}
