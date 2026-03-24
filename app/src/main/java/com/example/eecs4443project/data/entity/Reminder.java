package com.example.eecs4443project.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String date;
    private String time;
    private boolean completed;
    private boolean recurring;

    private long timestamp;
    public Reminder(String title, String date, String time, boolean completed, boolean recurring) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.completed = completed;
        this.recurring = recurring;
    }



    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public long getTimestamp() {
        return timestamp;
    }

    //setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
