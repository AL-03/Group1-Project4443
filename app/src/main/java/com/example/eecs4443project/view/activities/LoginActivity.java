package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.eecs4443project.DatabaseHelper;
import com.example.eecs4443project.R;
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

        // testing login
        userViewModel.register(new User("testuser", "password123"));

        // when user clicks login, call the onclicklistener and set username and password
        // from the information the user input
        loginBtn.setOnClickListener(v -> {
        try {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
    
            if (user.isEmpty()) {
                username.setError("Please enter a username");
                return;
            }
    
            if (pass.isEmpty()) {
                password.setError("Please enter a password");
                return;
            }
    
            new Thread(() -> {
                User loggedInUser = userViewModel.getUserSync(user, pass);
    
                runOnUiThread(() -> {
                    if (loggedInUser != null) {
    
                        int userId = loggedInUser.getId();
    
                        // store the user id
                        SessionManager.setUserId(userId);
    
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("USERNAME", user);
                        startActivity(intent);
                        finish();
    
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
    
                    } else {
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            });
    
        } catch (Exception e) {
            Log.e("LOGIN_ERROR", "Crash on login", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
});
    }
}
