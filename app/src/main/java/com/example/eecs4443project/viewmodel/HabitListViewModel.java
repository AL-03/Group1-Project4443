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

public class HabitListViewModel extends AndroidViewModel {

    private final HabitRepository repo;
    //private final LiveData<List<Habit>> allHabits;

    //private final LiveData<List<Habit>> selectedHabits;

    private MutableLiveData<String> filterQuery = new MutableLiveData<>();

    public HabitListViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        //selectedHabits = repo.getSelectedHabits();


    }

    public LiveData<List<Habit>> getFilteredHabits() {
        return Transformations.switchMap(filterQuery, habitQuery -> {
            if (habitQuery == null || habitQuery.isEmpty()) {
                return repo.getAllHabits();
            }
            else {
                return repo.getFilteredHabits(habitQuery);
            }
        });
    }
    //public LiveData<List<Habit>> getSelectedHabits() {return selectedHabits;}

    // should theoretically replace the "delete" method
    //public void unselectHabit(Habit habit) {
    //    repo.unselectHabit(habit);
    //}

    public void setFilterQuery(String habitQuery) {
        filterQuery.setValue(habitQuery);
    }

    public void selectHabit(Habit habit) {
        repo.selectHabit(habit);
    }
}
