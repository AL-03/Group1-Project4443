package com.example.eecs4443project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HabitsActivity extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Habit> habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        db = new DatabaseHelper(this);

        RecyclerView recycler = findViewById(R.id.habitsRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        habits = new ArrayList<>();

        // Dummy habits
        //remove
        habits.add(new Habit(1, "Drink Water", "Stay hydrated", 1));
        habits.add(new Habit(2, "Workout", "Exercise daily", 0));
        habits.add(new Habit(3, "Read Book", "Read 20 pages", 1));
        habits.add(new Habit(4, "Meditate", "10 minutes daily", 0));
        habits.add(new Habit(5, "Study Algorithms", "Practice CLRS problems", 1));

        //db logic, uncomment add habits page works
        /*
        Cursor cursor = db.getReadableDatabase()
                .rawQuery("SELECT * FROM habits ORDER BY starred DESC", null);

        while (cursor.moveToNext()) {
            habits.add(new Habit(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)
            ));

        }
        */

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

        recycler.setAdapter(new HabitDashboardAdapter(this, habits));
    }
}