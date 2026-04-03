package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.view.adapters.HabitListAdapter;
import com.example.eecs4443project.viewmodel.HabitListViewModel;

public class NewHabitsActivity extends AppCompatActivity implements HabitListAdapter.onHabitSelectedListener{


    private HabitListViewModel habitListVM;
    private HabitListAdapter adapter;
    private RecyclerView habitListRV;
    private SearchView habitSearch;

    Button customHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_list);


        // Set up RecyclerView
        habitListRV = findViewById(R.id.habitList);
        habitListRV.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView adapter
        adapter = new HabitListAdapter(habit -> {
            // The adapter first makes sure the habit is selected, then performs the onClick event of the habit
            habitListVM.selectHabit(habit);
            onHabitSelected(habit);
        });

        // Link the recyclerView adapter to the defined adapter
        habitListRV.setAdapter(adapter);


        // Set up ViewModel
        habitListVM = new ViewModelProvider(this).get(HabitListViewModel.class);

        // Observe LiveData from Room
        // This only collects the filtered list of habits. By default, the query is an empty string, which will show all habits
        // Filtered habits will be sorted in alphabetical order with priority given to ones that have not been selected yet
        habitListVM.getFilteredHabits().observe(this, adapter::setHabits);


        // if the user wants to create a customHabit, they will open the activity
        // that will create the custom habit and immediately add it to both lists
        // by way of
        //  -> setting the isSelected attribute of the new habit to true (selectedHabits can see it)
        //  -> inserting the habit into the database (filteredHabits can see it)
        customHabit = findViewById(R.id.createCustomBtn);
        customHabit.setOnClickListener(v -> {
            Intent customIntent = new Intent(NewHabitsActivity.this, CustomHabitActivity.class);
            startActivity(customIntent);
        });


        // Set up searchView
        habitSearch = findViewById(R.id.searchHabits);

        // initially, the searchView does not have a cursor inside it
        habitSearch.clearFocus();

        // whether the user submits the text they are quering, or they just change
        // the text, the textListener will dynamically allow for the filtered list
        // to be displayed rather than the initial full list of habits
        habitSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String habitQuery) {
                habitListVM.setFilterQuery(habitQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String habitQuery) {
                habitListVM.setFilterQuery(habitQuery);
                return true;
            }
        });


    }

    // If a habit is selected among the list, a popup will allow the user
    // to add the habit, which should really only update the isSelected
    // flag to true
    @Override
    public void onHabitSelected(Habit habit) {
        Intent intent = new Intent(this, HabitAddActivity.class);

        intent.putExtra("habit_id", habit.getId());
        intent.putExtra("title", habit.getTitle());
        intent.putExtra("desc", habit.getDescription());
        intent.putExtra("progress", habit.getProgress());
        intent.putExtra("starred", habit.getStarred());
        intent.putExtra("isSelected", habit.getIsSelected());

        startActivity(intent);
    }
}

