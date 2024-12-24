package ru.yandex.practicum.filmorate;

import org.postgresql.ds.PGSimpleDataSource;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/filmsdb";
        String user = "dbuser";
        String password = "12345";

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL(url);
        ds.setUser(user);
        ds.setPassword(password);
        try (Connection conn = ds.getConnection()) {
            // Делаем что-то с подключением
        } catch (SQLException e) {
            // Обрабатываем ошибки
        }
    }
}
