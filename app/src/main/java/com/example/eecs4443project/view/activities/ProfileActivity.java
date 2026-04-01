package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eecs4443project.R;
import com.example.eecs4443project.SessionManager;
import com.example.eecs4443project.data.entity.User;
import com.example.eecs4443project.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private EditText usernameDisplay, passwordDisplay;
    private EditText newPassword, newUsername;
    private static EditText journalPassword;
    private Button saveChanges, lightTheme, darkTheme;
    private Switch journalPasswordToggle;
    private static boolean isJournalPassword;
    private User user;
    private boolean nightMode;

    private BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameDisplay = findViewById(R.id.username);
        passwordDisplay = findViewById(R.id.password);
        saveChanges = findViewById(R.id.saveButton);
        lightTheme = findViewById(R.id.lightButton);
        darkTheme = findViewById(R.id.darkButton);
        journalPassword = findViewById(R.id.journalPassword);
        journalPasswordToggle = findViewById(R.id.toggle);
        nav = findViewById(R.id.bottomNav);
        user = viewModel.getUser(SessionManager.getUserId(this));

        usernameDisplay.setText(user.getUsername());
        passwordDisplay.setText(user.getPassword());

        darkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightMode = true;

            }

        });

        lightTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightMode = false;

            }

        });


    saveChanges.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isJournalPassword = journalPasswordToggle.isChecked();

            if (!newPassword.getText().toString().isEmpty() && !newUsername.getText().toString().isEmpty() && (!user.getUsername().equals(usernameDisplay.getText().toString()) || ! user.getPassword().equals(passwordDisplay.getText().toString()))) {
                viewModel.delete(user.getUsername());
                 user.setUsername(usernameDisplay.getText().toString());
                 user.setPassword(usernameDisplay.getText().toString());
                viewModel.register(new User( user.getUsername(),  user.getPassword()));
            }

            if(nightMode)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }



        }

    });

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

            if (item.getItemId() == R.id.nav_account) {
                return true;
            }

            return false;
        });
}
        public static boolean isJournalPassword()
        {
          return isJournalPassword;
        }

        public static String getJournalPassword()
        {
            return journalPassword.getText().toString();
        }
}
