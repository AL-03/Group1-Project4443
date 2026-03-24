package com.example.eecs4443project.view.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<Reminder> reminders = new ArrayList<>();
    private OnReminderActionListener listener;

    // interface for on reminder
    public interface OnReminderActionListener {
        void onReminderChecked(Reminder reminder);
        void onReminderLongPressed(Reminder reminder);
    }

    // constructor with listener
    public ReminderAdapter(OnReminderActionListener listener) {
        this.listener = listener;
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

        // set checkbox state
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(reminder.isCompleted());

        //put a line through text if marked as completed
        if (reminder.isCompleted()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // checkbox onclick listener
        holder.checkBox.setOnClickListener(v -> {
            boolean newState = !reminder.isCompleted();
            holder.checkBox.setChecked(newState);
            if (listener != null) {
                listener.onReminderChecked(reminder);
            }
        });

        // long press reminder to delete
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onReminderLongPressed(reminder);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, time;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reminderTitle);
            date = itemView.findViewById(R.id.reminderDate);
            time = itemView.findViewById(R.id.reminderTime);
            checkBox = itemView.findViewById(R.id.reminderCheckBox);
        }
    }
}