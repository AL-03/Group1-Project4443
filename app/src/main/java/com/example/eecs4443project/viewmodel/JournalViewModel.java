package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.eecs4443project.data.entity.Journal;
import com.example.eecs4443project.data.repository.JournalRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

// Middleman between views (e.g. activities) and database
public class JournalViewModel extends AndroidViewModel {
    // Provides indirect access to database
    private final JournalRepository repo;

    // Constructor
    public JournalViewModel(@NonNull Application app) {
        super(app);
        this.repo = new JournalRepository(app);
    }

    // Create new journal entry
    public void insert(Journal journal) {
        repo.insert(journal);
    }

    // Update an existing entry
    public void update(Journal journal) {
        repo.update(journal);
    }

    // Delete an entry
    public void delete(Journal journal) {
        repo.delete(journal);
    }

    // Get all entries
    public LiveData<List<Journal>> getAllJournals() {
        return repo.getAllJournals();
    }

    // Get all entries associated with a given user
    public LiveData<List<Journal>> getJournalsByUser(int userId) {
        return repo.getJournalsByUser(userId);
    }

    // Get all entries associated with a given label
    public LiveData<List<Journal>> getJournalsByLabel(String label) {
        return repo.getJournalsByLabel(label);
    }

    // Get all of a user's entries, grouped by label
    public LiveData<Map<String, List<Journal>>> getJournalsByAllLabels(int userId) {
        // Gets list of all journal entries and transforms it such that it's grouped by label
        return Transformations.map(repo.getJournalsByUser(userId), journals -> {
            // List of all journal entries grouped by label (returned later)
            Map<String, List<Journal>> grouped = new LinkedHashMap<>();

            for (Journal j : journals) {
                String label = j.getLabel();
                // If a label hasn't been seen previously, create a new key-value pair for it
                if (!grouped.containsKey(label)) {
                    grouped.put(label, new ArrayList<>());
                }
                // Add the journal to the given label's list
                grouped.get(label).add(j);
            }
            return grouped;
        });
    }

    // TEMP: add fake data to test with
    public void insertFakeData(int userId) {
        repo.insertFakeData(userId);
    }

    // Get an entry by its id
    public LiveData<Journal> getJournal(int id) {
        return repo.getJournal(id);
    }
}
