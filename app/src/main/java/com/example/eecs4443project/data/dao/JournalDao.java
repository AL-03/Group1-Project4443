package com.example.eecs4443project.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eecs4443project.data.entity.Journal;

import java.util.List;

// Defines SQL operations to access journal data
@Dao

public interface JournalDao {
    // Create new journal entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJournal(Journal journal);

    // Update an existing entry
    @Update
    void updateJournal(Journal journal);

    // Delete an entry
    @Delete
    void deleteJournal(Journal journal);

    // Get all entries
    @Query("SELECT * FROM journals")
    LiveData<List<Journal>> getAllJournals();

    // Get all entries associated with a given user, ordered by date
    @Query("SELECT * FROM journals WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Journal>> getJournalsByUser(int userId);

    // Get all entries associated with a given label
    @Query("SELECT * FROM journals WHERE label = :label")
    LiveData<List<Journal>> getJournalsByLabel(String label);

    // Get an entry by its id
    @Query("SELECT * FROM journals WHERE id = :id")
    LiveData<Journal> getJournal(int id);
}
