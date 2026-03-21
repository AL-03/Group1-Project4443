package com.example.eecs4443project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private ArrayList<Reminder> reminders;

    public ReminderAdapter(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.title.setText(reminder.getTitle());
        holder.date.setText(reminder.getDate());
        holder.time.setText(reminder.getTime());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, time;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reminderTitle);
            date = itemView.findViewById(R.id.reminderDate);
            time = itemView.findViewById(R.id.reminderTime);
        }
    }
}