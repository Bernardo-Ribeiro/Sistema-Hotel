package com.hotel.gerenciador;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Dotenv dotenv = Dotenv.configure().load();

    private static final String DB_HOST = "localhost";
    private static final String DB_NAME = "sistema_hotel";
    private static final String DB_PORT = dotenv.get("DB_PORT");
    private static final String DB_USER = dotenv.get("MYSQL_USER");
    private static final String DB_PASSWORD = dotenv.get("MYSQL_PASSWORD");

    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }
}