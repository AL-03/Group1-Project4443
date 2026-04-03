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
}
