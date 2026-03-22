package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.view.adapters.HabitDashboardAdapter;
import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.viewmodel.HabitViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HabitsActivity extends AppCompatActivity implements HabitDashboardAdapter.HabitClickListener {

    private HabitViewModel habitViewModel;
    private HabitDashboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        // Set up RecyclerView
        RecyclerView recycler = findViewById(R.id.habitsRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HabitDashboardAdapter(this, this);
        recycler.setAdapter(adapter);

        // Set up ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Observe LiveData from Room
        habitViewModel.getAllHabits().observe(this, habits -> {
            adapter.setHabits(habits);
        });

        // TEMP: Dummy habits
        //remove
        habitViewModel.insertDummyHabits();

        // NAV BAR
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_habits);

        nav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (item.getItemId() == R.id.nav_habits) {
                return true; // already on the habits page
            }

            if (item.getItemId() == R.id.nav_journal) {
                // placeholder
                //add intent when journal page is made
                Toast.makeText(this, "Journal page coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (item.getItemId() == R.id.nav_account) {
                // placeholder
                //add intent when account page is made
                Toast.makeText(this, "Account page coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    // Implement methods from HabitClickListener
    @Override
    public void onHabitClicked(Habit habit) {
        Intent intent = new Intent(this, HabitDetailActivity.class);
        intent.putExtra("habit_id", habit.getId());
        intent.putExtra("title", habit.getTitle());
        intent.putExtra("desc", habit.getDescription());
        startActivity(intent);
    }

    @Override
    public void onHabitLongPressed(Habit habit) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Habit")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (d, i) -> habitViewModel.delete(habit))
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onStarToggled(Habit habit) {
        habitViewModel.toggleStar(habit);
    }
}