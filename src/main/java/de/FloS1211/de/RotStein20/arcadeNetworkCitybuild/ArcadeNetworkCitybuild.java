package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.PerformactionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.AuszahlenExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinRightclickListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.PayAllExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.PayExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorGuiListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position.PositionTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.randomStuff.rdExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PermissionManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.SetRankExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.rank.SetRankTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar.ConfigSidebarExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar.ConfigSidebarTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sidebar.SidebarManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sign.SignExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters.EmptyTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters.OfflinePlayerTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tpa.TpaTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position.PositionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.tabcompleters.PlayerTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.vanish.UnvanishExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.vanish.VanishExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.slf4j.Logger;

import java.util.*;

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
    Bukkit.getMessenger().registerOutgoingPluginChannel(this, "arcadenetwork:tab");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "arcadenetwork:chat");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "arcadenetwork:tpa");
    getServer().getMessenger().registerIncomingPluginChannel(this, "arcadenetwork:tpa", new TpaListener());

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
    getCommand("invsee").setTabCompleter(new InvseeTabCompleter());

    getCommand("position").setExecutor(new PositionExecutor());
    getCommand("position").setTabCompleter(new PositionTabCompleter());

    getCommand("sign").setExecutor(new SignExecutor());
    getCommand("sign").setTabCompleter(new EmptyTabCompleter());

    Bukkit.getPluginManager().registerEvents(new SidebarManager(), this);
    Bukkit.getPluginManager().registerEvents(new ChatMessageManager(), this);
    getCommand("configsidebar").setExecutor(new ConfigSidebarExecutor());
    getCommand("configsidebar").setTabCompleter(new ConfigSidebarTabCompleter());

    getCommand("tempban").setExecutor(new TempBanExecutor());
    getCommand("tempban").setTabCompleter(new TempbanTabCompleter());

    Bukkit.getPluginManager().registerEvents(new InvseeManager(), this);

    getCommand("müll").setExecutor(new MuellExecutor());
    getCommand("müll").setTabCompleter(new EmptyTabCompleter());

    Bukkit.getPluginManager().registerEvents(new AntiXashinnListener(), this);

    getCommand("ping").setExecutor(new PingExecutor());
    getCommand("ping").setTabCompleter(new PlayerTabCompleter());

    getCommand("discord").setExecutor(new DiscordExecutor());
    getCommand("discord").setTabCompleter(new EmptyTabCompleter());

    Bukkit.getPluginManager().registerEvents(new LobbyProtectListener(), this);

    getCommand("testcom").setExecutor(new TestCom());

    Bukkit.getPluginManager().registerEvents(new TabListManager(), this);

    getCommand("vanish").setExecutor(new VanishExecutor());
    getCommand("vanish").setTabCompleter(new EmptyTabCompleter());
    getCommand("unvanish").setExecutor(new UnvanishExecutor());
    getCommand("unvanish").setTabCompleter(new EmptyTabCompleter());

    getCommand("testi").setExecutor(new rdExecutor());
    Bukkit.getPluginManager().registerEvents(new GuiManager(), this);
  }
}
