package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages.PerformactionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.AuszahlenExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.CoinRightclickListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.PayAllExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.coins.PayExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends.FriendsExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.friends.FriendsTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.invsee.InvseeTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mail.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute.*;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.namecolor.NamecolorGuiListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks.PerkCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks.PerkExecutor;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks.PerkEffectListener;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.perks.PerkManager;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.position.PositionTabCompleter;
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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
    saveResource("messages.yml", true);
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
    initConins();
    initPay();
    initColorCodes();
    initServerStructure();
    initRank();
    initNameColor();
    initMute();
    initTpa();
    initInvsee();
    initPosition();
    initSign();
    initSidBar();
    initBan();
    initMüll();
    initPing();
    initDiscord();
    initVanish();
    initFriends();
    initMail();
    initSql();
    initPerks();
    initRemoteCommands();
  }
  private void initConins(){
    getCommand("auszahlen").setExecutor(new AuszahlenExecutor());
    getCommand("auszahlen").setTabCompleter(new EmptyTabCompleter());
    Bukkit.getPluginManager().registerEvents(new CoinRightclickListener(), this);
  }
  private void initPay(){
    getCommand("pay").setExecutor(new PayExecutor());
    getCommand("pay").setTabCompleter(new PlayerTabCompleter());
    getCommand("payall").setExecutor(new PayAllExecutor());
    getCommand("payall").setTabCompleter(new EmptyTabCompleter());
  }
  private void initColorCodes(){
    getCommand("colorcodes").setExecutor(new ColorcodesExecutor());
    getCommand("colorcodes").setTabCompleter(new EmptyTabCompleter());
  }
  private void initServerStructure(){
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
    SidebarManager.startUpdateCycle();
    Bukkit.getPluginManager().registerEvents(new CustomGuiHolder(), this);
    getCommand("performaction").setExecutor(new PerformactionExecutor());
    Bukkit.getPluginManager().registerEvents(new AntiXashinnListener(), this);
    Bukkit.getPluginManager().registerEvents(new LobbyProtectListener(), this);
    getCommand("testcom").setExecutor(new TestCom());
    Bukkit.getPluginManager().registerEvents(new TabListManager(), this);
    Bukkit.getPluginManager().registerEvents(new GuiManager(), this);
  }
  private void initRank(){
    PermissionManager.initialize();
    getCommand("setrank").setExecutor(new SetRankExecutor());
    getCommand("setrank").setTabCompleter(new SetRankTabCompleter());
    Bukkit.getPluginManager().registerEvents(new PlayerNameManager(), this);
    Bukkit.getPluginManager().registerEvents(new PermissionManager(), this);
    getCommand("getcustomitem").setExecutor(new GetCustomItemExecutor());
    getCommand("getcustomitem").setTabCompleter(new GetCustomItemTabCompleter());
  }
  private void initNameColor(){
    getCommand("namecolor").setExecutor(new NamecolorExecutor());
    getCommand("namecolor").setTabCompleter(new EmptyTabCompleter());
    Bukkit.getPluginManager().registerEvents(new NamecolorGuiListener(), this);
    Bukkit.getPluginManager().registerEvents(new NamecolorCouponManager(), this);
  }
  private void initMute(){
    getCommand("mute").setExecutor(new MuteExecutor());
    getCommand("mute").setTabCompleter(new MuteTabCompleter());
    getCommand("unmute").setExecutor(new UnmuteExecutor());
    getCommand("unmute").setTabCompleter(new OfflinePlayerTabCompleter());
    getCommand("tempmute").setExecutor(new TempmuteExecutor());
    getCommand("tempmute").setTabCompleter(new TempmuteTabCompleter());
    getCommand("muteinfo").setExecutor(new MuteinfoExecutor());
    getCommand("muteinfo").setTabCompleter(new OfflinePlayerTabCompleter());
    Bukkit.getPluginManager().registerEvents(new MuteManager(), this);
  }
  private void initTpa(){
    getCommand("tpa").setExecutor(new TpaExecutor());
    getCommand("tpa").setTabCompleter(new TpaTabCompleter());
  }
  private void initInvsee(){
    Bukkit.getPluginManager().registerEvents(new InvseeManager(), this);
    getCommand("invsee").setExecutor(new InvseeExecutor());
    getCommand("invsee").setTabCompleter(new InvseeTabCompleter());
  }
  private void initPosition(){
    getCommand("position").setExecutor(new PositionExecutor());
    getCommand("position").setTabCompleter(new PositionTabCompleter());
  }
  private void initSign(){
    getCommand("sign").setExecutor(new SignExecutor());
    getCommand("sign").setTabCompleter(new EmptyTabCompleter());
  }
  private void initSidBar(){
    Bukkit.getPluginManager().registerEvents(new SidebarManager(), this);
    Bukkit.getPluginManager().registerEvents(new ChatMessageManager(), this);
    getCommand("configsidebar").setExecutor(new ConfigSidebarExecutor());
    getCommand("configsidebar").setTabCompleter(new ConfigSidebarTabCompleter());
  }
  private void initBan(){
    getCommand("tempban").setExecutor(new TempBanExecutor());
    getCommand("tempban").setTabCompleter(new TempbanTabCompleter());
  }
  private void initMüll(){
    getCommand("müll").setExecutor(new MuellExecutor());
    getCommand("müll").setTabCompleter(new EmptyTabCompleter());
  }
  private void initPing(){
    getCommand("ping").setExecutor(new PingExecutor());
    getCommand("ping").setTabCompleter(new PlayerTabCompleter());
  }
  private void initDiscord(){
    getCommand("discord").setExecutor(new DiscordExecutor());
    getCommand("discord").setTabCompleter(new EmptyTabCompleter());
  }
  private void initVanish(){
    getCommand("vanish").setExecutor(new VanishExecutor());
    getCommand("vanish").setTabCompleter(new EmptyTabCompleter());
    getCommand("unvanish").setExecutor(new UnvanishExecutor());
    getCommand("unvanish").setTabCompleter(new EmptyTabCompleter());
  }
  private void initFriends(){
    getCommand("friends").setExecutor(new FriendsExecutor());
    getCommand("friends").setTabCompleter(new FriendsTabCompleter());
  }
  private void initMail(){
    getCommand("mail").setExecutor(new MailExecutor());
    getCommand("mail").setTabCompleter(new MailTabCompleter());
    Bukkit.getPluginManager().registerEvents(new MailListener(), this);
  }

  private void initSql() {
    getCommand("sql").setExecutor(new SqlExecutor());
  }
  private void initPerks() {
    getCommand("perks").setExecutor(new PerkExecutor());
    getCommand("perks").setTabCompleter(new EmptyTabCompleter());
    Bukkit.getPluginManager().registerEvents(new PerkEffectListener(), this);
    Bukkit.getPluginManager().registerEvents(new PerkCouponManager(), this);
    Bukkit.getScheduler().runTaskTimer(this, () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        PerkManager.updatePerks(player);
      }
    }, 0L, 20L * 10L);
  }

  private void initRemoteCommands() {
    CommandPoller.start();
    getLogger().info("PollingPlugin aktiviert.");
    getCommand("fastpoll").setExecutor(new FastPollExecutor());
    getCommand("fastpoll").setTabCompleter(new EmptyTabCompleter());
  }
}

