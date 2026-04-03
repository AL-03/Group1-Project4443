package com.example.eecs4443project.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.AppDatabase;
import com.example.eecs4443project.data.dao.HabitDao;
import com.example.eecs4443project.data.dao.SelectedHabitDao;
import com.example.eecs4443project.data.entity.Habit;
import com.example.eecs4443project.data.entity.SelectedHabit;

import java.util.List;
import java.util.concurrent.Executors;

public class HabitRepository {
    private HabitDao habitDao;

    private SelectedHabitDao selHabitDao;
    private LiveData<List<Habit>> allHabits;

    public HabitRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        habitDao = db.habitDao();
        selHabitDao = db.selHabitDao();
        allHabits = habitDao.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    // Adding the ability to search for some of the habits for thew NewHabitsActivity
    public LiveData<List<Habit>> getSelectedHabits() {
        return habitDao.getSelectedHabits();
    }

    // Need to filter the habits when searching
    public LiveData<List<Habit>> getFilteredHabits(String habitQuery) {
        return habitDao.getFilteredHabits(habitQuery);
    }


    // specific to the habit class - need to remove the delete feature
    public void insert(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.insertHabit(habit));
    }

    public void update(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.updateHabit(habit));
    }

    public void delete(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.deleteHabit(habit));
    }

    public void updateStar(int id, int newVal){
        Executors.newSingleThreadExecutor().execute(() -> {
            habitDao.updateStar(id, newVal);
        });
    }

    // SelectHabit methods here

    /*
    public void selectHabit(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> selHabitDao.insertSelectedHabit(new SelectedHabit(habit.getId())));
    }

    public void unselectHabit(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> selHabitDao.unselectHabit(habit.getId()));
    }
    */
    public void selectHabit(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
           habit.setIsSelected(true);
           habitDao.updateHabit(habit);
        });
    }

    // For the HabitsActivity, when a user decides to "delete" their habit,
    // it should really just become unselected and have new default values
    public void unselectHabit(Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            habit.setIsSelected(false);
            /*
            habit.setDescription("");
            habit.setProgress(0);
            habit.setStarred(0);
            */
            habitDao.updateHabit(habit);
        });
    }
    /*
    public LiveData<List<Habit>> getSelectedHabits() {
        return selHabitDao.getSelectedHabits();
    }*/

    public LiveData<List<Integer>> getSelectedHabitIds() {
        return selHabitDao.getSelectedHabitIds();
    }


    // Temporary dummy data - this is actually the full list of habits, but only a few will be added to the selected habits for the user initially
    public void insertDummyHabits() {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            if(habitDao.getCount()==0) {
                /* Original settings for each habit
                Habit drinkWater = new Habit("Drink Water", "Stay hydrated", 1, 10);
                Habit workout = new Habit("Workout", "Exercise daily", 0, 50);
                Habit read = new Habit("Read Book", "Read 20 pages", 1, 20);
                Habit meditate = new Habit("Meditate", "10 minutes daily", 0, 100);
                Habit study = new Habit("Study Algorithms", "Practice CLRS problems", 1, 60);
                Habit finishApp = new Habit("Finish App", "Finish working on the wellness app", 1, 30);
                Habit makeDinner = new Habit("Make Dinner", "Cook dinner and clean up after", 0, 0);
                Habit draw = new Habit("Draw", "Draw something every day", 0, 0);
                 */
                Habit drinkWater = new Habit("Drink Water", "Stay hydrated", 0, 0);
                Habit workout = new Habit("Workout", "Exercise daily", 0, 0);
                Habit read = new Habit("Read Book", "Read 20 pages", 0, 0);
                Habit meditate = new Habit("Meditate", "10 minutes daily", 0, 0);
                Habit study = new Habit("Study Algorithms", "Practice CLRS problems", 1, 60);
                Habit finishApp = new Habit("Finish App", "Finish working on the wellness app", 1, 30);
                Habit makeDinner = new Habit("Make Dinner", "Cook dinner and clean up after", 0, 0);
                Habit draw = new Habit("Draw", "Draw something every day", 0, 0);


                // do not add to selected list initially
                habitDao.insertHabit(drinkWater);

                // do not add to selected list initially
                habitDao.insertHabit(workout);

                // do not add to selected list initially
                habitDao.insertHabit(read);

                // do not add to selected list initially
                habitDao.insertHabit(draw);

                // do not add to selected list
                habitDao.insertHabit(meditate);

                // add this to the selected list for users to see in their dashboard
                habitDao.insertHabit(study);
                habitDao.updateIsSelected(study.getId(), true);
                //selHabitDao.insertSelectedHabit(new SelectedHabit(habitDao.getHabit(study.getId()).getId()));
                //selHabitDao.insertSelectedHabit(new SelectedHabit(study.getId()));


                // add this to the selected list for users to see in their dashboard
                habitDao.insertHabit(finishApp);
                habitDao.updateIsSelected(finishApp.getId(), true);
                //selHabitDao.insertSelectedHabit(new SelectedHabit(finishApp.getId()));
                //selHabitDao.insertSelectedHabit(new SelectedHabit(habitDao.getHabit(finishApp.getId()).getId()));

                // add this to the selected list for users to see in their dashboard
                habitDao.insertHabit(makeDinner);
                habitDao.updateIsSelected(makeDinner.getId(), true);
                //selHabitDao.insertSelectedHabit(new SelectedHabit(makeDinner.getId()));
                //selHabitDao.insertSelectedHabit(new SelectedHabit(habitDao.getHabit(makeDinner.getId()).getId()));
            }
        });
    }
}
