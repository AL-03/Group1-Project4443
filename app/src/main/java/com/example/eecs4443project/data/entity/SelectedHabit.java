package com.example.eecs4443project.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="selected_habits", foreignKeys= @ForeignKey(entity = Habit.class, parentColumns = "id", childColumns = "habitId", onDelete = ForeignKey.CASCADE))
public class SelectedHabit {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int habitId;

    public SelectedHabit(int habitId) {
        this.habitId = habitId;
    } // the habitId is essentially a foreign key for the Habit's ID
}
