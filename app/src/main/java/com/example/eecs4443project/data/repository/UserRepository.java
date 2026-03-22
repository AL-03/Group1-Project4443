package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.UserDao;
import com.example.eecs4443project.data.entity.User;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
    }

    public LiveData<Integer> login(String username, String password) {
        return userDao.checkLogin(username, password);
    }

    public void register(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.insertUser(user));
    }
}
