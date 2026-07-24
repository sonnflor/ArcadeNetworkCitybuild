package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.randomStuff;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class rdExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
    Gui gui = new Gui(new HashMap<>(),27, Component.text("Test Stuff"));
    GuiDisplay nameDisplay = new GuiDisplay("nameD", Material.NAME_TAG);
    nameDisplay.setTitle(Component.text("Name halt"));
    gui.setElement(nameDisplay,0,0);
    Player player = (Player) commandSender;
    player.openInventory(gui.buildInventory());
    return true;
  }
}
