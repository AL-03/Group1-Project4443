package com.example.eecs4443project;

public class Habit {
    int id;
    String title;
    String description;
    int starred;

    public Habit(int id, String title, String description, int starred) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.starred = starred;
    }
}