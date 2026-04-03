package com.example.eecs4443project.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// Defines the Habits SQLite table
@Entity(tableName = "habits")
public class Habit {
    // Defines columns in Habits table
    // id is a primary key and is automatically generated
    @PrimaryKey(autoGenerate = true)
    private int id;

    // habit title
    private String title;

    // habit description
    private String description;

    // habit favourited
    private int starred;

    // habit progress
    private int progress;

    // boolean flag to check if habit has been selected already
    private boolean isSelected;

    // Constructor for Habit creation - should not have a manual id, since it is auto-generated
    public Habit(String title, String description, int starred, int progress) {
        this.title = title;
        this.description = description;
        this.starred = starred;
        this.progress=progress;
        // by default, habit is not selected
        this.isSelected = false;
    }

    @Ignore
    // Constructor when the id is already known and items need only be read
    public Habit(int id, String title, String description, int starred, int progress) {
        this.id=id;
        this.title = title;
        this.description = description;
        this.starred = starred;
        this.progress=progress;
        // by default, habit is not selected
        this.isSelected=false;
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

    public boolean getIsSelected() {return this.isSelected;}

    public void setIsSelected(boolean isSelected) {this.isSelected = isSelected;}



}

