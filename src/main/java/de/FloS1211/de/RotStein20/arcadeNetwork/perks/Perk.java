package de.FloS1211.de.RotStein20.arcadeNetwork.perks;

import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.LanguageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Locale;
import java.util.function.BiConsumer;

public class Perk {
  private String perkId;
  private Material displayedItem;
  private PotionEffect simplePotionEffect;
  private BiConsumer<Player, Boolean> complexSingleAction;

  public Perk(String perkId,
              Material displayedItem,
              PotionEffect simplePotionEffect,
              BiConsumer<Player, Boolean> complexSingleAction)  {
    this.perkId = perkId;
    this.displayedItem = displayedItem;
    this.simplePotionEffect = simplePotionEffect;
    this.complexSingleAction = complexSingleAction;
  }

  public String perkId() {
    return perkId;
  }
  public Material displayedItem() {
    return displayedItem;
  }
  public PotionEffect simplePotionEffect() {
    return simplePotionEffect;
  }
  public BiConsumer<Player, Boolean> complexSingleAction() {
    return complexSingleAction;
  }
  public String name() {
    return LanguageManager.getString(Locale.ENGLISH, "perk.general.perk-name."+perkId);
  }
  public String description() {
    return LanguageManager.getString(Locale.ENGLISH, "perk.general.perk-description."+perkId);
  }
  public String name(Player player) {
    return LanguageManager.getString(player, "perk.general.perk-name."+perkId);
  }
  public String description(Player player) {
    return LanguageManager.getString(player, "perk.general.perk-description."+perkId);
  }
}
