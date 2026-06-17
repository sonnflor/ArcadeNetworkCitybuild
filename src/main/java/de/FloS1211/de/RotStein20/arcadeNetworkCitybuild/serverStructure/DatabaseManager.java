package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseManager {
  public DatabaseManager() {

  }

  private Connection connection;

  FileConfiguration config =
      ArcadeNetworkCitybuild.getInstance().getConfig();

  String host = config.getString("database.host");
  int port = config.getInt("database.port");
  String database = config.getString("database.database");
  String username = config.getString("database.username");
  String password = config.getString("database.password");

  public void connect() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      return;
    }

    String url = "jdbc:mariadb://" + host + ":" + port + "/" + database;
    DriverManager.registerDriver(new Driver());

    connection = DriverManager.getConnection(url, username, password);
    Bukkit.getLogger().info("MariaDB connection established.");
  }

  public Connection getConnection() {
    return connection;
  }

  public void disconnect() {
    if (connection != null) {
      try {
        connection.close();
        System.out.println("MariaDB connection closed.");
      } catch (SQLException e) {
        System.err.println("Error closing MariaDB connection: " + e.getMessage());
      }
    }
  }

  public void createTables() throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute("""
          CREATE TABLE IF NOT EXISTS player_data (
            uuid VARCHAR(36) PRIMARY KEY,
            coins INTEGER NOT NULL DEFAULT 0,
            rank VARCHAR(20) NOT NULL DEFAULT 'default',
            extra_rank VARCHAR(20) NOT NULL DEFAULT 'none',
            namecolor VARCHAR(20) NOT NULL DEFAULT 'default',
            unlocked_namecolors TEXT NOT NULL DEFAULT '',
            is_muted BOOLEAN NOT NULL DEFAULT FALSE,
            mute_data TEXT NOT NULL DEFAULT ''
          );
          """);
    }
  }

  public static void executeSQL(String sql, List<Object> args) {
    try (var preparedStatement = ArcadeNetworkCitybuild.instance.getDatabaseManager().connection.prepareStatement(sql)) {
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
}
