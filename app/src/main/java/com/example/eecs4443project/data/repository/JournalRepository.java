package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.JournalDao;
import com.example.eecs4443project.data.entity.Journal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

// Allows ViewModel indirect access to database
public class JournalRepository {
    private final JournalDao journalDao;

    // Constructor
    public JournalRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        journalDao = db.journalDao();
    }

    // Create new journal entry
    public void insert(Journal journal) {
        AppDatabase.databaseWriteExecutor.execute(() -> journalDao.insertJournal(journal));
    }

    // Update an existing entry
    public void update(Journal journal) {
        AppDatabase.databaseWriteExecutor.execute(() -> journalDao.updateJournal(journal));
    }

    // Delete an entry
    public void delete(Journal journal) {
        AppDatabase.databaseWriteExecutor.execute(() -> journalDao.deleteJournal(journal));
    }

    // Delete all entries for a given user
    public void deleteAllForUser(int userId) {
        AppDatabase.databaseWriteExecutor.execute(() -> journalDao.deleteAllForUser(userId));
    }

    // Get all entries
    public LiveData<List<Journal>> getAllJournals() {
        return journalDao.getAllJournals();
    }

    // Get all entries associated with a given user
    public LiveData<List<Journal>> getJournalsByUser(int userId) {
        return journalDao.getJournalsByUser(userId);
    }

    // Get all entries associated with a given label
    public LiveData<List<Journal>> getJournalsByLabel(String label) {
        return journalDao.getJournalsByLabel(label);
    }

    // Get an entry by its id
    public LiveData<Journal> getJournal(int id) {
        return journalDao.getJournal(id);
    }

    // TEMP: add fake data to test with
    public void insertFakeData(int userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            journalDao.insertJournal(new Journal(userId, "My first entry", System.currentTimeMillis(), "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", "Personal"));
            journalDao.insertJournal(new Journal(userId, "Another thought", System.currentTimeMillis(), "Hi", "Personal"));
            journalDao.insertJournal(new Journal(userId, "Meeting notes", System.currentTimeMillis(), "Finish 3 pieces of homework.", "Work"));
        });
    }
}
