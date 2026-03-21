package com.example.eecs4443project;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HabitDashboardAdapter extends RecyclerView.Adapter<HabitDashboardAdapter.ViewHolder> {

    Context context;
    ArrayList<Habit> habits;
    DatabaseHelper db;

    public HabitDashboardAdapter(Context context, ArrayList<Habit> habits) {
        this.context = context;
        this.habits = habits;
        db = new DatabaseHelper(context);
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

        holder.title.setText(habit.title);
        holder.progress.setProgress(70);

        holder.star.setImageResource(
                habit.starred == 1 ?
                        android.R.drawable.btn_star_big_on :
                        android.R.drawable.btn_star_big_off
        );

        // star Toggle
        holder.star.setOnClickListener(v -> {
            int newVal = habit.starred == 1 ? 0 : 1;
            habit.starred = newVal;

            SQLiteDatabase dbWrite = db.getWritableDatabase();
            dbWrite.execSQL("UPDATE habits SET starred=? WHERE id=?",
                    new Object[]{newVal, habit.id});

            notifyItemChanged(position);
        });

        // click to get the detail view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HabitDetailActivity.class);
            intent.putExtra("title", habit.title);
            intent.putExtra("desc", habit.description);
            context.startActivity(intent);
        });

        // long click to delete the habit
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Habit")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (d, i) -> {
                        db.getWritableDatabase().execSQL(
                                "DELETE FROM habits WHERE id=?",
                                new Object[]{habit.id}
                        );
                        habits.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}