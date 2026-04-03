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
    // If more than one of the same habits are attempted to be created, this is ignored
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertHabit(Habit habit);

    // Update existing habit
    @Update
    void updateHabit(Habit habit);

    // Delete habit - null after the update habit
    @Delete
    void deleteHabit(Habit habit);

    // Get all habits
    @Query("SELECT * FROM habits")
    LiveData<List<Habit>> getAllHabits();

    // Get a specific habit by id - not used
    @Query("SELECT * FROM habits WHERE id = :id")
    Habit getHabit(int id);

    // Get the selectedList of Habits when they have been added by the user.
    // Sort by which are favourited, and then alphabetical order
    @Query("SELECT * FROM habits WHERE habits.isSelected = 1 ORDER BY habits.starred DESC, LOWER(habits.title) ASC")
    LiveData<List<Habit>> getSelectedHabits();

    // Get the filteredList of Habits when they are filtered
    // Sort by ones that have not yet been selected, and then by alphabetical order
    @Query("SELECT * FROM habits WHERE habits.title LIKE '%' || :habitQuery || '%' ORDER BY isSelected ASC, LOWER(habits.title) ASC")
    LiveData<List<Habit>> getFilteredHabits(String habitQuery);

    // Update the star of the specified habit
    @Query("UPDATE habits SET starred = :newVal WHERE id = :id")
    void updateStar(int id, int newVal);

    // Get the count of all records in habits
    @Query("SELECT COUNT(*) FROM habits")
    int getCount();
}

