package com.example.eecs4443project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.eecs4443project.data.entity.User;
import com.example.eecs4443project.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void register(User user) {
        repository.register(user);
    }

    public User getUser(String username, String password) {
        return repository.getUser(username, password);
    }
}