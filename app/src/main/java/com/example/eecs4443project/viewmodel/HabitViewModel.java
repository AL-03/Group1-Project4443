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

    private final LiveData<List<Habit>> selectedHabits;

    //private MutableLiveData<String> filteredHabits = new MutableLiveData<>();

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        selectedHabits = repo.getSelectedHabits();
        allHabits = repo.getAllHabits();

        /*
        Transformations.switchMap(filteredHabits, habitQuery -> {
            if (habitQuery == null || habitQuery.isEmpty()) {
                return repo.getAllHabits();
            }
            else {
                return repo.getFilteredHabits(habitQuery);
            }
        });
        */
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public LiveData<List<Habit>> getSelectedHabits() {return selectedHabits;}

    // should theoretically replace the "delete" method
    //public void unselectHabit(Habit habit) {
    //    repo.unselectHabit(habit);
    //}


    //public void setHabitQuery(String habitQuery) {
    //    filteredHabits.setValue(habitQuery);
    //}


    /*
    public LiveData<List<Habit>> getFilteredHabits() {
        return Transformations.switchMap(filteredHabits, habitQuery -> repo.filterHabits(habitQuery != null ? habitQuery: ""));
    }
    */


    //public LiveData<List<Integer>> getSelectedHabitIds() {
    //    return repo.getSelectedHabitIds();
    //}

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