package com.example.eecs4443project.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.eecs4443project.data.dao.HabitDao;
import com.example.eecs4443project.data.dao.JournalDao;
import com.example.eecs4443project.data.dao.ReminderDao;
import com.example.eecs4443project.data.dao.UserDao;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.entity.Journal;
import com.example.eecs4443project.data.entity.Reminder;
import com.example.eecs4443project.data.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Creates actual SQLite database
@Database(entities = {Habit.class, Reminder.class, User.class, Journal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // Volatile (and "synchronized" later) ensure only 1 thread can create the database at a time
    private static volatile AppDatabase INSTANCE;

    // Create threads for db operations to run in parallel in the background (instead of on the main thread, where UI runs)
    private static final int NUM_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    // Creates an instance of the database
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_db")
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract HabitDao habitDao();

    public abstract ReminderDao reminderDao();

    public abstract UserDao userDao();

    public abstract JournalDao journalDao();
}
