package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eecs4443project.AddReminderDialog;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.entity.Reminder;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

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

        // Charts
        LineChart lineChart = findViewById(R.id.progressChart);
        PieChart pieChart = findViewById(R.id.pieChart);


        // Habits
        RecyclerView habitsRecycler = findViewById(R.id.habitsRecycler);
        habitsRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyHabits = findViewById(R.id.emptyHabitsText);

        habitAdapter = new HabitDashboardAdapter(this, new HabitDashboardAdapter.HabitClickListener() {
            @Override public void onHabitClicked(Habit h) {
                Intent intent = new Intent(DashboardActivity.this, HabitDetailActivity.class);
                intent.putExtra("habit_id", h.getId());
                intent.putExtra("title", h.getTitle());
                intent.putExtra("desc", h.getDescription());
                startActivity(intent);
            }

            @Override public void onHabitLongPressed(Habit h) {}

            @Override public void onStarToggled(Habit h) {
                habitViewModel.toggleStar(h);
            }
        });

        habitsRecycler.setAdapter(habitAdapter);

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        habitViewModel.getAllHabits().observe(this, habits -> {

            List<Habit> starredHabits = new ArrayList<>();

            for (Habit h : habits) {
                if (h.getStarred() == 1) {
                    starredHabits.add(h);
                }
            }

            habitAdapter.setHabits(starredHabits);

            stats.setText("You have " + starredHabits.size() + " favourite habits");

            emptyHabits.setVisibility(habits.isEmpty() ? View.VISIBLE : View.GONE);




            // line chart for all habits
            List<Entry> entries = new ArrayList<>();
            List<Entry> avgEntries = new ArrayList<>();

            float total = 0f;

            for (int i = 0; i < habits.size(); i++) {
                float progress = habits.get(i).getProgress();
                entries.add(new Entry(i, progress));
                total += progress;
            }

            float avg = habits.size() > 0 ? total / habits.size() : 0f;

            for (int i = 0; i < habits.size(); i++) {
                avgEntries.add(new Entry(i, avg));
            }

            LineDataSet progressSet = new LineDataSet(entries, "Progress");
            progressSet.setColor(Color.BLUE);
            progressSet.setLineWidth(3f);
            progressSet.setCircleRadius(4f);
            progressSet.setDrawFilled(true);

            LineDataSet avgSet = new LineDataSet(avgEntries, "Average");
            avgSet.setColor(Color.RED);
            avgSet.setDrawCircles(false);
            avgSet.setLineWidth(2f);

            LineData lineData = new LineData(progressSet, avgSet);

            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getAxisRight().setDrawGridLines(false);

            lineChart.clear();
            lineChart.setData(lineData);
            lineChart.invalidate();

            //pie chart for all habits
            int completed = 0;
            int inProgress = 0;

            for (Habit h : habits) {
                if (h.getProgress() >= 100) {
                    completed++;
                } else {
                    inProgress++;
                }
            }

            List<PieEntry> pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(completed, "Completed"));
            pieEntries.add(new PieEntry(inProgress, "In Progress"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Completion");


            pieDataSet.setColors(
                    Color.parseColor("#0077FF"),
                    Color.parseColor("#B8D9FF")
            );

            pieDataSet.setDrawValues(false);

            PieData pieData = new PieData(pieDataSet);

            pieChart.clear();
            pieChart.setData(pieData);

            pieChart.setDrawEntryLabels(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("");
            pieChart.setDrawCenterText(false);


            pieChart.invalidate();
        });

        //reminders
        RecyclerView reminderRecycler = findViewById(R.id.reminderRecycler);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyReminders = findViewById(R.id.emptyRemindersText);

        Button addBtn = findViewById(R.id.addReminderBtn);

        addBtn.setOnClickListener(v -> {
            AddReminderDialog.show(this, reminderViewModel);
        });

        ReminderAdapter reminderAdapter = new ReminderAdapter(new ReminderAdapter.OnReminderActionListener() {
            @Override
            public void onToggleComplete(Reminder reminder) {
                reminder.setCompleted(!reminder.isCompleted());
                reminderViewModel.update(reminder);
            }

            @Override
            public void onDelete(Reminder reminder) {
                reminderViewModel.delete(reminder);

            }
        });
        reminderRecycler.setAdapter(reminderAdapter);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        reminderViewModel.getAllReminders().observe(this, reminders -> {
            reminderAdapter.setReminders(reminders);

            emptyReminders.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);
        });

        //dummy data
        habitViewModel.insertDummyHabits();
        reminderViewModel.insertDummyReminders();

        //nav bar
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_dashboard);

        nav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_dashboard) return true;

            if (item.getItemId() == R.id.nav_habits) {
                startActivity(new Intent(this, HabitsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (item.getItemId() == R.id.nav_journal) {
                Toast.makeText(this, "Journal page coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (item.getItemId() == R.id.nav_account) {
                Toast.makeText(this, "Account page coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    private int calculateStreak(List<Habit> habits) {
        int streak = 0;

        for (Habit h : habits) {
            if (h.getProgress() > 0) {
                streak++;
            } else {
                streak = 0;
            }
        }

        return streak;
    }

    private String getGreeting(String username) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour < 12) {
            return "☀️ Good Morning, " + username;
        } else if (hour < 18) {
            return "⛅ Good Afternoon, " + username;
        } else {
            return "🌙 Good Evening, " + username;
        }
    }
}