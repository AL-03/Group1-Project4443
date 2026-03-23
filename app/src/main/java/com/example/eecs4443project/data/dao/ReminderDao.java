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

    @Insert
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Query("SELECT * FROM reminders ORDER BY date, time")
    LiveData<List<Reminder>> getAllReminders();

    @Query("SELECT * FROM reminders WHERE id = :id ORDER BY date, time")
    Reminder getReminder(int id);

    @Query("SELECT COUNT (*) FROM reminders")
    int getCount();
}
