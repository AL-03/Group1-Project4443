package com.example.eecs4443project.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.eecs4443project.R;
import com.example.eecs4443project.SessionManager;
import com.example.eecs4443project.data.entity.User;
import com.example.eecs4443project.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private EditText usernameDisplay, passwordDisplay;
    private static EditText journalDisplay;
    private Button saveChanges, lightTheme, darkTheme;
    private Switch journalPasswordToggle;
    private LinearLayout journalSettings;
    private static boolean isJournalPassword;
    private User user;
    private boolean nightMode;

    private BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Access user data
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Get UIs from xml
        usernameDisplay = findViewById(R.id.username);
        passwordDisplay = findViewById(R.id.password);
        saveChanges = findViewById(R.id.saveButton);
        journalSettings = findViewById(R.id.journalSettings);
        journalDisplay = findViewById(R.id.journalPassword);
        journalPasswordToggle = findViewById(R.id.privacyToggle);
        nav = findViewById(R.id.bottomNav);

        // Set whole view as focused
        ConstraintLayout root = findViewById(R.id.profile);
        root.requestFocus();

        // If anywhere on the screen is tapped, refocus on the root
        root.setOnTouchListener((v, event) -> {
            // Clear focus from anything that's currently focused
            View current = getCurrentFocus();
            if (current instanceof EditText) {
                current.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
            }

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            // Allow touch handling as usual
            return false;
        });


        // Get associated user and display their credentials
        int userId = SessionManager.getUserId(this);
        viewModel.getUser(userId).observe(this, u -> {
            user = u;
            // Display the user's username and password if it's not a null object
            if (user != null) {
                usernameDisplay.setText(user.getUsername());
                passwordDisplay.setText(user.getPassword());
            }
            else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        });

        // When clicked, allow editing of credentials
        usernameDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Allow editing
                usernameDisplay.setInputType(InputType.TYPE_CLASS_TEXT);
                // Makes keyboard appear
                usernameDisplay.setFocusableInTouchMode(true);
                // Shows caret
                usernameDisplay.setCursorVisible(true);
                // Enters edit mode
                usernameDisplay.requestFocus();
            }
        });
        passwordDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Allow editing
                passwordDisplay.setInputType(InputType.TYPE_CLASS_TEXT);
                // Makes keyboard appear
                passwordDisplay.setFocusableInTouchMode(true);
                // Shows caret
                passwordDisplay.setCursorVisible(true);
                // Enters edit mode
                passwordDisplay.requestFocus();
            }
        });

        journalDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Allow editing
                passwordDisplay.setInputType(InputType.TYPE_CLASS_TEXT);
                // Makes keyboard appear
                passwordDisplay.setFocusableInTouchMode(true);
                // Shows caret
                passwordDisplay.setCursorVisible(true);
                // Enters edit mode
                passwordDisplay.requestFocus();
            }
        });

        // When users tap something outside of the credential, disable editing again
        usernameDisplay.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                usernameDisplay.setInputType(InputType.TYPE_NULL);
                usernameDisplay.setFocusable(false);
                usernameDisplay.setCursorVisible(false);

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        passwordDisplay.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                passwordDisplay.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordDisplay.setFocusable(false);
                passwordDisplay.setCursorVisible(false);

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        journalDisplay.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                passwordDisplay.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordDisplay.setFocusable(false);
                passwordDisplay.setCursorVisible(false);

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        // If privacyToggle is off, hide the journal password content
        journalPasswordToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                journalSettings.setVisibility(View.VISIBLE);
            }
            else {
                journalSettings.setVisibility(View.GONE);
            }
        });

        // Handle ability to edit


        // Save changes
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check whether user wants to set password on journal
                isJournalPassword = journalPasswordToggle.isChecked();

                // Update user credentials
                if (!passwordDisplay.getText().toString().isEmpty() && !usernameDisplay.getText().toString().isEmpty() && (!user.getUsername().equals(usernameDisplay.getText().toString()) || ! user.getPassword().equals(passwordDisplay.getText().toString()))) {
                    user.setUsername(usernameDisplay.getText().toString());
                    user.setPassword(passwordDisplay.getText().toString());
                }

                Toast.makeText(ProfileActivity.this, "Successfully saved changes", Toast.LENGTH_SHORT).show();
            }

        });

        // Set up nav bar
        nav.setSelectedItemId(R.id.nav_account);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (item.getItemId() == R.id.nav_habits) {
                startActivity(new Intent(this, HabitsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (item.getItemId() == R.id.nav_journal) {
                startActivity(new Intent(this, JournalActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return item.getItemId() == R.id.nav_account;
        });
    }

    public static boolean isJournalPassword()
    {
      return isJournalPassword;
    }

    public static String getJournalPassword()
    {
        return journalDisplay.getText().toString();
    }
}
