package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.PerformactionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.AuszahlenExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinRightclickListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.PayAllExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.PayExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorGuiListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PermissionManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.SetRankExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.SetRankTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sign.SignExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters.EmptyTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters.OfflinePlayerTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.CustomGuiHolder;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.PositionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.GetCustomItemTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters.PlayerTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.ColorcodesExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.GetCustomItemExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ArcadeNetworkCitybuild extends JavaPlugin {

  //Sidebar
  public Map<UUID, Location> lastPos = new HashMap<>();
  //SQL
  public String databasePath = getDataFolder().getAbsolutePath() + "/ArcadeCraftPlugin.db";
  public static ArcadeNetworkCitybuild instance;
  private DatabaseManager databaseManager;

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    saveResource("messages.yml",true);
    MessageManager.load();

    Utils.init();
    registerFeatures();
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

    databaseManager = new DatabaseManager();
    try {
      databaseManager.connect();
      databaseManager.createTables();
    } catch (Exception e) {
      e.printStackTrace();
      getServer().getPluginManager().disablePlugin(this);
    }
  }

  public static ArcadeNetworkCitybuild getInstance() {
    return instance;
  }

  @Override
  public void onDisable() {
    if (databaseManager != null) {
      databaseManager.disconnect();
    }
  }

  public DatabaseManager getDatabaseManager() {
    return databaseManager;
  }

  private void registerFeatures() {
    getCommand("auszahlen").setExecutor(new AuszahlenExecutor());
    getCommand("auszahlen").setTabCompleter(new EmptyTabCompleter());
    Bukkit.getPluginManager().registerEvents(new CoinRightclickListener(), this);
    getCommand("pay").setExecutor(new PayExecutor());
    getCommand("pay").setTabCompleter(new PlayerTabCompleter());
    getCommand("payall").setExecutor(new PayAllExecutor());
    getCommand("payall").setTabCompleter(new EmptyTabCompleter());

    getCommand("colorcodes").setExecutor(new ColorcodesExecutor());
    getCommand("colorcodes").setTabCompleter(new EmptyTabCompleter());

    getCommand("citybuild").setExecutor(new CitybuildTpExecutor());
    getCommand("citybuild").setTabCompleter(new EmptyTabCompleter());
    getCommand("farming").setExecutor(new FarmingTpExecutor());
    getCommand("farming").setTabCompleter(new EmptyTabCompleter());
    getCommand("lobby").setExecutor(new LobbyTpExecutor());
    getCommand("lobby").setTabCompleter(new EmptyTabCompleter());
    getCommand("arcadeattack").setExecutor(new ArcadeAttackTpExecutor());
    getCommand("arcadeattack").setTabCompleter(new EmptyTabCompleter());
    org.bukkit.Bukkit.getPluginManager().registerEvents(new de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.arcadeattack.ArcadeAttackEggListener(), this);
    de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.arcadeattack.ArcadeAttackEggListener.startEggParticleTask();

    PermissionManager.initialize();
    getCommand("setrank").setExecutor(new SetRankExecutor());
    getCommand("setrank").setTabCompleter(new SetRankTabCompleter());
    Bukkit.getPluginManager().registerEvents(new PlayerNameManager(), this);
    Bukkit.getPluginManager().registerEvents(new PermissionManager(), this);

    getCommand("getcustomitem").setExecutor(new GetCustomItemExecutor());
    getCommand("getcustomitem").setTabCompleter(new GetCustomItemTabCompleter());

    getCommand("namecolor").setExecutor(new NamecolorExecutor());
    getCommand("namecolor").setTabCompleter(new EmptyTabCompleter());
    Bukkit.getPluginManager().registerEvents(new NamecolorGuiListener(), this);
    Bukkit.getPluginManager().registerEvents(new NamecolorCouponManager(), this);

    getCommand("mute").setExecutor(new MuteExecutor());
    getCommand("mute").setTabCompleter(new MuteTabCompleter());
    getCommand("unmute").setExecutor(new UnmuteExecutor());
    getCommand("unmute").setTabCompleter(new OfflinePlayerTabCompleter());
    getCommand("tempmute").setExecutor(new TempmuteExecutor());
    getCommand("tempmute").setTabCompleter(new TempmuteTabCompleter());
    getCommand("muteinfo").setExecutor(new MuteinfoExecutor());
    getCommand("muteinfo").setTabCompleter(new OfflinePlayerTabCompleter());
    Bukkit.getPluginManager().registerEvents(new MuteManager(), this);

    SidebarManager.startUpdateCycle();

    Bukkit.getPluginManager().registerEvents(new CustomGuiHolder(), this);

    getCommand("tpa").setExecutor(new TpaExecutor());
    getCommand("tpa").setTabCompleter(new TpaTabCompleter());
    getCommand("performaction").setExecutor(new PerformactionExecutor());

    getCommand("invsee").setExecutor(new InvseeExecutor());
    getCommand("invsee").setTabCompleter(new PlayerTabCompleter());

    getCommand("position").setExecutor(new PositionExecutor());
    getCommand("position").setTabCompleter(new PlayerTabCompleter());

    getCommand("sign").setExecutor(new SignExecutor());
    getCommand("sign").setTabCompleter(new EmptyTabCompleter());
  }
}
