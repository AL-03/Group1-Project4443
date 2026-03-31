package com.example.eecs4443project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Reminder;
import com.example.eecs4443project.viewmodel.ReminderViewModel;

import java.util.Calendar;
import java.util.Locale;

public class AddReminderDialog {

    //show the reminder dialog
    public static void show(Context context, ReminderViewModel viewModel) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_reminder, null);

        EditText title = view.findViewById(R.id.inputTitle);
        EditText dateInput = view.findViewById(R.id.inputDate);
        EditText timeInput = view.findViewById(R.id.inputTime);

        //initialize a calendar
        Calendar calendar = Calendar.getInstance();

        //date picker to pick reminder date
        dateInput.setOnClickListener(v -> {
            //today's date
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //create a calendar date picker
            //date information is formatted in a specific way for database purposes
            DatePickerDialog datePicker = new DatePickerDialog(context,
                    (view1, y, m, d) -> {

                        // format: YYYY-MM-DD
                        String formatted = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", y, m + 1, d);

                        dateInput.setText(formatted);
                    }, year, month, day);

            datePicker.show();
        });

        //time picker for reminder
        timeInput.setOnClickListener(v -> {
            //time right now
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            //create a time picker and set the format of the resulting string for database purposes
            TimePickerDialog timePicker = new TimePickerDialog(context,
                    (view12, h, m) -> {

                        // format: HH:MM
                        String formatted = String.format(Locale.getDefault(),
                                "%02d:%02d", h, m);

                        timeInput.setText(formatted);
                    }, hour, minute, true);

            timePicker.show();
        });

        //dialog popup
        //this creates a popup for the user to enter a reminder
        AlertDialog reminderDialog=new AlertDialog.Builder(context)
                .setTitle("Add Reminder")
                .setView(view)
                //from here, if the user presses add, the reminder is inserted into the viewmodel
                //else nothing gets returned
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();
        reminderDialog.show();

        reminderDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String t = title.getText().toString().trim();
                String d = dateInput.getText().toString();
                String ti = timeInput.getText().toString();

                if (!t.isEmpty() && !d.isEmpty() && !ti.isEmpty()) {
                    Reminder r = new Reminder(t, d, ti, false, false);
                    viewModel.insert(r);
                    reminderDialog.dismiss();
                } else {
                    if (t.isEmpty()) {
                        title.setError("Enter a title");
                    }

                    if (d.isEmpty()) {
                        dateInput.setError("Enter a date");
                    }

                    if (ti.isEmpty()) {
                        timeInput.setError("Enter a time");
                    }

                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}