package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.view.adapters.ReminderAdapter;
import com.example.eecs4443project.viewmodel.ReminderViewModel;
import com.example.eecs4443project.view.adapters.HabitDashboardAdapter;
import com.example.eecs4443project.viewmodel.HabitViewModel;
import com.example.eecs4443project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {
    private HabitViewModel habitViewModel;
    private ReminderViewModel reminderViewModel;
    private HabitDashboardAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "User";

        TextView greeting = findViewById(R.id.greetingText);
        TextView stats = findViewById(R.id.statsText);

        greeting.setText(getGreeting(username));

        // HABITS
        RecyclerView habitsRecycler = findViewById(R.id.habitsRecycler);
        habitsRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyHabits = findViewById(R.id.emptyHabitsText);

        habitAdapter = new HabitDashboardAdapter(this, new HabitDashboardAdapter.HabitClickListener() {
            @Override public void onHabitClicked(Habit h) {}
            @Override public void onHabitLongPressed(Habit h) {}
            @Override public void onStarToggled(Habit h) {}
        });
        habitsRecycler.setAdapter(habitAdapter);

        // Set up HabitViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Observe LiveData from Room + ANALYTICS
        habitViewModel.getAllHabits().observe(this, habits -> {
            habitAdapter.setHabits(habits);

            if (habits.isEmpty()) {
                emptyHabits.setVisibility(View.VISIBLE);
            } else {
                emptyHabits.setVisibility(View.GONE);
            }

            stats.setText("You have " + habits.size() + " favourite habits");
        });

        // REMINDERS
        RecyclerView reminderRecycler = findViewById(R.id.reminderRecycler);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyReminders = findViewById(R.id.emptyRemindersText);

        ReminderAdapter reminderAdapter = new ReminderAdapter();
        reminderRecycler.setAdapter(reminderAdapter);

        // Set up ReminderViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        // Observe LiveData from Room
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            reminderAdapter.setReminders(reminders);

            if (reminders.isEmpty()) {
                emptyReminders.setVisibility(View.VISIBLE);
            } else {
                emptyReminders.setVisibility(View.GONE);
            }
        });

        // TEMP: Dummy habits and reminders
        //remove
        habitViewModel.insertDummyHabits();
        reminderViewModel.insertDummyReminders();

        // TODO: Make into a fragment (to avoid repeats)
        // NAV BAR
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_dashboard);

        nav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_dashboard) {
                return true; // already on the dashboard page
            }

            if (item.getItemId() == R.id.nav_habits) {
                startActivity(new Intent(this, HabitsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (item.getItemId() == R.id.nav_journal) {
                startActivity(new Intent(this, JournalActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (item.getItemId() == R.id.nav_account) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }

    private String getGreeting(String username) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour < 12) {
            return "☀\uFE0F Good Morning, " + username;
        } else if (hour < 18) {
            return "⛅ Good Afternoon, " + username;
        } else {
            return "\uD83C\uDF19 Good Evening, " + username;
        }

    }
}