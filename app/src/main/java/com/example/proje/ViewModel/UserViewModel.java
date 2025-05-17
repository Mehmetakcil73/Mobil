package com.example.proje.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proje.Model.User;
import com.example.proje.Repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<List<User>> allUsersLiveData = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public boolean register(String username, String password, String email) {
        User user = new User(username, password, email);
        return userRepository.registerUser(user);
    }

    public boolean registerAdmin(String username, String password, String email) {
        User user = new User(username, password, email, true);
        return userRepository.registerUser(user);
    }

    public User login(String username, String password) {
        return userRepository.loginUser(username, password);
    }

    public void logout() {
        userRepository.logoutUser();
    }

    public boolean isLoggedIn() {
        return userRepository.isUserLoggedIn();
    }

    public boolean isCurrentUserAdmin() {
        return userRepository.isCurrentUserAdmin();
    }

    public LiveData<User> getUser(String username) {
        return userRepository.getUser(username);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
        loadAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        loadAllUsers();
        return allUsersLiveData;
    }

    private void loadAllUsers() {
        List<User> users = userRepository.getAllUsers();
        allUsersLiveData.setValue(users);
    }

    public User getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}
