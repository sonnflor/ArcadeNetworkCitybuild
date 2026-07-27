package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButtonExecutor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FriendsGuiButtonExecutor implements GuiButtonExecutor {
  @Override
  public void customAction(String buttonId, Gui gui, Player player) {
    switch (buttonId) {
      case "invite_button" -> {
        player.sendMessage("invite");
        player.closeInventory();
        new AnvilGUI.Builder()
            .plugin(ArcadeNetworkCitybuild.getInstance())
            .title("Freund hinzufügen")
            .text("Name")
            .onClick((slot, state) -> {
              if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
              String playerName = state.getText();
              Player target = Bukkit.getPlayerExact(playerName);
              if (target == null) {
                return List.of(AnvilGUI.ResponseAction.replaceInputText("Nicht gefunden"));
              }
              FriendsManager.inviteFriend(state.getPlayer(), target);
              return List.of(AnvilGUI.ResponseAction.close());
            })
            .open(player);
      }
      case "delete_button" -> {
        player.sendMessage("delete");
        player.closeInventory();
        new AnvilGUI.Builder()
            .plugin(ArcadeNetworkCitybuild.getInstance())
            .title("Freund entfreunden")
            .text("Name")
            .onClick((slot, state) -> {
              if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
              String playerName = state.getText();
              Player target = Bukkit.getPlayerExact(playerName);
              if (target == null) {
                return List.of(AnvilGUI.ResponseAction.replaceInputText("Nicht gefunden"));
              }
              FriendsManager.deleteFriend(state.getPlayer(), target);
              return List.of(AnvilGUI.ResponseAction.close());
            })
            .open(player);
      }
    }
  }

  @Override
  public void switchPage(String buttonId, Gui gui, int page, Player player) {

  }

  @Override
  public void closeGui(String buttonId, Gui gui, Player player) {

  }

  @Override
  public void accept(String buttonId, Gui gui, Player player) {

  }

  @Override
  public void reject(String buttonId, Gui gui, Player player) {

  }

  @Override
  public void onSwitch(String buttonId, Gui gui, boolean state, Player player) {

  }
}
