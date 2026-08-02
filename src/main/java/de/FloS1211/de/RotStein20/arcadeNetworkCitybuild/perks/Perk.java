package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record Perk(String perkId, String name, String description, Material displayedItem, PotionEffect simplePotionEffect, BiConsumer<Player, Boolean> complexSingleAction) {
}
