package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.eecs4443project.ReminderWorker;
import com.example.eecs4443project.data.dao.ReminderDao;
import com.example.eecs4443project.data.entity.Reminder;
import com.example.eecs4443project.data.AppDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReminderRepository {

    private final ReminderDao reminderDao;
    private final ExecutorService executorService;
    private final Application application;

    public ReminderRepository(Application application) {
        this.application = application;

        AppDatabase db = AppDatabase.getInstance(application);
        reminderDao = db.reminderDao();

        executorService = Executors.newSingleThreadExecutor();
    }

    //getters

    public LiveData<List<Reminder>> getAllReminders() {
        return reminderDao.getAllReminders();
    }

    public LiveData<List<Reminder>> getActiveReminders() {
        return reminderDao.getActiveReminders();
    }

    public LiveData<List<Reminder>> getCompletedReminders() {
        return reminderDao.getCompletedReminders();
    }

    public Reminder getReminder(int id) {
        return reminderDao.getReminderById(id);
    }

    //dummy data
    public void insertDummyReminders() {
        executorService.execute(() -> {
            reminderDao.insertReminder(new Reminder("Test 1", "2026-04-10", "12:00", false, false));
            reminderDao.insertReminder(new Reminder("Test 2", "2026-04-11", "14:00", false, false));
        });
    }

    //insert
    public void insert(Reminder reminder) {
        executorService.execute(() -> {

            long id = reminderDao.insertReminder(reminder);
            reminder.setId((int) id);
            //once the reminders are saved, schedule the reminder notification
            scheduleReminderNotifications(reminder);
        });
    }

    //update
    public void update(Reminder reminder) {
        executorService.execute(() -> {

            reminderDao.updateReminder(reminder);

            WorkManager.getInstance(application)
                    .cancelAllWorkByTag("reminder_" + reminder.getId());

            scheduleReminderNotifications(reminder);
        });
    }

    //delete
    //this deletes the reminder and the scheduled notifications
    public void delete(Reminder reminder) {
        executorService.execute(() -> {

            reminderDao.deleteReminder(reminder);

            WorkManager.getInstance(application).cancelAllWorkByTag("reminder_" + reminder.getId());
        });
    }


    //function to schedule the reminder notifications
    //reminder notifications are set for a day before, an hour before, and at the exact time
    //so that users don't get too many reminders, but receive a fair amount
    private void scheduleReminderNotifications(Reminder reminder) {

        long triggerTime = parseDateTime(reminder.getDate(), reminder.getTime());
        if (triggerTime == -1) return;

        long now = System.currentTimeMillis();

        long oneDayBefore = triggerTime - TimeUnit.DAYS.toMillis(1);
        long oneHourBefore = triggerTime - TimeUnit.HOURS.toMillis(1);

        scheduleIfValid(oneDayBefore, now, reminder, "Due tomorrow");
        scheduleIfValid(oneHourBefore, now, reminder, "Due in 1 hour");
        scheduleIfValid(triggerTime, now, reminder, "Due now");
    }

    //function to determine if valid in order to schedule the notification properly
    private void scheduleIfValid(long scheduledTime, long now, Reminder reminder, String suffix) {
        if (scheduledTime <= now) return;

        long delay = scheduledTime - now;

        Data data = new Data.Builder().putString("title", "Reminder").putString("message", reminder.getTitle() + " - " + suffix).build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ReminderWorker.class).setInitialDelay(delay, TimeUnit.MILLISECONDS).addTag("reminder_" + reminder.getId()).setInputData(data).build();
        WorkManager.getInstance(application).enqueue(request);
    }

    private long parseDateTime(String date, String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date parsed = sdf.parse(date + " " + time);
            return parsed != null ? parsed.getTime() : -1;

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}