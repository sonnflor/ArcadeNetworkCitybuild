package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum GuiSound {
  CLICK(Sound.UI_BUTTON_CLICK),
  SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
  FAILURE(Sound.ENTITY_VILLAGER_NO),
  TOGGLE_ON(Sound.BLOCK_LEVER_CLICK),
  TOGGLE_OFF(Sound.BLOCK_LEVER_CLICK),
  PAGE_FLIP(Sound.ITEM_BOOK_PAGE_TURN);

  private final Sound sound;

  GuiSound(Sound sound) {
    this.sound = sound;
  }

  public void play(Player player) {
    player.playSound(player.getLocation(), sound, 1f, 1f);
  }
}
