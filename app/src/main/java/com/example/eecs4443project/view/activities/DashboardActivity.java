package com.example.eecs4443project.view.activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

        createNotificationChannel();

        //gets the username and password from login to determine the user
        String username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "User";


        TextView greeting = findViewById(R.id.greetingText);
        TextView stats = findViewById(R.id.statsText);

        //Sets the greeting based on username
        greeting.setText(getGreeting(username));

        //Charts
        BarChart barChart = findViewById(R.id.progressChart);
        PieChart pieChart = findViewById(R.id.pieChart);


        //Habits
        //Create a recyclerview for the habits
        //If there are no habits, the "No Habits" textview is displayed
        RecyclerView habitsRecycler = findViewById(R.id.habitsRecycler);
        habitsRecycler.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyHabits = findViewById(R.id.emptyHabitsText);



        //Creates a new HabitDashboardAdapter
        habitAdapter = new HabitDashboardAdapter(this, new HabitDashboardAdapter.HabitClickListener() {
            //Once the user clicks on a habit
            @Override public void onHabitClicked(Habit h) {
                //Create an intent to switch to the detailview of the habit
                Intent intent = new Intent(DashboardActivity.this, HabitDetailActivity.class);
                //Send the habit information to the habit detail activity class
                intent.putExtra("habit_id", h.getId());
                intent.putExtra("title", h.getTitle());
                intent.putExtra("desc", h.getDescription());
                intent.putExtra("progress", h.getProgress());
                intent.putExtra("starred", h.getStarred());
                startActivity(intent);
            }

            //If the user long presses on the habit, it will give them a popup
            //message confirming their action intention
            @Override public void onHabitLongPressed(Habit h) {
                new androidx.appcompat.app.AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Delete Habit")
                        .setMessage("Are you sure?")
                        //If the user clicks yes, the habit will be deleted
                        .setPositiveButton("Yes", (d, i) -> habitViewModel.delete(h))
                        //Otherwise, no data is changed and the alert will close
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override public void onStarToggled(Habit h) {
                habitViewModel.toggleStar(h);
            }
        });


        habitsRecycler.setAdapter(habitAdapter);

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        //Gets the starred habits for the favourite habits card
        habitViewModel.getAllHabits().observe(this, habits -> {

            List<Habit> starredHabits = new ArrayList<>();

            for (Habit h : habits) {
                if (h.getStarred() == 1) {
                    starredHabits.add(h);
                }
            }

            habitAdapter.setHabits(starredHabits);

            //Uses the length of the starred habits ArrayList to generate a custom message
            stats.setText("You have " + starredHabits.size() + " favourite habits");

            //If the length of the ArrayList is not empty, do not show the empty habits textview
            emptyHabits.setVisibility(habits.isEmpty() ? View.VISIBLE : View.GONE);




            // Bar chart for all habits
            int[] bins = new int[10];

            for (Habit habit : habits) {
                int progress = habit.getProgress();
                int index = progress / 10;
                //to handle when a task is 100% completed
                if (index == 10) index = 9;
                bins[index]++;
            }

            //x axis labels
            String[] labels = {"0-10", "11-20", "21-30", "31-40", "41-50", "51-60", "61-70", "71-80", "81-90", "91-100"};

            List<BarEntry> entries = new ArrayList<>();

            //converts to chart entries
            for (int i = 0; i < 10; i++) {
                entries.add(new BarEntry(i, bins[i]));
            }
            //add to the dataset of the bar chart
            BarDataSet dataSet = new BarDataSet(entries, null);
            BarData data = new BarData(dataSet);

            dataSet.setColor(Color.parseColor("#5CA8FF"));

            barChart.clear();
            barChart.setData(data);
            barChart.setExtraBottomOffset(24f);

            barChart.getLegend().setEnabled(false);


            //set axis labels
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setLabelCount(10);
            xAxis.setAxisMinimum(-0.5f);
            xAxis.setAxisMaximum(9.5f);
            xAxis.setLabelRotationAngle(-45f);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setGranularity(1f);
            yAxis.setGranularityEnabled(true);
            yAxis.setAxisMinimum(0f);

            barChart.getAxisRight().setEnabled(false);

            barChart.setFitBars(true);
            barChart.getDescription().setEnabled(false);
            barChart.setDrawGridBackground(false);

            barChart.invalidate();



            //Pie chart for all habits completion
            int completed = 0;
            int inProgress = 0;

            //Gets the progress of each habit
            //If the progress is 100, add to completed
            //Else, add to in progress
            for (Habit h : habits) {
                if (h.getProgress() >= 100) {
                    completed++;
                } else {
                    inProgress++;
                }
            }

            //Create an arraylist for the piechart
            List<PieEntry> pieEntries = new ArrayList<>();
            //Adds the number of completed and in progress habits
            pieEntries.add(new PieEntry(completed, "Completed"));
            pieEntries.add(new PieEntry(inProgress, "In Progress"));

            //Gives the pie data set the values calculated above
            PieDataSet pieDataSet = new PieDataSet(pieEntries, null);

            //Pie chart formatting
            pieDataSet.setColors(
                    Color.parseColor("#0077FF"),
                    Color.parseColor("#B8D9FF")
            );

            pieDataSet.setDrawValues(false);

            PieData pieData = new PieData(pieDataSet);

            pieChart.clear();
            pieChart.setData(pieData);

            //creates a listener to determine what part of the chart
            // the user is pressing at that moment
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (e instanceof PieEntry) {
                        PieEntry entry = (PieEntry) e;

                        // show number in center of the chart
                        pieChart.setCenterText(String.valueOf((int) entry.getValue()));
                        pieChart.setDrawCenterText(true);
                    }
                }

                //when the user has nothing selected, remove the values
                //for a cleaner ui
                @Override
                public void onNothingSelected() {
                    pieChart.setCenterText("");
                    pieChart.setDrawCenterText(false);
                }
            });





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

        ImageButton addBtn = findViewById(R.id.addReminderBtn);

        //If the user clicks to add a reminder, they will get a popup to add one
        addBtn.setOnClickListener(v -> {
            AddReminderDialog.show(this, reminderViewModel);
        });

        ReminderAdapter reminderAdapter = new ReminderAdapter(new ReminderAdapter.OnReminderActionListener() {
            //If the user clicks the reminder checkbox, update the completion status
            @Override
            public void onReminderChecked(Reminder reminder) {
                reminder.setCompleted(!reminder.isCompleted());
                reminderViewModel.update(reminder);
            }

            //If the user long presses the reminder, an alert is given asking if they
            //want to delete the reminder
            @Override
            public void onReminderLongPressed(Reminder reminder) {
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Delete Reminder")
                        .setMessage("Are you sure?")
                        //If the user clicks yes, the reminder will be deleted
                        .setPositiveButton("Yes", (d, i) -> reminderViewModel.delete(reminder))
                        //Else, not data is changed and the alert is closed
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        reminderRecycler.setAdapter(reminderAdapter);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        reminderViewModel.getAllReminders().observe(this, reminders -> {
            reminderAdapter.setReminders(reminders);

            emptyReminders.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);
        });

        //Temporary dummy data
        habitViewModel.insertDummyHabits();
        reminderViewModel.insertDummyReminders();

        //Navigation Bar
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

    //do not currently use
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

    //Function to determine the greeting based on time of day
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

    //function to create a notification channel
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel =
                    new android.app.NotificationChannel(
                            "reminder_channel",
                            "Reminders",
                            android.app.NotificationManager.IMPORTANCE_HIGH
                    );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }



}