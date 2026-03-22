package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.repository.HabitRepository;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {

    private final HabitRepository repo;
    private final LiveData<List<Habit>> allHabits;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        allHabits = repo.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
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

    public void toggleStar(Habit habit) {
        int newVal = habit.getStarred() == 1 ? 0 : 1;
        habit.setStarred(newVal);
        repo.update(habit);
    }

    // TEMP: dummy data
    public void insertDummyHabits() {
        repo.insertDummyHabits();
    }
}