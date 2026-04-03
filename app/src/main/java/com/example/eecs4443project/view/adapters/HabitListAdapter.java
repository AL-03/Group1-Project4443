package com.example.eecs4443project.view.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443project.R;
import com.example.eecs4443project.data.entity.Habit;

import java.util.ArrayList;
import java.util.List;
public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.ViewHolder> {


    private List<Habit> habits = new ArrayList<>();
    // Ensures adapter (view) doesn't directly interact with the data (model)
    private final onHabitSelectedListener listener;

    public interface onHabitSelectedListener {
        void onHabitSelected(Habit habit);
    }

    public HabitListAdapter(onHabitSelectedListener listener) {
        this.listener = listener;
    }

    // Sets the habits as a list of habits
    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.habitListTitle);
        }
    }

    // Inflates the recyclerview with different items than the recyclerview in HabitsActivity
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);

        holder.title.setText(habit.getTitle());


        // colour shift and make sure they cannot select something that is already selected
        // The alpha value of 0.5 sets selected habits as grey, and their onClickListener is nullified
        // Meanwhile, the ones that have yet to be selected still have a listener and the full opacity of the text
        if (!habit.getIsSelected()) {
            holder.itemView.setAlpha(1f);
            holder.itemView.setOnClickListener(v -> listener.onHabitSelected(habit));

        }
        else {
            holder.itemView.setAlpha(0.5f);
            holder.itemView.setOnClickListener(null);
        }
    }


    @Override
    public int getItemCount() {
        return habits.size();
    }



}
