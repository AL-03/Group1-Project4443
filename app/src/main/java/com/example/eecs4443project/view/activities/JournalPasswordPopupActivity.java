package com.example.eecs4443project.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
 ;

import com.example.eecs4443project.R;
import com.example.eecs4443project.view.fragments.journal.JournalListFragment;

public class JournalPasswordPopupActivity extends AppCompatActivity {

    private EditText passwordInput;
    private TextView notification;

    private Button enter, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.password_popup);

        passwordInput = findViewById(R.id.journalPasswordInput);
        notification = findViewById(R.id.notification);
        enter = findViewById(R.id.enter);
        cancel  = findViewById(R.id.cancel);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordInput.getText().toString().isEmpty() && passwordInput.getText().toString().equals(ProfileActivity.getJournalPassword()))
                {
                    JournalListFragment.setJournalPasswordCorrect(true);
                    finish();
                }
                else
                {
                   notification.setText(R.string.error_invalid_journal_password);
                }

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.setText("");
                finish();
            }

        });


    }


}