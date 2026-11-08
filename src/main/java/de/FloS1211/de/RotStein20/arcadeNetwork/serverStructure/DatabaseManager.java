package de.FloS1211.de.RotStein20.arcadeNetwork.serverStructure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import de.FloS1211.de.RotStein20.arcadeNetwork.perks.Perk;
import de.FloS1211.de.RotStein20.arcadeNetwork.perks.PerkManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mariadb.jdbc.Driver;

import java.sql.*;
import java.util.List;

public class DatabaseManager implements Listener {
  public DatabaseManager() {

  }

  //private Connection connection;

  FileConfiguration config =
      ArcadeNetwork.getInstance().getConfig();

  String host = config.getString("database.host");
  int port = config.getInt("database.port");
  String database = config.getString("database.database");
  String username = config.getString("database.username");
  String password = config.getString("database.password");

  private HikariDataSource dataSource;


  public void connect() throws SQLException {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);
    config.setUsername(username);
    config.setPassword(password);
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setConnectionTimeout(10000);
    config.setMaxLifetime(1800000);
    config.setKeepaliveTime(120000);
    config.setPoolName("ArcadeNetwork-Database");
    dataSource = new HikariDataSource(config);
    /*
    if (connection != null && !connection.isClosed()) {
      return;
    }

    String url = "jdbc:mariadb://" + host + ":" + port + "/" + database;
    DriverManager.registerDriver(new Driver());

    connection = DriverManager.getConnection(url, username, password);
    Bukkit.getLogger().info("MariaDB connection established.");
    */
  }

  public Connection getConnection() throws SQLException{
    //return connection;
    return dataSource.getConnection();
  }

  public void disconnect() {
    if (dataSource != null) {
      dataSource.close();
    }
    System.out.println("MariaDB connection closed.");
    /*if (connection != null) {
      try {
        connection.close();
        System.out.println("MariaDB connection closed.");
      } catch (SQLException e) {
        System.err.println("Error closing MariaDB connection: " + e.getMessage());
      }
    }*/
  }

  public void createTables() throws SQLException {
    try (Statement statement = getConnection().createStatement()) {
      statement.execute("""
          CREATE TABLE IF NOT EXISTS player_data (
            uuid VARCHAR(36) PRIMARY KEY,
            coins INTEGER NOT NULL DEFAULT 0,
            rank VARCHAR(20) NOT NULL DEFAULT 'default',
            extra_rank VARCHAR(20) NOT NULL DEFAULT 'none',
            namecolor VARCHAR(20) NOT NULL DEFAULT 'default',
            unlocked_namecolors TEXT NOT NULL DEFAULT '',
            is_muted BOOLEAN NOT NULL DEFAULT FALSE,
            mute_data TEXT NOT NULL DEFAULT '',
            vanished BOOLEAN NOT NULL DEFAULT FALSE,
            clan VARCHAR(6) NOT NULL DEFAULT ''
          );
          """);
      statement.execute("""
          CREATE TABLE IF NOT EXISTS positions (
            uuid VARCHAR(36),
            x REAL NOT NULL DEFAULT 0,
            y REAL NOT NULL DEFAULT 0,
            z REAL NOT NULL DEFAULT 0,
            dim VARCHAR(20) NOT NULL DEFAULT "world",
            name TEXT NOT NULL DEFAULT "pos",
            server TEXT
          );
          """);
      statement.execute("""
          CREATE TABLE IF NOT EXISTS friends (
            uuid VARCHAR(36) NOT NULL,
            targetUuid VARCHAR(36) NOT NULL,
            day INTEGER NOT NULL,
            approved BOOLEAN NOT NULL DEFAULT FALSE
          );
          """);
      statement.execute("""
          CREATE TABLE IF NOT EXISTS mail (
            id INTEGER PRIMARY KEY AUTO_INCREMENT,
            uuidSender VARCHAR(36) NOT NULL,
            uuidTarget VARCHAR(36) NOT NULL,
            unixDay INTEGER NOT NULL,
            isRead BOOLEAN NOT NULL DEFAULT FALSE,
            message TEXT NOT NULL DEFAULT "",
            caption TEXT NOT NULL DEFAULT "Neue Nachricht",
            deletedBySender BOOLEAN NOT NULL DEFAULT FALSE,
            deletedByTarget BOOLEAN NOT NULL DEFAULT FALSE,
            favoritedBySender BOOLEAN NOT NULL DEFAULT FALSE,
            favoritedByTarget BOOLEAN NOT NULL DEFAULT FALSE
          );
          """);
      StringBuilder createPerksTableSQL = new StringBuilder("""
          CREATE TABLE IF NOT EXISTS perks (
            uuid VARCHAR(36) PRIMARY KEY
          """);
      for (Perk perk : PerkManager.perks.values()) {
        createPerksTableSQL.append(",\n").append(perk.perkId()).append(" INTEGER NOT NULL DEFAULT 0");
      }
      createPerksTableSQL.append("\n);");
      statement.execute(createPerksTableSQL.toString());
      statement.execute("""
          CREATE TABLE IF NOT EXISTS clans (
            name VARCHAR(6) PRIMARY KEY,
            owner VARCHAR(36) NOT NULL,
            color TINYINT UNSIGNED NOT NULL
          );
          """);
    }
  }

  public static void executeSQL(String sql, List<Object> args) {
    try (
        Connection connection = ArcadeNetwork.getInstance().getDatabaseManager().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      for (int i = 0; i < args.size(); i++) {
        preparedStatement.setObject(i + 1, args.get(i));
      }
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static SQLTable getTable(String tableName, String condition, List<Object> args) {
    return new SQLTable(tableName,condition,args);
  }

  @EventHandler
  public void onJoin (PlayerJoinEvent event) {
    if (getTable("player_data", "uuid = ?", List.of(event.getPlayer().getUniqueId().toString())).isEmpty()) {
      executeSQL("INSERT INTO player_data (uuid) VALUES (?)", List.of(event.getPlayer().getUniqueId().toString()));
    }
  }
}
