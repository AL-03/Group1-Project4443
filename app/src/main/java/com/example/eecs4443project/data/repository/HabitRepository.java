package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.HabitDao;
import com.example.eecs4443project.data.entity.Habit;

import java.util.List;
import java.util.concurrent.Executors;

public class HabitRepository {
    private HabitDao habitDao;
    private LiveData<List<Habit>> allHabits;

    public HabitRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        habitDao = db.habitDao();
        allHabits = habitDao.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public LiveData<Habit> getHabit(int id) {
        return habitDao.getHabit(id);
    }

    public void insert(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.insertHabit(habit));
    }

    public void update(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.updateHabit(habit));
    }

    public void delete(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.deleteHabit(habit));
    }


    public void updateStar(int id, int newVal){
        Executors.newSingleThreadExecutor().execute(() -> {
            habitDao.updateStar(id, newVal);
        });
    }

    // Temporary dummy data
    public void insertDummyHabits() {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            if(habitDao.getCount()==0) {
                habitDao.insertHabit(new Habit(0, "Drink Water", "Stay hydrated", 1, 10));
                habitDao.insertHabit(new Habit(0, "Workout", "Exercise daily", 0, 50));
                habitDao.insertHabit(new Habit(0, "Read Book", "Read 20 pages", 1, 20));
                habitDao.insertHabit(new Habit(0, "Meditate", "10 minutes daily", 0, 100));
                habitDao.insertHabit(new Habit(0, "Study Algorithms", "Practice CLRS problems", 1, 60));
                habitDao.insertHabit(new Habit(0, "Finish App", "Finishing working on the wellness app", 1, 30));
                habitDao.insertHabit(new Habit(0,"Make Dinner", "Cook dinner and clean up after", 0, 0));
                habitDao.insertHabit(new Habit(0, "Draw", "Draw something every day", 0, 0));
            }
        });
    }
}
