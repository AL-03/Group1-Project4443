package com.example.eecs4443project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        db = new DatabaseHelper(this);

        // when user clicks login, call the onclicklistener and set username and password
        // from the information the user input
        loginBtn.setOnClickListener(v -> {
            try {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                // input validation
                // ensures user actually enters values before checking database
                if (user.isEmpty()) {
                    username.setError("Please enter a username");
                    return;
                }

                if (pass.isEmpty()) {
                    password.setError("Please enter a password");
                    return;
                }

                // database login check
                // check login info from database to see if username and password is correct
                if (db.checkLogin(user, pass)) {

                    // if the login information exists in the database, create a new intent
                    // to direct the user to the dashboard
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                    // we want to store the username to display a personalized
                    // welcome message for the user
                    intent.putExtra("USERNAME", user);

                    startActivity(intent);

                    // optional: finish login so user can't go back
                    finish();

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                } else {
                    // invalid credentials feedback
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // catch unexpected runtime/database errors
                Log.e("LOGIN_ERROR", "Crash on login", e);
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}