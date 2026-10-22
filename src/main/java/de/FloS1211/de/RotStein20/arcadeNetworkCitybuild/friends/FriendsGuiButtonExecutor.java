package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButton;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButtonExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.LanguageManager;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FriendsGuiButtonExecutor implements GuiButtonExecutor {
  @Override
  public void customAction(String buttonId, Gui gui, Player player, ClickType clickType) {
    switch (buttonId) {
      case "invite_button" -> {
        player.closeInventory();
        new AnvilGUI.Builder()
            .plugin(ArcadeNetworkCitybuild.getInstance())
            .title(LanguageManager.getString(player, "friends.gui.invite.title"))
            .text(LanguageManager.getString(player, "friends.gui.invite.text"))
            .onClick((slot, state) -> {
              if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
              String playerName = state.getText();
              Player target = Bukkit.getPlayerExact(playerName);
              if (target == null) {
                return List.of(AnvilGUI.ResponseAction.replaceInputText(LanguageManager.getString(player, "friends.gui.invite.not-found")));
              }
              FriendsManager.inviteFriend(state.getPlayer(), target);
              return List.of(AnvilGUI.ResponseAction.close());
            })
            .open(player);
      }
      case "delete_button" -> {
        player.closeInventory();
        new AnvilGUI.Builder()
            .plugin(ArcadeNetworkCitybuild.getInstance())
            .title(LanguageManager.getString(player, "friends.gui.delete.title"))
            .text(LanguageManager.getString(player, "friends.gui.delete.text"))
            .onClick((slot, state) -> {
              if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
              String playerName = state.getText();
              Player target = Bukkit.getPlayerExact(playerName);
              if (target == null) {
                return List.of(AnvilGUI.ResponseAction.replaceInputText(LanguageManager.getString(player, "friends.gui.delete.not-found")));
              }
              FriendsManager.deleteFriend(state.getPlayer(), target, true);
              return List.of(AnvilGUI.ResponseAction.close());
            })
            .open(player);
      }
    }
    if (buttonId.startsWith("requestButton")) {
      if (clickType.isLeftClick()) accept(buttonId, gui, player, clickType);
      if (clickType.isRightClick()) reject(buttonId, gui, player, clickType);
    }
  }

  @Override
  public void switchPage(String buttonId, Gui gui, int page, Player player, ClickType clickType) {

  }

  @Override
  public void closeGui(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void accept(String buttonId, Gui gui, Player player, ClickType clickType) {
    FriendsManager.acceptFriendRequest(Bukkit.getOfflinePlayer(UUID.fromString(((GuiButton) gui.getElement(buttonId)).customData.get("uuid"))), player);
    FriendsManager.openGui(player);
  }

  @Override
  public void reject(String buttonId, Gui gui, Player player, ClickType clickType) {
    FriendsManager.rejectFriendRequest(Bukkit.getOfflinePlayer(UUID.fromString(((GuiButton) gui.getElement(buttonId)).customData.get("uuid"))), player);
    FriendsManager.openGui(player);
  }

  @Override
  public void onSwitch(String buttonId, Gui gui, boolean state, Player player, ClickType clickType) {

  }
}
