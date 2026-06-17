package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class Namecolor {
  private final String name;
  private final NamedTextColor col1;
  private final NamedTextColor col2;
  private final Material item;
  private final String type;

  public Namecolor(String name, NamedTextColor col1, NamedTextColor col2, Material item, String type) {
    this.name = name;
    this.col1 = col1;
    this.col2 = col2;
    this.item = item;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public NamedTextColor getCol1() {
    return col1;
  }

  public NamedTextColor getCol2() {
    return col2;
  }

  public Material getItem() {
    return item;
  }

  public String getType() {
    return type;
  }
}
