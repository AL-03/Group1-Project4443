package com.example.eecs4443project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Reminder;
import com.example.eecs4443project.viewmodel.ReminderViewModel;

import java.util.Calendar;
import java.util.Locale;

public class AddReminderDialog {

    public static void show(Context context, ReminderViewModel viewModel) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_reminder, null);

        EditText title = view.findViewById(R.id.inputTitle);
        EditText dateInput = view.findViewById(R.id.inputDate);
        EditText timeInput = view.findViewById(R.id.inputTime);

        Calendar calendar = Calendar.getInstance();

        //date picker to pick reminder date
        dateInput.setOnClickListener(v -> {

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(context,
                    (view1, y, m, d) -> {

                        // format: YYYY-MM-DD
                        String formatted = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", y, m + 1, d);

                        dateInput.setText(formatted);
                    }, year, month, day);

            datePicker.show();
        });

        //time picker for due time
        timeInput.setOnClickListener(v -> {

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

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
        new AlertDialog.Builder(context)
                .setTitle("Add Reminder")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {

                    String t = title.getText().toString().trim();
                    String d = dateInput.getText().toString();
                    String ti = timeInput.getText().toString();

                    if (!t.isEmpty() && !d.isEmpty() && !ti.isEmpty()) {
                        Reminder r = new Reminder(t, d, ti, false, false);
                        viewModel.insert(r);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}