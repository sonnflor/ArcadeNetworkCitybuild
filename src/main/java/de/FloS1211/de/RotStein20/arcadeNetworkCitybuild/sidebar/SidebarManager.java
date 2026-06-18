package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.Utils;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinsManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SidebarManager implements Listener {
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    if (!event.getPlayer().hasPlayedBefore()) {
      event.getPlayer().getPersistentDataContainer().set(Utils.key_sidebar,PersistentDataType.STRING,"coins,date,time");
    }
  }

  public static void startUpdateCycle() {
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          setSidebar(player);
        }
      }
    }.runTaskTimer(ArcadeNetworkCitybuild.getInstance(), 0L, 1L);
  }
  public static void setSidebar(Player player) {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();

    Objective objective = board.registerNewObjective("sidebar", "dummy", Component.text("§3§lArcadeCraft"));
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    String world = "Citybuild";
    List<String> elements = getSidebarElements(player);
    elements = elements.reversed();
    elements.add(getName(player));
    elements.add("§f§l"+world);

    int i = 0;
    for (String el : elements) {
      objective.getScore(el).setScore(i*2);
      i++;
    }

    player.setScoreboard(board);
  }

  private static List<String> getEnablesElements(Player player) {
    String data = player.getPersistentDataContainer().get(Utils.key_sidebar,PersistentDataType.STRING);
    if (data == null) return List.of();
    return List.of(data.split(","));
  }

  static List<String> features = List.of("coins","kills","deaths","height","pos","velocity","date","time");

  private static List<String> getSidebarElements(Player player) {
    List<String> result = new ArrayList<>();
    List<String> enabled = getEnablesElements(player);
    for (String feature : features) {
      if (enabled.contains(feature)) {
        result.add(getSidebarElement(feature, player));
      }
    }
    return result;
  }

  private static String getSidebarElement(String name, Player player) {
    String result = "";
    switch (name) {
      case "coins" -> result=getCoins(player.getUniqueId().toString());
      case "kills" -> result=getKills(player);
      case "deaths" -> result=getDeaths(player);
      case "height" -> result=getHeight(player);
      case "pos" -> result=getPos(player);
      case "velocity" -> result=getVelocity(player);
      case "date" -> result=getDate();
      case "time" -> result=getTime();
    }
    return result;
  }

  private static String getName(Player player) {
    return "§7§lName: " + PlayerNameManager.getPlayerNameString(player.getUniqueId().toString());
  }

  private static String getCoins(String uuid) {
    return "§7§lCoins: §r§f"+CoinsManager.getCoins(uuid);
  }

  private static String getKills(Player player) {
    return "§7§lKills: §r§f"+player.getStatistic(Statistic.PLAYER_KILLS);
  }

  private static String getDeaths(Player player) {
    return "§7§lDeaths: §r§f"+player.getStatistic(Statistic.DEATHS);
  }

  private static String getHeight(Player player) {
    int distance = 0;
    Location loc = player.getLocation();
    for (int blocks = 0; !(player.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY()-blocks,loc.getBlockZ()).isSolid())&&loc.getBlockY() - blocks > player.getWorld().getMinHeight(); blocks++) {
      distance = blocks;
    }
    return "§7§lAbstand zum Boden: §r§f"+distance+"m";
  }

  private static String getPos(Player player) {
    Location loc = player.getLocation();
    return "§7§lKoordinaten: §r§f"+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ();
  }

  private static final Map<UUID, Location> lastLocations = new HashMap<>();

  private static String getVelocity(Player player) {
    Location current = player.getLocation();
    Location last = lastLocations.get(player.getUniqueId());
    String result = "";

    if (last != null) {
      double distancePerTick = current.distance(last);
      double blocksPerSecond = distancePerTick * 20;
      result = "§7§lGeschwindigkeit: §r§f"+String.format("%.3f", blocksPerSecond);
    }

    lastLocations.put(player.getUniqueId(), current.clone());
    return result;
  }

  private static String getDate() {
    return "§7§lDatum: §r§f"+LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  private static String getTime() {
    return "§7§lZeit: §r§f"+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
  }
}
