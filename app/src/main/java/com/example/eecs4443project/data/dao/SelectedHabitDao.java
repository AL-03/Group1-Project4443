package com.example.eecs4443project.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.entity.SelectedHabit;

import java.util.List;

@Dao
public interface SelectedHabitDao {

    // Insert selected habits in a singleton-like manner
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSelectedHabit(SelectedHabit selectedHabit);

    // When the user wishes to remove their selected habit from their dashboard, they will be removing the instance
    // seen from the selected_habits table
    @Query("DELETE FROM selected_habits WHERE habitId = :habitId")
    void unselectHabit(int habitId);

    // View all of the selected habits - for the main habit dashboard
    @Query("SELECT * FROM habits INNER JOIN selected_habits ON habits.id = selected_habits.habitId ORDER BY habits.title ASC")
    LiveData<List<Habit>> getSelectedHabits();

    // Fetch the IDs only
    @Query("SELECT habitId FROM selected_habits")
    LiveData<List<Integer>> getSelectedHabitIds();
}
