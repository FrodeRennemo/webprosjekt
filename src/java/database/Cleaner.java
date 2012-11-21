package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Cleaner {
  public static void closeResSet(ResultSet res) {
    try {
      if (res != null) {
        res.close();
      }
    } catch (SQLException e) {
      writeMessage(e, "closeResSet()");
    }
  }

  public static void closeSentence(Statement stm) {
    try {
      if (stm != null) {
        stm.close();
      }
    } catch (SQLException e) {
      writeMessage(e, "closeSentence()");
    }
  }

  public static void closeConnection(Connection connection) {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      writeMessage(e, "lukkconnection()");
    }
  }

  public static void rollback(Connection connection) {
    try {
      if (connection != null && !connection.getAutoCommit()) {
        connection.rollback();
      }
    } catch (SQLException e) {
      writeMessage(e, "rollback()");
    }
  }

  public static void setAutoCommit(Connection connection) {
    try {
      if (connection != null && !connection.getAutoCommit()) {
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      writeMessage(e, "settAutoCommit()");
    }
  }

  public static void writeMessage(Exception e, String message) {
    System.err.println("*** Error occured: " + message + ". ***");
    e.printStackTrace(System.err);
  }
}
