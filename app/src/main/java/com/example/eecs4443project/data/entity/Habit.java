package com.example.eecs4443project.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Defines the Habits SQLite table
@Entity(tableName = "habits")
public class Habit {
    // Defines columns in Habits table
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private int starred;
    private int progress;

    // Constructor
    public Habit(int id, String title, String description, int starred, int progress) {
        this.id=id;
        this.title = title;
        this.description = description;
        this.starred = starred;
        this.progress=progress;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStarred() {
        return starred;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
