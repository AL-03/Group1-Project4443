package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.HabitDao;
import com.example.eecs4443project.data.entity.Habit;

import java.util.List;

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

    public void toggleStar(int id, int newVal) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.updateStar(id, newVal));
    }

    // TEMP
    public void insertDummyHabits() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            habitDao.insertHabit(new Habit("Drink Water", "Stay hydrated", 1));
            habitDao.insertHabit(new Habit("Workout", "Exercise daily", 0));
            habitDao.insertHabit(new Habit("Read Book", "Read 20 pages", 1));
            habitDao.insertHabit(new Habit("Meditate", "10 minutes daily", 0));
            habitDao.insertHabit(new Habit("Study Algorithms", "Practice CLRS problems", 1));
        });
    }
}
