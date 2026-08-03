package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButtonExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class NamecolorGuiButtonExecutor implements GuiButtonExecutor {
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
    String colName = gui.customData.get("couponName").substring(10);
    YamlConfiguration config = new YamlConfiguration();
    try {
      config.loadFromString(gui.customData.get("couponItem"));
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
    }
    ItemStack item = config.getItemStack("item");
    try {
      NamecolorManager.unlockNamecolor(player.getUniqueId().toString(), colName);
      player.sendMessage("§f[§aNamecolor§f]§7 Du hast die Namensfarbe " + colName.replace('_',' ') + " freigeschaltet. Aktiviere sie mit /namensfarben");
      if (!player.getGameMode().equals(org.bukkit.GameMode.CREATIVE)) item.setAmount(item.getAmount() - 1);
    } catch (IllegalArgumentException e) {
      player.sendMessage("§f[§aNamecolor§f]§7 Ein kritischer Fehler ist aufgetreten. Bitte informiere einen Supporter oder Admin");
    }
  }

  @Override
  public void reject(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void onSwitch(String buttonId, Gui gui, boolean state, Player player, ClickType clickType) {

  }
}
