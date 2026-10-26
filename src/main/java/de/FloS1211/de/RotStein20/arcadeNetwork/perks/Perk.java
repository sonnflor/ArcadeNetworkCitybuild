package de.FloS1211.de.RotStein20.arcadeNetwork.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.function.BiConsumer;

public record Perk(String perkId, String name, String description, Material displayedItem, PotionEffect simplePotionEffect, BiConsumer<Player, Boolean> complexSingleAction) {
}
