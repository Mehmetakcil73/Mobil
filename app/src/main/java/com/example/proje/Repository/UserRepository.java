package com.example.proje.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.proje.DB.UserDatabaseHelper;
import com.example.proje.Model.User;
import com.example.proje.SessionManager.SessionManager;

import java.util.List;

public class UserRepository {

    private final UserDatabaseHelper dbHelper;
    private final SessionManager sessionManager;

    public UserRepository(Context context) {
        dbHelper = new UserDatabaseHelper(context);
        sessionManager = new SessionManager(context);
    }

    public boolean registerUser(User user) {
        return dbHelper.insertUser(user);
    }

    public User loginUser(String username, String password) {
        boolean isValid = dbHelper.checkUser(username, password);
        if (isValid) {
            User user = dbHelper.getUserByUsername(username);
            if (user != null) {
                sessionManager.setLogin(true);
                sessionManager.saveUsername(username);
                sessionManager.setAdmin(user.isAdmin());
                return user;
            }
        }
        return null;
    }

    public void logoutUser() {
        sessionManager.clearSession();
    }

    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public boolean isCurrentUserAdmin() {
        return sessionManager.isAdmin();
    }

    public LiveData<User> getUser(String username) {
        return dbHelper.getUserLiveData(username);
    }

    public void updateUser(User user) {
        dbHelper.updateUser(user);
    }

    public List<User> getAllUsers() {
        return dbHelper.getAllUsers();
    }

    public User getCurrentUser() {
        String username = sessionManager.getUsername();
        if (username != null) {
            return dbHelper.getUserByUsername(username);
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return dbHelper.getUserByUsername(username);
    }
}
