package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.viewmodel.HabitCustomViewModel;


public class HabitAddActivity extends AppCompatActivity {

    private HabitCustomViewModel viewModel;
    private Habit habit;

    private EditText addHabitTitle, addHabitDesc, addProgressInput;
    private SeekBar addProgressSeekBar;

    private boolean isSelected;

    private boolean isChanged = false;
    private boolean isInitializing=true;
    private int starState = 0;

    Button addBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        //Creates a viewmodel
        viewModel = new ViewModelProvider(this).get(HabitCustomViewModel.class);

        //Set all ui elements
        addHabitTitle = findViewById(R.id.addHabitTitle);
        addHabitDesc = findViewById(R.id.addHabitDesc);
        addProgressInput = findViewById(R.id.addProgressInput);
        addProgressSeekBar = findViewById(R.id.addProgressSeekBar);



        // Get data sent from the intent
        int id = getIntent().getIntExtra("habit_id", -1);
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        int progress = getIntent().getIntExtra("progress", 0);
        int starred = getIntent().getIntExtra("starred", 0);
        // When the habits from the selected habits are being updated in detail, "isSelected" should be true
        isSelected = getIntent().getBooleanExtra("isSelected", true);

        //create a new habit item based on the intent data gathered
        habit = new Habit(id, title, desc, starred, progress);
        // Set the isSelected to true for the new habit (by default would be false in constructor)
        habit.setIsSelected(isSelected);

        //Set UI
        addHabitTitle.setText(title);
        addHabitDesc.setText(desc);
        addProgressInput.setText(String.valueOf(progress));
        addProgressSeekBar.setProgress(progress);

        starState = starred;
        isInitializing=false;


        //Title change
        //Create a textwatcher to detect change in the page text
        addHabitTitle.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isInitializing){
                    isChanged=true;
                }
            }

        });

        //description change
        addHabitDesc.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isInitializing){
                    isChanged=true;
                }
            }

        });

        //progress seekbar change
        addProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                addProgressInput.setText(String.valueOf(value));
                isChanged = true;
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //sync the progress text input to the seekbar
        //updates the value based on the user's change
        addProgressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 100) value = 100;
                    if (value < 0) value = 0;

                    addProgressSeekBar.setProgress(value);
                    isChanged = true;
                }
            }
        });

        addBtn = findViewById(R.id.addAddButton);
        cancelBtn = findViewById(R.id.addCancelButton);

        cancelBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Task")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (d, i) -> {
                        //viewModel.delete(habit);
                        // modified this to be unselected instead of edited - maintaining the instance idea
                        viewModel.unselectHabit(habit);
                        Intent cancelAdd = new Intent(HabitAddActivity.this, NewHabitsActivity.class);
                        startActivity(cancelAdd);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


        // If the add button is clicked, the values are updated based on what the user changed
        addBtn.setOnClickListener(v -> {
            String habitTitle = addHabitTitle.getText().toString().trim();
            String habitDesc = addHabitDesc.getText().toString().trim();
            int newProgress = addProgressSeekBar.getProgress();
            boolean isValid = true;

            if (habitTitle.isEmpty()) {
                // provide an error if the title is empty - there should always be a title
                Toast.makeText(this, "Invalid habit title", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            else {
                // set the new values of the habit
                habit.setTitle(habitTitle);
                habit.setDescription(habitDesc);
                habit.setProgress(newProgress);
                // The habit is automatically selected
                habit.setIsSelected(true);
                // The habit is updated, not inserted (unlike CustomHabitActivity)
                viewModel.update(habit);

                // Let users know the habit has been added
                Toast.makeText(this, "Habit added", Toast.LENGTH_SHORT).show();
                // Navigate back to where users can see all their selected habits
                Intent addedHabit = new Intent(HabitAddActivity.this, HabitsActivity.class);
                startActivity(addedHabit);
            }


        });


        // Back handler
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            //id there are any unsaved changes, there will be an alert popup asking
            //if the user wants to save or discard those changes
            //if user presses save, changes will be saved
            //else changes are ignored
            @Override
            public void handleOnBackPressed() {
                if (isChanged) {
                    new AlertDialog.Builder(HabitAddActivity.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("Save changes before leaving?")
                            .setPositiveButton("Save", (d, i) -> {
                                saveChanges();
                                setEnabled(false);
                                getOnBackPressedDispatcher().onBackPressed();
                            })
                            .setNegativeButton("Discard", (d, i) -> {
                                setEnabled(false);
                                getOnBackPressedDispatcher().onBackPressed();
                            })
                            .show();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };


        getOnBackPressedDispatcher().addCallback(this, callback);

    }


    //saves the current input of all fields
    private void saveChanges() {
        String title = addHabitTitle.getText().toString();
        String desc = addHabitDesc.getText().toString();
        int progress;
        try {
            progress = Integer.parseInt(addProgressInput.getText().toString());
        } catch (Exception e) {
            progress = 0;
        }

        habit.setTitle(title);
        habit.setDescription(desc);
        habit.setProgress(progress);
        habit.setStarred(starState);
        habit.setIsSelected(isSelected);

        viewModel.update(habit);

        //gives a toast message so users know their changes have been saved
        // NOTE: the screen does not change in case users want to edit again
        //they must go back manually after saving if they want to exit the detail screen
        Toast.makeText(this, "Habit Changes Saved", Toast.LENGTH_SHORT).show();
        isChanged = false;
    }





}
