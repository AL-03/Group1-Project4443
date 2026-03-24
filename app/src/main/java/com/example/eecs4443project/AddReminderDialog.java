package com.example.eecs4443project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.eecs4443project.data.entity.Reminder;
import com.example.eecs4443project.viewmodel.ReminderViewModel;

public class AddReminderDialog {

    public static void show(Context context, ReminderViewModel viewModel) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_reminder, null);

        EditText title = view.findViewById(R.id.inputTitle);
        EditText date = view.findViewById(R.id.inputDate);
        EditText time = view.findViewById(R.id.inputTime);


        new AlertDialog.Builder(context)
                .setTitle("Add Reminder")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {

                    String t = title.getText().toString();
                    String d = date.getText().toString();
                    String ti = time.getText().toString();

                    if (!t.isEmpty()) {
                        Reminder r = new Reminder(t, d, ti, false, false);
                        viewModel.insert(r);
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
