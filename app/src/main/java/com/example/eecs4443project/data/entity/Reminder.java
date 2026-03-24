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

    public Reminder(String title, String date, String time, boolean completed, boolean recurring) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.completed = completed;
        this.recurring = recurring;
    }

    // getters and setters

    public int getId() {
        return id; }
    public void setId(int id) {
        this.id = id; }

    public String getTitle() {
        return title; }
    public void setTitle(String title) {
        this.title = title; }

    public String getDate() {
        return date; }
    public void setDate(String date) {
        this.date = date; }

    public String getTime() {
        return time; }
    public void setTime(String time) {
        this.time = time; }

    public boolean isCompleted() {
        return completed; }
    public void setCompleted(boolean completed) {
        this.completed = completed; }

    public boolean isRecurring() {
        return recurring; }
    public void setRecurring(boolean recurring) {
        this.recurring = recurring; }
}
