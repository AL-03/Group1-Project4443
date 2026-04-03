package com.example.eecs4443project.view.activities;

import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eecs4443project.data.entity.Habit;

public class CustomHabitActivity extends AppCompatActivity {

    /*
    TODO: Need to handle the logic for creating a brand new habit with the information
    the user provides. When the custom habit is created, it would be good to have a
    toast declaration
    There is also a cancel and create button.
    When the create button is pressed, a handler needs to check that none of the fields
    are empty. If they are, a Toast statement. If they aren't, the user receives a message
    that the habit has been added, and they will be navigated back to their list of habits
    In the backend, the isSelected flag goes up, such that the habit will also be greyed out
    in the habit list
    */
    private Habit habit;
    EditText cstmHabitTitle, cstmHabitDesc, cstmHabitProgressInput;
    SeekBar cstmHabitSeekBar;

    Button createHabit, cancelHabit;


}
