package com.example.eecs4443project.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eecs4443project.data.entity.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    // COUNT will return 1 if credentials match, but 0 if they don't match
    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND password = :password")
    LiveData<Integer> checkLogin(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    LiveData<User> getUser(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User getUser(String username, String password);
}
