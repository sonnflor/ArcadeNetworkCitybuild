package de.FloS1211.de.RotStein20.arcadeNetwork;

import de.FloS1211.de.RotStein20.arcadeNetwork.arcadeattack.ArcadeAttackEggListener;
import de.FloS1211.de.RotStein20.arcadeNetwork.clickableMessages.PerformactionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.coins.AuszahlenExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.coins.CoinRightclickListener;
import de.FloS1211.de.RotStein20.arcadeNetwork.coins.PayAllExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.coins.PayExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.friends.FriendsExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.friends.FriendsTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.gui.GuiManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.invsee.InvseeExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.invsee.InvseeManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.invsee.InvseeTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.mail.*;
import de.FloS1211.de.RotStein20.arcadeNetwork.mute.*;
import de.FloS1211.de.RotStein20.arcadeNetwork.namecolor.NamecolorCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.namecolor.NamecolorExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.namecolor.NamecolorGuiListener;
import de.FloS1211.de.RotStein20.arcadeNetwork.perks.PerkCouponManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.perks.PerkExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.perks.PerkEffectListener;
import de.FloS1211.de.RotStein20.arcadeNetwork.perks.PerkManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.position.PositionTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.proxyTeleport.*;
import de.FloS1211.de.RotStein20.arcadeNetwork.rank.PermissionManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.rank.PlayerNameManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.rank.SetRankExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.rank.SetRankTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.remoteConsole.CommandPoller;
import de.FloS1211.de.RotStein20.arcadeNetwork.remoteConsole.FastPollingExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure.*;
import de.FloS1211.de.RotStein20.arcadeNetwork.shop.*;
import de.FloS1211.de.RotStein20.arcadeNetwork.sidebar.ConfigSidebarExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.sidebar.ConfigSidebarTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.sidebar.SidebarManager;
import de.FloS1211.de.RotStein20.arcadeNetwork.sign.SignExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.tabcompleters.EmptyTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.tabcompleters.OfflinePlayerTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.tpa.TpaExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.tpa.TpaListener;
import de.FloS1211.de.RotStein20.arcadeNetwork.tpa.TpaTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.utils.*;
import de.FloS1211.de.RotStein20.arcadeNetwork.position.PositionExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.tabcompleters.PlayerTabCompleter;
import de.FloS1211.de.RotStein20.arcadeNetwork.vanish.UnvanishExecutor;
import de.FloS1211.de.RotStein20.arcadeNetwork.vanish.VanishExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class ArcadeNetwork extends JavaPlugin {
  //Sidebar
  public Map<UUID, Location> lastPos = new HashMap<>();
  //SQL
  public String databasePath = getDataFolder().getAbsolutePath() + "/ArcadeCraftPlugin.db";
  public static ArcadeNetwork instance;
  private DatabaseManager databaseManager;
  //shop
  public ShopManager shopManager;
  //features
  public FeatureManager featureManager;

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
    FeatureVisibilityListener.disabledCommands.clear();
    featureManager = new FeatureManager(getConfig());
    saveResource("messages.yml", true);
    MessageManager.load();

    Utils.init();
    registerFeatures();
    getServer().getPluginManager().registerEvents(new FeatureVisibilityListener(this), this);
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    if (featureManager.getState("proxy")) {
      Bukkit.getMessenger().registerOutgoingPluginChannel(this, "arcadenetwork:tab");
      getServer().getMessenger().registerOutgoingPluginChannel(this, "arcadenetwork:chat");
      getServer().getMessenger().registerOutgoingPluginChannel(this, "arcadenetwork:tpa");
      getServer().getMessenger().registerIncomingPluginChannel(this, "arcadenetwork:tpa", new TpaListener());
    }
    LanguageManager.load();

    databaseManager = new DatabaseManager();
    try {
      databaseManager.connect();
      databaseManager.createTables();
    } catch (Exception e) {
      e.printStackTrace();
      getServer().getPluginManager().disablePlugin(this);
    }
  }

  public static ArcadeNetwork getInstance() {
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
    initCoins(featureManager.getState("coins"));
    initPay(featureManager.getState("pay"));
    initColorCodes(featureManager.getState("colorcodes"));
    initProxyTeleport(featureManager.getState("proxy_teleport"));
    initArcadeAttack(featureManager.getState("arcade_attack"));
    initServerStructure(featureManager.getState("server_structure"));
    initSidebar(featureManager.getState("sidebar"));
    if (featureManager.getState("anti_xashinn")) Bukkit.getPluginManager().registerEvents(new AntiXashinnListener(), this);
    initSpawn(featureManager.getState("spawn"));
    initLobby(featureManager.getState("lobby"));
    initRank(featureManager.getState("rank"));
    initNameColor(featureManager.getState("namecolor"));
    initMute(featureManager.getState("mute"));
    initTpa(featureManager.getState("tpa"));
    initInvsee(featureManager.getState("invsee"));
    initPosition(featureManager.getState("position"));
    initSign(featureManager.getState("sign"));
    initBan(featureManager.getState("ban"));
    initTrash(featureManager.getState("trash"));
    initPing(featureManager.getState("ping"));
    initDiscord(featureManager.getState("discord"));
    initVanish(featureManager.getState("vanish"));
    initFriends(featureManager.getState("friends"));
    initMail(featureManager.getState("mail"));
    initPerks(featureManager.getState("perks"));
    initRemoteCommands(featureManager.getState("remote"));
    initShop(featureManager.getState("shop"));
  }
  private void initCoins(boolean enabled){
    FeatureManager.registerCommand("auszahlen", new AuszahlenExecutor(), new EmptyTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new CoinRightclickListener(), this);
    }
  }
  private void initPay(boolean enabled){
    FeatureManager.registerCommand("pay", new PayExecutor(), new PlayerTabCompleter(), enabled);
    FeatureManager.registerCommand("payall", new PayAllExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initColorCodes(boolean enabled){
    FeatureManager.registerCommand("colorcodes", new ColorcodesExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initProxyTeleport(boolean enabled) {
    FeatureManager.registerCommand("citybuild", new CitybuildTpExecutor(), new EmptyTabCompleter(), enabled);
    FeatureManager.registerCommand("farming", new FarmingTpExecutor(), new EmptyTabCompleter(), enabled);
    FeatureManager.registerCommand("lobby", new LobbyTpExecutor(), new EmptyTabCompleter(), enabled);
    FeatureManager.registerCommand("arcadeattack", new ArcadeAttackTpExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initArcadeAttack(boolean enabled) {
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new ArcadeAttackEggListener(), this);
      ArcadeAttackEggListener.startEggParticleTask();
    }
  }
  private void initLobby(boolean enabled) {
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new LobbyProtectListener(), this);
      LobbyProtectListener.startVoidProtection();
    }
  }
  private void initSpawn(boolean enabled) {
    FeatureManager.registerCommand("spawn", new SpawnExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initServerStructure(boolean enabled) {
    FeatureManager.registerCommand("performaction", new PerformactionExecutor(), null, enabled);
    FeatureManager.registerCommand("testcom", new TestCom(), null, enabled);
    FeatureManager.registerCommand("sql", new SqlExecutor(), null, enabled);
    if (enabled) {
      if (featureManager.getState("proxy")) Bukkit.getPluginManager().registerEvents(new TabListManager(), this);
      Bukkit.getPluginManager().registerEvents(new CustomGuiHolder(), this);
      Bukkit.getPluginManager().registerEvents(new GuiManager(), this);
    }
  }
  private void initRank(boolean enabled) {
    FeatureManager.registerCommand("setrank", new SetRankExecutor(), new SetRankTabCompleter(), enabled);
    FeatureManager.registerCommand("getcustomitem", new GetCustomItemExecutor(), new GetCustomItemTabCompleter(), enabled);
    if (enabled) {
      PermissionManager.initialize();
      Bukkit.getPluginManager().registerEvents(new PlayerNameManager(), this);
      Bukkit.getPluginManager().registerEvents(new PermissionManager(), this);
    }
  }
  private void initNameColor(boolean enabled) {
    FeatureManager.registerCommand("namecolor", new NamecolorExecutor(), new EmptyTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new NamecolorCouponManager(), this);
      Bukkit.getPluginManager().registerEvents(new NamecolorGuiListener(), this);
    }
  }
  private void initMute(boolean enabled) {
    FeatureManager.registerCommand("mute", new MuteExecutor(), new MuteTabCompleter(), enabled);
    FeatureManager.registerCommand("unmute", new UnmuteExecutor(), new OfflinePlayerTabCompleter(), enabled);
    FeatureManager.registerCommand("tempmute", new TempmuteExecutor(), new TempmuteTabCompleter(), enabled);
    FeatureManager.registerCommand("muteinfo", new MuteinfoExecutor(), new OfflinePlayerTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new MuteManager(), this);
    }
  }
  private void initTpa(boolean enabled) {
    FeatureManager.registerCommand("tpa", new TpaExecutor(), new TpaTabCompleter(), enabled);
  }
  private void initInvsee(boolean enabled) {
    FeatureManager.registerCommand("invsee", new InvseeExecutor(), new InvseeTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new InvseeManager(), this);
    }
  }
  private void initPosition(boolean enabled){
    FeatureManager.registerCommand("position", new PositionExecutor(), new PositionTabCompleter(), enabled);
  }
  private void initSign(boolean enabled){
    FeatureManager.registerCommand("sign", new SignExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initSidebar(boolean enabled){
    FeatureManager.registerCommand("configsidebar", new ConfigSidebarExecutor(), new ConfigSidebarTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new SidebarManager(), this);
      Bukkit.getPluginManager().registerEvents(new ChatMessageManager(), this);
      SidebarManager.startUpdateCycle();
    }
  }
  private void initBan(boolean enabled) {
    FeatureManager.registerCommand("tempban", new TempBanExecutor(), new TempbanTabCompleter(), enabled);
  }
  private void initTrash(boolean enabled){
    FeatureManager.registerCommand("müll", new TrashExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initPing(boolean enabled){
    FeatureManager.registerCommand("ping", new PingExecutor(), new PlayerTabCompleter(), enabled);
  }
  private void initDiscord(boolean enabled){
    FeatureManager.registerCommand("discord", new DiscordExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initVanish(boolean enabled){
    FeatureManager.registerCommand("vanish", new VanishExecutor(), new EmptyTabCompleter(), enabled);
    FeatureManager.registerCommand("unvanish", new UnvanishExecutor(), new EmptyTabCompleter(), enabled);
  }
  private void initFriends(boolean enabled){
    FeatureManager.registerCommand("friends", new FriendsExecutor(), new FriendsTabCompleter(), enabled);
  }
  private void initMail(boolean enabled){
    FeatureManager.registerCommand("mail", new MailExecutor(), new MailTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new MailListener(), this);
    }
  }

  private void initPerks(boolean enabled) {
    FeatureManager.registerCommand("perks", new PerkExecutor(), new EmptyTabCompleter(), enabled);
    if (enabled) {
      Bukkit.getPluginManager().registerEvents(new PerkEffectListener(), this);
      Bukkit.getPluginManager().registerEvents(new PerkCouponManager(), this);
      Bukkit.getScheduler().runTaskTimer(this, () -> {
        for (Player player : Bukkit.getOnlinePlayers()) {
          PerkManager.updatePerks(player);
        }
      }, 0L, 20L * 10L);
    }
  }

  private void initRemoteCommands(boolean enabled) {
    FeatureManager.registerCommand("fastpoll", new FastPollingExecutor(), new EmptyTabCompleter(), enabled);
    if (enabled) {
      CommandPoller.start();
    }
  }

  private void initShop(boolean enabled) {
    if (enabled) {
      shopManager = new ShopManager();
      ShopManager.registerShelfRecipes();
      Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
      Bukkit.getPluginManager().registerEvents(new ShopConfigGuiListener(), this);
      Bukkit.getPluginManager().registerEvents(new ShopStorageGuiListener(), this);
    }
  }
}

