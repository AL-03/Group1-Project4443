package com.example.eecs4443project.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eecs4443project.R;
import com.example.eecs4443project.view.fragments.journal.JournalEditFragment;
import com.example.eecs4443project.view.fragments.journal.JournalListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class JournalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_journal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_journal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // When started up, load the fragment that displays the list of journal entries
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.journal_fragment_container, new JournalListFragment())
                    .commit();
        }

        // Navigation bar
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_journal);

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
                JournalListFragment fragment = JournalListFragment.newInstance();
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.journal_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            if (item.getItemId() == R.id.nav_account) {
                // placeholder
                //add intent when account page is made
                Toast.makeText(this, "Account page coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }
}