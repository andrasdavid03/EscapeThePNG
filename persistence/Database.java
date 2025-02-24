package persistence;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Database {
    private final int maxTimes;
    private final PreparedStatement insertStatement;
    private final PreparedStatement deleteStatement;
    private  Connection connection;
    private EmbeddedPostgres embeddedPostgres;

    public Database(int maxTimes) {
        this.maxTimes = maxTimes;

        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "admin");
        connectionProps.put("serverTimezone", "UTC");

        try {
            Path dataDirectory = Paths.get("game-data");
            if (!Files.exists(dataDirectory)) {
                Files.createDirectory(dataDirectory);
            }

            embeddedPostgres = EmbeddedPostgres.builder().setDataDirectory(dataDirectory).setCleanDataDirectory(false).start();
            String databaseURL = embeddedPostgres.getJdbcUrl("postgres", "postgres");

            connection = DriverManager.getConnection(databaseURL, connectionProps);
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE highscores");
            } catch (SQLException e) {
                if (!e.getSQLState().equals("42P04")) {
                    throw e;
                }
            }

            // Create table
            databaseURL = embeddedPostgres.getJdbcUrl("postgres", "highscores");
            connection = DriverManager.getConnection(databaseURL, connectionProps);

            String createTableSql = "CREATE TABLE IF NOT EXISTS highscores (level INT, name VARCHAR(255), time INT);";
            try (Statement tableStmt = connection.createStatement()) {
                tableStmt.execute(createTableSql);
            }

            String insertQuery = "INSERT INTO HIGHSCORES (LEVEL, NAME, TIME) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);
            String deleteQuery = "DELETE FROM HIGHSCORES WHERE TIME=? AND LEVEL=?";
            deleteStatement = connection.prepareStatement(deleteQuery);

        } catch (SQLException e) {
            System.err.println("Can't connect to database");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<HighScore> getHighScoresSorted(int level) {
        String query = "SELECT * FROM HIGHSCORES WHERE level = ? ORDER BY time ASC";
        ArrayList<HighScore> highScores = new ArrayList<>();

       try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
           preparedStatement.setInt(1, level);

           try (ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    String name = results.getString("NAME");
                    int time = results.getInt("TIME");
                    highScores.add(new HighScore(name, time));
                }
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

        return highScores;
    }

    public void putHighScore(int level, String name, int time) {
        ArrayList<HighScore> highScores = getHighScoresSorted(level);
        if (highScores.size() < maxTimes) {
            insertTime(level, name, time);
        } else {
            int biggestTime = highScores.getLast().getTime();
            if (biggestTime > time) {
                deleteTime(level, biggestTime);
                insertTime(level, name, time);
            }
        }
    }

    private void insertTime(int level, String name, int time) {
        try {
            insertStatement.setInt(1, level);
            insertStatement.setString(2, name);
            insertStatement.setInt(3, time);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteTime(int level, int time) {
        try {
            deleteStatement.setInt(1, time);
            deleteStatement.setInt(2, level);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopEmbeddedPostgres() {
        try {
            if (embeddedPostgres != null) {
                embeddedPostgres.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
