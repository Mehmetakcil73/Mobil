package com.example.proje.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.proje.DB.UserDatabaseHelper;
import com.example.proje.Model.User;
import com.example.proje.SessionManager.SessionManager;

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

    public boolean loginUser(String username, String password) {
        boolean isValid = dbHelper.checkUser(username, password);
        if (isValid) {
            sessionManager.setLogin(true);
        }
        return isValid;
    }

    public void logoutUser() {
        sessionManager.logout();
    }

    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public LiveData<User> getUser(String username) {
        return dbHelper.getUserLiveData(username);
    }

    public void updateUser(User user) {
        dbHelper.updateUser(user);
    }
}
