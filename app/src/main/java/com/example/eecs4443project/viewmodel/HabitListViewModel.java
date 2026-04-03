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
    private LiveData<List<Habit>> habits;

    //private final LiveData<List<Habit>> selectedHabits;

    private MutableLiveData<String> filterQuery = new MutableLiveData<>();

    public HabitListViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        // Set the filter query to be the empty string initially
        // So that the transformation.switchmap initially shows
        // the entire list of habits
        setFilterQuery("");


    }

    /*
    When the user enters text into the SearchView, the filtered
    list of habits will need to be shown. the Transformations.switchMap
    allows for dynamic LiveData creation that will return back the
    appropriate list to be seen in the RecyclerView. A habitQuery string
    is fed that upon an empty string will show all the habits,
    and the filtered habits only if not empty

    */
    public LiveData<List<Habit>> getFilteredHabits() {
        habits = Transformations.switchMap(filterQuery, habitQuery -> {
            if (habitQuery == null || habitQuery.isEmpty()) {
                return repo.getAllHabits();
            }
            else {
                return repo.getFilteredHabits(habitQuery);
            }
        });
        return habits;
    }
    //public LiveData<List<Habit>> getSelectedHabits() {return selectedHabits;}

    // should theoretically replace the "delete" method
    //public void unselectHabit(Habit habit) {
    //    repo.unselectHabit(habit);
    //}

    // The function sets the value of the filterQuery that will filter the habits
    public void setFilterQuery(String habitQuery) {
        filterQuery.setValue(habitQuery);
    }

    // If the habit is meant to be selected, its isSelected attribute is set to 1
    public void selectHabit(Habit habit) {
        repo.selectHabit(habit);
    }
}
