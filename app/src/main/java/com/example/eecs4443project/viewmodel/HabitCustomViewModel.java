package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.repository.HabitRepository;

import java.util.List;

public class HabitCustomViewModel extends AndroidViewModel {

    private final HabitRepository repo;
    private final LiveData<List<Habit>> allHabits;

    // Returns all habits. Is kept separate for maintainability and differentiation from teh standard habitViewModel
    public HabitCustomViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        allHabits = repo.getAllHabits();

    }

    // Unused but kept for now
    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    // Unused but kept for now
    public void selectHabit(Habit habit) {
        repo.selectHabit(habit);
    }

    public void unselectHabit(Habit habit) {
        repo.unselectHabit(habit);
    }


    public void insert(Habit habit) {
        repo.insert(habit);
    }

    public void update(Habit habit) {
        repo.update(habit);
    }

    public void delete(Habit habit) {
        repo.delete(habit);
    }

}
