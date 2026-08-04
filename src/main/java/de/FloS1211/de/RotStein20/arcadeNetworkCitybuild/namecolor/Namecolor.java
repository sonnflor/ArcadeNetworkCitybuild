package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public record Namecolor(String name, NamedTextColor col1, NamedTextColor col2, Material item,
                        String type) {
}
