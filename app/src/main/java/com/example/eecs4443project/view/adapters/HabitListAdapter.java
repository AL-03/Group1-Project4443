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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.view.activities.HabitDetailActivity;

import java.util.ArrayList;
import java.util.List;
public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.ViewHolder> {

    //Context context;
    private List<Habit> habits = new ArrayList<>();
    // Ensures adapter (view) doesn't directly interact with the data (model)
    //private List<Integer> selectedHabits = new ArrayList<>();
    private List<Habit> selHabits = new ArrayList<>();
    private final onHabitSelectedListener listener;

    public interface onHabitSelectedListener {
        void onHabitSelected(Habit habit);
    }

    public HabitListAdapter(onHabitSelectedListener listener) {
       // this.context = context;
        this.listener = listener;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    /*
    public void setSelectedHabits(List<Integer> selectedHabits) {
        this.selectedHabits = selectedHabits;
        notifyDataSetChanged();
    }*/

    public void setSelectedHabits(List<Habit> selHabits) {
        this.selHabits = selHabits;
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.habitListTitle);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item, parent, false);
        return new ViewHolder(view);
        /*
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.habit_list_item, parent, false));

        */
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);

        holder.title.setText(habit.getTitle());

        // colour shift and make sure they cannot select something that is already selected
        if (habit.getIsSelected()) {
            holder.itemView.setAlpha(0.5f);
            holder.itemView.setClickable(false);
        }
        else {
            holder.itemView.setAlpha(1f);
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(v -> listener.onHabitSelected(habit));
        }
    }


    @Override
    public int getItemCount() {
        return habits.size();
    }



}
