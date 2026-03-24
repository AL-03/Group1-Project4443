package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.entity.Reminder;
import com.example.eecs4443project.data.repository.ReminderRepository;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private final ReminderRepository repo;
    private final LiveData<List<Reminder>> allReminders;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        repo = new ReminderRepository(application);
        allReminders = repo.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public LiveData<Reminder> getReminder(int id) {
        return repo.getReminder(id);
    }

    public void insert(Reminder reminder) {
        repo.insert(reminder);
    }

    public void update(Reminder reminder) {
        repo.update(reminder);
    }

    public void delete(Reminder reminder) {
        repo.delete(reminder);
    }

    // TEMP: dummy data
    public void insertDummyReminders() {
        repo.insertDummyReminders();
    }
}
