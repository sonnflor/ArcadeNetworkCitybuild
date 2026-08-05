package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.DatabaseManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.SQLTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PerkManager {
  public static LinkedHashMap<String, Perk> perks = new LinkedHashMap<>() {
    {
      put("frog", new Perk("frog", "Frosch Perk", "Lässt dich höher Springen (Jump Boost 2)", Material.FROG_SPAWN_EGG, new PotionEffect(PotionEffectType.JUMP_BOOST, 15 * 20, 1), null));
      put("speed", new Perk("speed", "Speed Perk", "Macht dich schneller (Speed 2)", Material.DIAMOND_BOOTS, new PotionEffect(PotionEffectType.SPEED, 15 * 20, 1), null));
      put("nokia", new Perk("nokia", "Nokia Perk", "Macht dich resistenter (Resistance 4)", Material.BRICK, new PotionEffect(PotionEffectType.RESISTANCE, 15 * 20, 3), null));
      put("miner", new Perk("miner", "Miner Perk", "Lässt dich schneller Blöcke abbauen (Haste 2)", Material.DIAMOND_PICKAXE, new PotionEffect(PotionEffectType.HASTE, 15 * 20, 1), null));
      put("irongolem", new Perk("irongolem", "Eisengolem Perk", "Macht dich stärker (Strength 2)", Material.MACE, new PotionEffect(PotionEffectType.STRENGTH, 15 * 20, 1), null));
      put("heal", new Perk("heal", "Heilungs Perk", "Lässt dich schneller regenerieren (Regeneration 1)", Material.ENCHANTED_GOLDEN_APPLE, new PotionEffect(PotionEffectType.REGENERATION, 15 * 20, 0), null));
      put("fish", new Perk("fish", "Fisch Perk", "Lässt dich unterwasser atmen (Water Breathing 1)", Material.SALMON, new PotionEffect(PotionEffectType.WATER_BREATHING, 15 * 20, 0), null));
      put("no_hunger", new Perk("no_hunger", "Kein Hunger Perk", "Du hast nie wieder Hunger (Saturation 1)", Material.COOKED_BEEF, new PotionEffect(PotionEffectType.SATURATION, 15 * 20, 0), null));
      put("bird", new Perk("bird", "Vogel Perk", "Lässt dich fliegen, wie im Kreativ-Modus", Material.ELYTRA, null, PerkEffectListener::switchBirdPerk));
      put("cat", new Perk("cat", "Katzen Perk", "Du erhältst keinen Fallschaden", Material.CAT_SPAWN_EGG, null, null));
      put("telekinesis", new Perk("telekinesis", "Telekinese Perk", "Gedropte Items werden in dein Inventar teleportiert", Material.ENDER_EYE, null, null));
      put("dolphin", new Perk("dolphin", "Delfin Perk", "Du kannst im Wasser nach vorn schießen, wie mit einem Dreizack", Material.DOLPHIN_SPAWN_EGG, new PotionEffect(PotionEffectType.DOLPHINS_GRACE,15*20,0), null));
      put("xp", new Perk("xp", "Doppelte XP Perk", "Du erhältst doppelte XP", Material.EXPERIENCE_BOTTLE, null, null));
      put("keep_inv", new Perk("keep_inv", "Keep Inventory Perk", "Du behältst dein Inventar nach dem Tod", Material.CHEST, null, null));
      put("rocket", new Perk("rocket", "Raketen Perk", "Du kannst dich mit Shift im Elytraflug nach vorn boosten", Material.FIREWORK_ROCKET, null, null));
      put("ghost", new Perk("ghost", "Geister Perk", "Du bist unsichtbar (Invisibility)", Material.VEX_SPAWN_EGG, new PotionEffect(PotionEffectType.INVISIBILITY, 15 * 20, 0), null));
      put("lava", new Perk("lava", "Lava Perk", "Du erhällst keinen Feuerschaden (Fire Resistance)", Material.LAVA_BUCKET, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 15 * 20, 0), null));
    }};
  public static void switchPerk(OfflinePlayer player, Perk perk, boolean state) {
    DatabaseManager.executeSQL("UPDATE perks SET " + perk.perkId() + " = ? WHERE uuid = ?", List.of(state ? 2 : 1, player.getUniqueId().toString()));
    if (perk.complexSingleAction() != null) {
      if (player.isOnline()) {
        Player onlinePlayer = (Player) player;
        perk.complexSingleAction().accept(onlinePlayer, state);
      }
    }
    if (player.isOnline()) {
      Player onlinePlayer = (Player) player;
      onlinePlayer.sendMessage(MessageManager.get("perk-switch-perk", Map.of("perk", perk.name(),"state", state ? "<green>aktiviert</green>" : "<red>deaktiviert</red>")));
      updatePerks(onlinePlayer);
    }
  }

  public static void gainPerk(OfflinePlayer player, String perkId) {
    DatabaseManager.executeSQL("UPDATE perks SET " + perkId + " = ? WHERE uuid = ?", List.of(1, player.getUniqueId().toString()));
    if (player.isOnline()) {
      Player onlinePlayer = (Player) player;
      onlinePlayer.sendMessage(MessageManager.get("perk-gain-perk", Map.of("perkName", perks.get(perkId).name())));
    }
  }

  public static Map<String, Integer> getPerkStates(OfflinePlayer player) {
    SQLTable perkStates = DatabaseManager.getTable("perks", "uuid = ?", List.of(player.getUniqueId().toString()));
    Map<String, Integer> perkStateMap = new HashMap<>();
    if (perkStates.isEmpty()) {
      DatabaseManager.executeSQL("INSERT INTO perks (uuid) VALUES (?)", List.of(player.getUniqueId().toString()));
      perkStates = DatabaseManager.getTable("perks", "uuid = ?", List.of(player.getUniqueId().toString()));
    }
    for (String perkId : perks.sequencedKeySet()) {
      perkStateMap.put(perkId, perkStates.getValue(perkId, 0));
    }
    return perkStateMap;
  }

  public static boolean isPerkActive(OfflinePlayer player, String perkId) {
    Map<String, Integer> perkStates = getPerkStates(player);
    return perkStates.getOrDefault(perkId, 0) == 2;
  }

  public static void openPerkMenu(Player player) {
    Gui gui = new Gui(27, Component.text("Perks"));
    Map<String, Integer> perkStates = getPerkStates(player);
    gui.setElementInBottomBar(GuiButton.getPrevPageButton(new PerkGuiButtonExecutor()),0);
    gui.setElementInBottomBar(GuiButton.getNextPageButton(new PerkGuiButtonExecutor()),8);
    int i = 0;
    for (Perk perk : perks.sequencedValues()) {
      int state = perkStates.getOrDefault(perk.perkId(), 0);
      gui.setElement(new GuiDisplay("perk_" + perk.perkId(), perk.displayedItem())
          .title(Component.text(perk.name()))
          .lore(List.of(Component.text(perk.description()))),(int) Math.floor(i / 4f),(i%4)*2+1);
      GuiElement<?> stateDisplay;
      if (state == 0) {
        stateDisplay = new GuiDisplay("perk_state_" + perk.perkId(), Material.LIGHT_GRAY_DYE)
            .title(Component.text("Nicht freigeschaltet"));
      } else {
        stateDisplay = new GuiSwitch(
            "perk_state_" + perk.perkId(),
            new GuiDisplay("perk_state_display_active_" + perk.perkId(), Material.LIME_DYE)
                .title(Component.text("Aktiviert").color(NamedTextColor.GREEN)),
            new GuiDisplay("perk_state_display_inactive_"+perk.perkId(), Material.RED_DYE)
                .title(Component.text("Deaktiviert").color(NamedTextColor.RED)),
            new PerkGuiButtonExecutor(),
            state == 2
        );
      }
      gui.setElement(stateDisplay,(int) Math.floor(i / 4f),(i%4)*2+1+9);
      i++;
    }
    player.openInventory(gui.buildInventory());
  }

  public static void updatePerks(Player player) {
    Map<String, Integer> perkStates = getPerkStates(player);
    for (Map.Entry<String, Integer> entry : perkStates.entrySet()) {
      int state = entry.getValue();
      if (state != 2) continue;
      String perkId = entry.getKey();
      Perk perk = perks.get(perkId);
      if (perk != null) {
        if (perk.simplePotionEffect() != null) {
          player.addPotionEffect(perk.simplePotionEffect());
        }
      }
    }
  }
}
