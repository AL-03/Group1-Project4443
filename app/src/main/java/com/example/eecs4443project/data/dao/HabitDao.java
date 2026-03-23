package com.example.eecs4443project.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eecs4443project.data.entity.Habit;

import java.util.List;

// Defines SQLite commands
@Dao
public interface HabitDao {
    // Create new habit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHabit(Habit habit);

    // Update existing habit
    @Update
    void updateHabit(Habit habit);

    // Delete habit
    @Delete
    void deleteHabit(Habit habit);

    // Get all habits
    @Query("SELECT * FROM habits")
    LiveData<List<Habit>> getAllHabits();

    // Get a specific habit
    @Query("SELECT * FROM habits WHERE id = :id")
    Habit getHabit(int id);

    @Query("UPDATE habits SET starred = :newVal WHERE id = :id")
    void updateStar(int id, int newVal);

    @Query("SELECT COUNT(*) FROM habits")
    int getCount();
}
