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

    public void register(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.insertUser(user));
    }

    public LiveData<User> getUser(String username, String password) {
        return userDao.getUser(username, password);
    }

    public void update(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.update(user));
    }

    public void delete(String username)
    {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.delete(username));
    }

    public LiveData<User> getUser(int id)
    {
        return userDao.getUser(id);
    }
}