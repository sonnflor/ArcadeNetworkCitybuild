package de.FloS1211.de.RotStein20.arcadeNetwork.shop;

import org.bukkit.block.Shelf;

import java.util.List;

public record ShopSession (Shelf shelf, List<Integer> indices) {
}
