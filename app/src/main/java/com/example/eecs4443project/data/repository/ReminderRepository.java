package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.ReminderDao;
import com.example.eecs4443project.data.entity.Reminder;

import java.util.List;

public class ReminderRepository {

    private final ReminderDao reminderDao;

    private final LiveData<List<Reminder>> allReminders;
    private final LiveData<List<Reminder>> activeReminders;
    private final LiveData<List<Reminder>> completedReminders;

    public ReminderRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        reminderDao = db.reminderDao();

        allReminders = reminderDao.getAllReminders();
        activeReminders = reminderDao.getActiveReminders();
        completedReminders = reminderDao.getCompletedReminders();
    }

    //getters for live data
    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public LiveData<List<Reminder>> getActiveReminders() {
        return activeReminders;
    }

    public LiveData<List<Reminder>> getCompletedReminders() {
        return completedReminders;
    }

    //gets single reminder
    public Reminder getReminder(int id) {
        return reminderDao.getReminderById(id);
    }

    //insert,update, delete
    public void insert(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                reminderDao.insertReminder(reminder)
        );
    }

    public void update(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                reminderDao.updateReminder(reminder)
        );
    }

    public void delete(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                reminderDao.deleteReminder(reminder)
        );
    }

    //completion handling
    public void markCompleted(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                reminderDao.markAsCompleted(reminder.getId())
        );
    }

    public void markActive(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                reminderDao.markAsActive(reminder.getId())
        );
    }

    //Temporary dummy data
    public void insertDummyReminders() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (reminderDao.getCount() == 0) {
                reminderDao.insertReminder(new Reminder("Submit Assignment", "2026-03-22", "10:00", false, false));
                reminderDao.insertReminder(new Reminder("Doctor Appointment", "2026-03-23", "14:30", false, false));
                reminderDao.insertReminder(new Reminder("Team Meeting", "2026-03-24", "09:00", false, false));
                reminderDao.insertReminder(new Reminder("Clean Room", "2026-03-26", "11:00", false, false));
                reminderDao.insertReminder(new Reminder("Visit Family", "2026-04-01", "12:00", false, false));
                reminderDao.insertReminder(new Reminder("Patch Jacket", "2026-04-02", "09:00", false, false));
            }
        });
    }
}