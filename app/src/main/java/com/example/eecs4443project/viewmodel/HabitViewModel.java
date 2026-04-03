package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.repository.HabitRepository;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {

    private final HabitRepository repo;
    private final LiveData<List<Habit>> allHabits;

    // Comparatively to the HabitListViewModel, this viewModel only shows the selected habits, not the whole list with filtering
    private final LiveData<List<Habit>> selectedHabits;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        // Both the selected habits and all the habits are fetched
        selectedHabits = repo.getSelectedHabits();
        allHabits = repo.getAllHabits();

    }

    public LiveData<List<Habit>> getAllHabits() {
        return repo.getAllHabits();
    }

    public LiveData<Habit> getHabit(int id) {
        return repo.getHabit(id);
    }

    public LiveData<List<Habit>> getSelectedHabits() {return selectedHabits;}


    // Not used, but kept for now
    public void selectHabit(Habit habit) {
        repo.selectHabit(habit);
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

    public void unselectHabit(Habit habit) {
        repo.unselectHabit(habit);
    }

    public void toggleStar(Habit habit) {
        int newVal = (habit.getStarred() == 1) ? 0 : 1;
        habit.setStarred(newVal);
        repo.updateStar(habit.getId(), newVal);
    }

    // TEMP: dummy data
    public void insertDummyHabits() {
        repo.insertDummyHabits();
    }
}