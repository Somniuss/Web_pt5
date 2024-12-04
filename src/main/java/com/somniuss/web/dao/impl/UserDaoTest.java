package com.somniuss.web.dao.impl;

import com.somniuss.web.bean.User;
import com.somniuss.web.dao.DaoException;
import com.somniuss.web.dao.impl.SQLUserDao;

public class UserDaoTest {
    public static void main(String[] args) {
        SQLUserDao userDao = new SQLUserDao();

        // Данные для теста
        String name = "Test User";
        String email = "testuser@example.com";
        String password = "password123";

        try {
            // Попытка регистрации нового пользователя
            User registeredUser = userDao.registration(name, email, password);
            System.out.println("Пользователь успешно зарегистрирован: " + registeredUser);

            // Попробуем получить пользователя из базы данных для проверки
            User retrievedUser = userDao.authorization(email, password);
            if (retrievedUser != null) {
                System.out.println("Пользователь успешно найден: " + retrievedUser);
            } else {
                System.out.println("Пользователь не найден в базе данных.");
            }
        } catch (DaoException e) {
            System.err.println("Ошибка при регистрации пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
