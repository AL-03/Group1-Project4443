package com.example.eecs4443project.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eecs4443project.data.entity.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    LiveData<User> getUser(String username, String password);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<User> getUser(int id);

    @Query("DELETE FROM users WHERE username = :username")
    void delete(String username);
    User getUser(String username, String password);
}