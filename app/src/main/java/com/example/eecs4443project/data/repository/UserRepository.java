package com.example.eecs4443project.data.repository;

import android.app.Application;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.UserDao;
import com.example.eecs4443project.data.entity.User;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
    }

    public void register(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.insertUser(user));
    }

    public User getUser(String username, String password) {
        return userDao.getUser(username, password);
    }
}