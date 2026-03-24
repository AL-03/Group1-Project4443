package com.example.eecs4443project.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eecs4443project.data.entity.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    //insert, update, and delete functions

    @Insert
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    //get all from database
    @Query("SELECT * FROM reminders ORDER BY date ASC, time ASC")
    LiveData<List<Reminder>> getAllReminders();

    //get only active reminders
    @Query("SELECT * FROM reminders WHERE completed = 0 ORDER BY date ASC, time ASC")
    LiveData<List<Reminder>> getActiveReminders();

    //gets completed reminders
    @Query("SELECT * FROM reminders WHERE completed = 1 ORDER BY date DESC, time DESC")
    LiveData<List<Reminder>> getCompletedReminders();

    //get a single reminder
    @Query("SELECT * FROM reminders WHERE id = :id LIMIT 1")
    Reminder getReminderById(int id);

    //count
    @Query("SELECT COUNT(*) FROM reminders")
    int getCount();

   //update status
    @Query("UPDATE reminders SET completed = 1 WHERE id = :id")
    void markAsCompleted(int id);

    @Query("UPDATE reminders SET completed = 0 WHERE id = :id")
    void markAsActive(int id);
}