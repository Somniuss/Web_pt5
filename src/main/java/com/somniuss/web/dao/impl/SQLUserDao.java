package com.somniuss.web.dao.impl;

import com.somniuss.web.bean.User;
import com.somniuss.web.dao.DaoException;
import com.somniuss.web.dao.UserDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDao implements UserDao {

    // Константы для подключения к базе данных
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/soundeffects_management_v1?serverTimezone=Europe/Minsk";
    private static final String JDBC_USER = "root"; // Укажите имя пользователя базы данных
    private static final String JDBC_PASSWORD = "root"; // Укажите пароль пользователя базы данных
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; // Драйвер для MySQL

    static {
        try {
            // Загружаем драйвер базы данных
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер базы данных не найден", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public User registration(String name, String email, String password) throws DaoException {
        User user = new User(name, email, password); // Пароль сохраняется в открытом виде

        try (Connection con = getConnection()) {
            String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword()); // Сохраняем пароль в открытом виде

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    return user; // Пользователь успешно добавлен
                } else {
                    throw new DaoException("Ошибка при добавлении пользователя");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Логирование ошибки при работе с базой данных
            throw new DaoException(e);
        }
    }

    @Override
    public User authorization(String email, String password) throws DaoException {
        try (Connection con = getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, email);
                ps.setString(2, password); // Сравниваем пароль в открытом виде

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    // Если пользователь найден, создаем объект User
                    return new User(rs.getString("name"), rs.getString("email"), rs.getString("password"));
                }
                return null; // Пользователь не найден
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Логирование ошибки при работе с базой данных
            throw new DaoException(e);
        }
    }
}
