package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

public interface GuiButtonExecutor {
  void customAction(String buttonId, Gui gui);
  void switchPage(String buttonId, Gui gui, int page);
  void closeGui(String buttonId, Gui gui);
  void accept(String buttonId, Gui gui);
  void reject(String buttonId, Gui gui);
  void onSwitch(String buttonId, Gui gui, boolean state);
}
