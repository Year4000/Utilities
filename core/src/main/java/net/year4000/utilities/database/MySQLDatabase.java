/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.database;

import java.sql.*;

@SuppressWarnings("unused")
public class MySQLDatabase {
    private static MySQLDatabase inst = null;
    private static String database;
    private static String username;
    private static String password;
    private Connection connection;

    private MySQLDatabase(String database, String username, String password) {
        try {
            connection = DriverManager.getConnection(
                database,
                username,
                password
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the instance of the database.
     *
     * @return Database
     */
    public static MySQLDatabase inst() {
        if (inst == null) {
            inst = new MySQLDatabase(database, username, password);
        }

        return inst;
    }

    /**
     * Set the connection details of the instance.
     *
     * @param database Database url to connect to.
     * @param username The username for the database.
     * @param password The password to use.
     */
    public static void setConnection(String database, String username, String password) {
        MySQLDatabase.database = database;
        MySQLDatabase.username = username;
        MySQLDatabase.password = password;
    }

    /**
     * Create a uri to connect to a db.
     *
     * @param type     The type of connection.
     * @param hostname The hostname of the database.
     * @param database The database's database.
     * @return Url string
     */
    public static String makeUri(String type, String hostname, String database) {
        return String.format(
            "jdbc:%s://%s/%s?autoReconnect=true",
            type,
            hostname,
            database
        );
    }

    /**
     * Run a query and receive a result set
     *
     * @param sql The query to run.
     * @return ResultSet
     */
    public ResultSet silentExecuteQuery(String sql) {
        ResultSet results = null;
        try {
            Statement statement = connection.createStatement();
            results = statement.executeQuery(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Run a query and receive a result set
     *
     * @param sql The query to run.
     * @return ResultSet
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(sql);

        return results;
    }

    /**
     * Run a query on the database.
     *
     * @param sql The query to run.
     */
    public boolean silentQuery(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Run a query on the database.
     *
     * @param sql The query to run.
     */
    public void query(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
    }
}