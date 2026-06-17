package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLTable {
  private final String name;
  private final ArrayList<String> tableColumns;
  private final ArrayList<ArrayList<Object>> tableValues;

  private Integer getColumnIndex(String columnName) {
    for (int i = 0; i < tableColumns.size(); i++) {
      String aktColumnName = tableColumns.get(i);
      if (aktColumnName.equals(columnName)) {
        return i;
      }
    }
    return -1;
  }

  public SQLTable(String tableName, String condition, List<Object> args) {
    this.name = tableName;
    ArrayList<String> preapredTableColums = new ArrayList<>();
    ArrayList<ArrayList<Object>> preparedTableValues = new ArrayList<>();
    String sql = "SELECT * FROM " + tableName;
    if (condition != null && !condition.isBlank() && !Objects.equals(condition, "true")) {
      sql += " WHERE " + condition;
    }
    try (PreparedStatement preparedStatement = ArcadeNetworkCitybuild.getInstance().getDatabaseManager().getConnection().prepareStatement(sql)) {
      if (!Objects.equals(condition, "true")) {
        assert condition != null;
        if (condition.contains("?")) {
          int i = 0;
          for (Object arg : args) {
            if (arg instanceof String) {
              preparedStatement.setString(i + 1, (String) arg);
            } else if (arg instanceof Integer) {
              preparedStatement.setInt(i + 1, (Integer) arg);
            } else if (arg instanceof Double) {
              preparedStatement.setDouble(i + 1, (Double) arg);
            }
            i++;
          }
        }
      }
      ResultSet resultSet = preparedStatement.executeQuery();
      for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
        preapredTableColums.add(resultSet.getMetaData().getColumnName(i + 1));
      }
      while (resultSet.next()) {
        ArrayList<Object> valueList = new ArrayList<>();
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
          valueList.add(resultSet.getObject(i + 1));
        }
        preparedTableValues.add(valueList);
      }
    } catch (SQLException e) {
      Bukkit.getLogger().severe(String.valueOf(e));
    }
    this.tableColumns = preapredTableColums;
    this.tableValues = preparedTableValues;
  }

  public String getTableName() {
    return name;
  }

  public String getStringValue(String columnName, int index) {
    int columnIndex = getColumnIndex(columnName);
    return (String) tableValues.get(index).get(columnIndex);
  }

  public Integer getIntValue(String columnName, int index) {
    int columnIndex = getColumnIndex(columnName);
    return (Integer) tableValues.get(index).get(columnIndex);
  }

  public Boolean getBooleanValue(String columnName, int index) {
    int columnIndex = getColumnIndex(columnName);
    Object value = tableValues.get(index).get(columnIndex);
    if (value instanceof Boolean b) {
      return b;
    }
    if (value instanceof Number n) {
      return n.intValue() != 0;
    }
    return false;
  }

  public double getDoubleValue(String columnName, int index) {
    int columnIndex = getColumnIndex(columnName);
    return (Double) tableValues.get(index).get(columnIndex);
  }

  public List<String> getStringColumn(String columnName) {
    List<String> result = new ArrayList<>();
    for (ArrayList<Object> tableValue : tableValues) {
      result.add((String) tableValue.get(getColumnIndex(columnName)));
    }
    return result;
  }

  public List<Integer> getIntColumn(String columnName) {
    List<Integer> result = new ArrayList<>();
    for (ArrayList<Object> tableValue : tableValues) {
      result.add((Integer) tableValue.get(getColumnIndex(columnName)));
    }
    return result;
  }

  public List<Double> getDoubleColumn(String columnName) {
    List<Double> result = new ArrayList<>();
    for (ArrayList<Object> tableValue : tableValues) {
      result.add((Double) tableValue.get(getColumnIndex(columnName)));
    }
    return result;
  }

  public Integer getColumnCount() {
    return tableColumns.size();
  }

  public Integer size() {
    return tableValues.size();
  }

  public boolean isEmpty() {
    return tableValues.isEmpty();
  }

  @Override
  public String toString() {
    return "SQLTable{name='" + name + "',columnNames=" + tableColumns.toString() + ", values=" + tableValues.toString();
  }
}
