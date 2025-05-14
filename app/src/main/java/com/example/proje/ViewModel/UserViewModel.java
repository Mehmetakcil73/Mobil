package com.example.proje.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proje.Model.User;
import com.example.proje.Repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public boolean register(String username, String password, String email) {
        User user = new User(username, password, email);
        return userRepository.registerUser(user);
    }

    public boolean login(String username, String password) {
        return userRepository.loginUser(username, password);
    }

    public void logout() {
        userRepository.logoutUser();
    }

    public boolean isLoggedIn() {
        return userRepository.isUserLoggedIn();
    }

    public LiveData<User> getUser(String username) {
        return userRepository.getUser(username);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
}
