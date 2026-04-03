package com.example.eecs4443project.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.entity.SelectedHabit;

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

    // Delete habit - to be removed such that only the selecteditems can be deleted
    @Delete
    void deleteHabit(Habit habit);

    // Get all habits
    @Query("SELECT * FROM habits")
    LiveData<List<Habit>> getAllHabits();

    // Get a specific habit by id
    @Query("SELECT * FROM habits WHERE id = :id")
    Habit getHabit(int id);



    // Get a specific habit by its name/title for NewHabitsActivity searchView
    //@Query("SELECT * FROM habits LEFT JOIN selected_habits ON habits.id = selected_habits.habitId WHERE habits.title LIKE '%' || :filterQuery || '%' ORDER BY habits.title ASC")
    //LiveData<List<Habit>> getFilteredHabits(String filterQuery);


    // Get the selectedList of Habits when they have been added by the user
    @Query("SELECT * FROM habits WHERE isSelected = 1 ORDER BY habits.title ASC")
    LiveData<List<Habit>> getSelectedHabits();

    // Get the filteredList of Habits when they are filtered
    @Query("SELECT * FROM habits WHERE habits.title LIKE '%' || :habitQuery || '%' ORDER BY habits.title ASC")
    LiveData<List<Habit>> getFilteredHabits(String habitQuery);


    @Query("UPDATE habits SET starred = :newVal WHERE id = :id")
    void updateStar(int id, int newVal);

    @Query("UPDATE habits SET isSelected = :newBool WHERE id = :id")
    void updateIsSelected(int id, boolean newBool);

    @Query("SELECT COUNT(*) FROM habits")
    int getCount();
}

