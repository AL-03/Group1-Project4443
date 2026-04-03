package com.example.eecs4443project.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.view.adapters.HabitDashboardAdapter;
import com.example.eecs4443project.view.adapters.HabitListAdapter;
import com.example.eecs4443project.viewmodel.HabitListViewModel;
import com.example.eecs4443project.viewmodel.HabitViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewHabitsActivity extends AppCompatActivity implements HabitListAdapter.onHabitSelectedListener{

    //private HabitViewModel habitViewModel;

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
        adapter = new HabitListAdapter(habit -> {habitListVM.selectHabit(habit);});
        habitListRV.setAdapter(adapter);


        // Set up ViewModel
        habitListVM = new ViewModelProvider(this).get(HabitListViewModel.class);

        // Observe LiveData from Room
        //habitViewModel.getFilteredHabits().observe(this, adapter::setHabits);

        //habitViewModel.getSelectedHabitIds().observe(this, adapter::setSelectedHabits);

        customHabit = findViewById(R.id.createCustomBtn);
        customHabit.setOnClickListener(v -> {
            Intent customIntent = new Intent(NewHabitsActivity.this, CustomHabitActivity.class);
            startActivity(customIntent);
        });


        // Set up searchView
        habitSearch = findViewById(R.id.searchHabits);
        habitSearch.clearFocus();
        habitSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                habitListVM.setFilterQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String habitText) {
                habitListVM.setFilterQuery(habitText);
                return true;
            }
        });


    }

    @Override
    public void onHabitSelected(Habit habit) {

    }
}

