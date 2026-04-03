package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.viewmodel.HabitCustomViewModel;
import com.example.eecs4443project.viewmodel.HabitListViewModel;

public class CustomHabitActivity extends AppCompatActivity {

    private Habit habit;

    private HabitCustomViewModel habitCstmVM;
    EditText cstmHabitTitle, cstmHabitDesc, cstmHabitProgressInput;
    SeekBar cstmHabitSeekBar;

    Button createHabit, cancelHabit;

    private boolean isChanged = false;
    private boolean isInitializing=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_habit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customHabitMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up ViewModel
        habitCstmVM = new ViewModelProvider(this).get(HabitCustomViewModel.class);

        // Link the UI elements to the defined variables
        cstmHabitTitle = findViewById(R.id.cstmHabitTitle);
        cstmHabitDesc = findViewById(R.id.cstmHabitDesc);
        cstmHabitProgressInput = findViewById(R.id.cstmProgressInput);
        cstmHabitSeekBar = findViewById(R.id.cstmProgressSeekBar);

        // Link the buttons
        createHabit = findViewById(R.id.addCstmButton);
        cancelHabit = findViewById(R.id.cancelCstmButton);

        // Provide a listener for the title editable fields to keep track of changes made
        cstmHabitTitle.addTextChangedListener(new TextWatcher(){
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

        // Provide a listener for the description editable field to keep track of changes made
        cstmHabitDesc.addTextChangedListener(new TextWatcher(){
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

        // Provide a listener for the progress editable field to keep track of changes made
        cstmHabitSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                cstmHabitProgressInput.setText(String.valueOf(value));
                isChanged = true;
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // sync the progress text input to the seekbar
        // updates the value based on the user's change
        cstmHabitProgressInput.addTextChangedListener(new TextWatcher() {
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

                    cstmHabitSeekBar.setProgress(value);
                    isChanged = true;
                }
            }
        });


        // Cancel button listener - will reset the fields to empty, and navigate back to the habit list screen
        cancelHabit.setOnClickListener(v -> {
            // When cancel is clicked, the text fields are reset
            // with progress initialized to 0, and the text
            // becoming blank
            cstmHabitTitle.setText("");
            cstmHabitDesc.setText("");
            cstmHabitProgressInput.setText(String.valueOf(0));
            cstmHabitSeekBar.setProgress(0);

            // Cancel then takes the user back to the Main Activity
            Intent cancelIntent = new Intent(CustomHabitActivity.this, NewHabitsActivity.class);
            startActivity(cancelIntent);

        });

        // Create habit button listener - will set the fields to what the user edited, unless the title is empty
        createHabit.setOnClickListener(v -> {
            String habitTitle=cstmHabitTitle.getText().toString().trim();
            String habitDesc=cstmHabitDesc.getText().toString().trim();
            int progress = cstmHabitSeekBar.getProgress();
            // The app would like the user to set a titlenfor each of the habits
            // However, they do not need to set progress immediately, so they will not be prompted
            // to do so before successfully creating their custom task
            boolean isValid=true;

            if (habitTitle.isEmpty()) {
                Toast.makeText(this, "Invalid habit title", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            else {
                // the user can star the habit only after they have added it.
                // for custom habits, a new habit is made. For "added" habits, even if they are edited, they are only updated, not inserted
                habit = new Habit(habitTitle, habitDesc, 0, progress);
                // the habit is immediately set as "isSelected" so that it appears in the selected list of Habits in the HabitActivity
                habit.setIsSelected(true);
                habitCstmVM.insert(habit);
            }

            // Reset to default values after a successful added habit
            cstmHabitTitle.setText("");
            cstmHabitDesc.setText("");
            cstmHabitProgressInput.setText(String.valueOf(0));
            cstmHabitSeekBar.setProgress(0);

            //OPTIONAL (can be removed). Right after the user adds their new task, they should be navigated to see it
            Toast.makeText(this, "Custom habit added", Toast.LENGTH_SHORT).show();
            Intent createdCustom = new Intent(CustomHabitActivity.this, HabitsActivity.class);
            startActivity(createdCustom);


        });

    }


}
