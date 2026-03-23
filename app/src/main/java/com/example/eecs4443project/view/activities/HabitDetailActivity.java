package com.example.eecs4443project.view.activities;

import android.os.Bundle;
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
    private ProgressBar progressBar;
    private ImageView starIcon;

    private boolean isChanged = false;
    private int starState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        viewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        habitTitle = findViewById(R.id.habitTitle);
        habitDesc = findViewById(R.id.habitDesc);
        progressInput = findViewById(R.id.progressInput);
        progressBar = findViewById(R.id.progressBar);
        starIcon = findViewById(R.id.starIcon);

        Button updateBtn = findViewById(R.id.updateBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);

        // Get data
        int id = getIntent().getIntExtra("habit_id", -1);
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        int progress = getIntent().getIntExtra("progress", 0);
        int starred = getIntent().getIntExtra("starred", 0);

        habit = new Habit(id, title, desc, starred, progress);

        // Set UI
        habitTitle.setText(title);
        habitDesc.setText(desc);
        progressInput.setText(String.valueOf(progress));
        progressBar.setProgress(progress);

        starState = starred;
        updateStarUI();

        // Star toggle
        starIcon.setOnClickListener(v -> {
            starState = (starState == 1) ? 0 : 1;
            updateStarUI();
            isChanged = true;
        });

        // Update button
        updateBtn.setOnClickListener(v -> saveChanges());

        // Delete button
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

    private void updateStarUI() {
        starIcon.setImageResource(
                starState == 1 ?
                        android.R.drawable.btn_star_big_on :
                        android.R.drawable.btn_star_big_off
        );
    }

    private void saveChanges() {
        String title = habitTitle.getText().toString();
        String desc = habitDesc.getText().toString();
        int progress = Integer.parseInt(progressInput.getText().toString());

        habit.setTitle(title);
        habit.setDescription(desc);
        habit.setProgress(progress);
        habit.setStarred(starState);

        viewModel.update(habit);

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        isChanged = false;
    }
}