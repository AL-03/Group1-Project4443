package com.example.eecs4443project.view.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.viewmodel.HabitViewModel;

public class HabitDetailActivity extends AppCompatActivity {

    private HabitViewModel viewModel;
    private Habit habit;

    private EditText habitTitle, habitDesc, progressInput;
    private SeekBar progressSeekBar;
    private ImageView starIcon;

    private boolean isSelected;

    private boolean isChanged = false;
    private boolean isInitializing=true;
    private int starState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        //Creates a viewmodel
        viewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        //Set all ui elements
        habitTitle = findViewById(R.id.habitTitle);
        habitDesc = findViewById(R.id.habitDesc);
        progressInput = findViewById(R.id.progressInput);
        progressSeekBar = findViewById(R.id.progressSeekBar);
        starIcon = findViewById(R.id.starIcon);

        Button updateBtn = findViewById(R.id.updateBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);

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
        habitTitle.setText(title);
        habitDesc.setText(desc);
        progressInput.setText(String.valueOf(progress));
        progressSeekBar.setProgress(progress);

        starState = starred;

        isInitializing=false;
        updateStarUI();

        //Heart toggle change
        starIcon.setOnClickListener(v -> {
            starState = (starState == 1) ? 0 : 1;
            updateStarUI();
            isChanged = true;
        });

        //Title change
        //Create a textwatcher to detect change in the page text
        habitTitle.addTextChangedListener(new TextWatcher(){
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
        habitDesc.addTextChangedListener(new TextWatcher(){
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
        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                progressInput.setText(String.valueOf(value));
                isChanged = true;
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //sync the progress text input to the seekbar
        //updates the value based on the user's change
        progressInput.addTextChangedListener(new TextWatcher() {
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

                    progressSeekBar.setProgress(value);
                    isChanged = true;
                }
            }
        });

        // Update button sets new changes user made
        updateBtn.setOnClickListener(v -> saveChanges());

        // Delete button
        //gives an alert popup asking if they intended to delete the habit
        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Habit")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (d, i) -> {
                        viewModel.delete(habit);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
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
                    new AlertDialog.Builder(HabitDetailActivity.this)
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

    //function to update the star ui to change based on previous state
    private void updateStarUI() {
        starIcon.setImageResource(
                starState == 1 ?
                        R.drawable.baseline_favorite_24 :
                        R.drawable.baseline_favorite_border_24
        );
    }

//saves the current input of all fields
    private void saveChanges() {
        String title = habitTitle.getText().toString();
        String desc = habitDesc.getText().toString();
        int progress;
        try {
            progress = Integer.parseInt(progressInput.getText().toString());
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