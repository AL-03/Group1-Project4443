package com.example.eecs4443project.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.view.activities.HabitDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HabitDashboardAdapter extends RecyclerView.Adapter<HabitDashboardAdapter.ViewHolder> {

    Context context;
    List<Habit> habits = new ArrayList<>();
    // Ensures adapter (view) doesn't directly interact with the data (model)
    HabitClickListener listener;

    // Implemented by HabitsActivity
    public interface HabitClickListener {
        void onHabitClicked(Habit habit);
        void onHabitLongPressed(Habit habit);
        void onStarToggled(Habit habit);

        // Adding the onAddNewHabitClicked(Button addNewHabit
    }

    public HabitDashboardAdapter(Context context, HabitClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ProgressBar progress;
        ImageView star;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.habitTitle);
            progress = v.findViewById(R.id.habitProgress);
            star = v.findViewById(R.id.starIcon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.habit_dashboard_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Habit habit = habits.get(position);

        holder.title.setText(habit.getTitle());

        holder.progress.setProgress(habit.getProgress());

        holder.star.setImageDrawable(null);

        if (habit.getStarred()==1) {
            holder.star.setImageResource(R.drawable.baseline_favorite_24);
        } else{
            holder.star.setImageResource(R.drawable.baseline_favorite_border_24);
        }



        // star Toggle
        holder.star.setOnClickListener(v -> listener.onStarToggled(habit));

        // click to get the detail view
        holder.itemView.setOnClickListener(v -> listener.onHabitClicked(habit));

        // long click to delete the habit
        holder.itemView.setOnLongClickListener(v -> {
            listener.onHabitLongPressed(habit);
            return true;
        });

        //Log.d("STAR_DEBUG", habit.getTitle() + " starred=" + habit.getStarred());
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}