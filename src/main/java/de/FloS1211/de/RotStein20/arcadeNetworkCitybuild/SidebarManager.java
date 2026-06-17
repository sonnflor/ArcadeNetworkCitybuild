package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinsManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SidebarManager {
  public static void startUpdateCycle() {
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          setSidebar(player);
        }
      }
    }.runTaskTimer(ArcadeNetworkCitybuild.getInstance(), 0L, 5L);
  }
  public static void setSidebar(Player player) {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();

    Objective objective = board.registerNewObjective("sidebar", "dummy", Component.text("§3§lArcadeCraft"));
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    // Du kannst hier dynamisch Infos reinsetzen, z.B. Uhrzeit mit SimpleDateFormat
    String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    if (manager.getMainScoreboard().getObjective("coins") == null) {
      manager.getMainScoreboard().registerNewObjective("coins", Criteria.DUMMY, Component.text("Coins"));
    }
    String world = "Citybuild";
    BigDecimal coins = CoinsManager.getCoins(player.getUniqueId().toString());
    int i = -1;


    objective.getScore("§0").setScore(i+10);
    objective.getScore("§f§l" + world).setScore(i+9);
    objective.getScore("§1").setScore(i+8);
    objective.getScore("§7§lName: " + PlayerNameManager.getPlayerNameString(player.getUniqueId().toString())).setScore(i+7);
    objective.getScore("§2").setScore(i+6);
    objective.getScore("§7§lCoins: §r§f" + coins).setScore(i+5);
    objective.getScore("§3").setScore(i+4);
    objective.getScore("§7§lDatum: §f" + date).setScore(i+3);
    objective.getScore("§7§lUhrzeit: §f" + time).setScore(i+2);
    objective.getScore("§4").setScore(i+1);

    player.setScoreboard(board);
  }
}
