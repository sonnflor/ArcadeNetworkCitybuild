package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.shop;

import org.bukkit.block.Shelf;

import java.util.List;

public record ShopSession (Shelf shelf, List<Integer> indices) {
}
