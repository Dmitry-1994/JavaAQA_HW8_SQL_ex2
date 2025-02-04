package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private SQLHelper() {
    }

    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getVerCode() {
        var dataSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, dataSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void clearDataBase() {
        try (var conn = getConnection()) {
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static void clearAuthCodes() {
        try (var conn = getConnection()) {
            runner.update(conn, "DELETE FROM auth_codes");
        }
    }
}