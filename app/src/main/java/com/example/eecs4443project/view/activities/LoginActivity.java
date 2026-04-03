package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.eecs4443project.R;
import com.example.eecs4443project.SessionManager;
import com.example.eecs4443project.data.entity.User;
import com.example.eecs4443project.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // TEMP test user - remove when done testing
        userViewModel.register(new User("testuser", "password123"));

        loginBtn.setOnClickListener(v -> {

            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (user.isEmpty()) {
                username.setError("Enter username");
                return;
            }

            if (pass.isEmpty()) {
                password.setError("Enter password");
                return;
            }

            // Log in the user
            userViewModel.getUser(user, pass).observe(this, loggedInUser -> {
                if (loggedInUser != null) {

                    int userId = loggedInUser.getId();
                    SessionManager.setUserId(LoginActivity.this, userId);

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();

                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}