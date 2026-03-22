package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eecs4443project.data.entity.User;
import com.example.eecs4443project.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repo;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
    }

    public LiveData<Integer> login(String username, String password) {
        return repo.login(username, password);
    }

    public void register(User user) {
        repo.register(user);
    }
}
