package com.example.eecs4443project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Habit> habits;
    ArrayList<Reminder> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = new DatabaseHelper(this);

        String username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "User";

        TextView greeting = findViewById(R.id.greetingText);
        TextView stats = findViewById(R.id.statsText);

        greeting.setText(getGreeting(username));

        // HABITS
        RecyclerView habitsRecycler = findViewById(R.id.habitsRecycler);
        habitsRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyHabits = findViewById(R.id.emptyHabitsText);

        habits = new ArrayList<>();

        Cursor cursor = db.getReadableDatabase()
                .rawQuery("SELECT * FROM habits WHERE starred = 1", null);

        while (cursor.moveToNext()) {
            habits.add(new Habit(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)
            ));
        }

        if (habits.size() == 0) emptyHabits.setVisibility(View.VISIBLE);

        habitsRecycler.setAdapter(new HabitDashboardAdapter(this, habits));

        // ANALYTICS
        stats.setText("You have " + habits.size() + " favourite habits");

        // REMINDERS
        RecyclerView reminderRecycler = findViewById(R.id.reminderRecycler);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyReminders = findViewById(R.id.emptyRemindersText);

        reminders = new ArrayList<>();

        // Dummy Data
        reminders.add(new Reminder(1, "Submit Assignment", "2026-03-22", "10:00"));
        reminders.add(new Reminder(2, "Doctor Appointment", "2026-03-23", "14:30"));
        reminders.add(new Reminder(3, "Team Meeting", "2026-03-24", "09:00"));

        if (reminders.size() == 0) emptyReminders.setVisibility(View.VISIBLE);

        reminderRecycler.setAdapter(new ReminderAdapter(reminders));


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