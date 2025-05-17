package com.example.proje.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_ADMIN = "is_admin";
    private static final String KEY_IS_GUEST = "is_guest";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        if (!isLoggedIn) {
            editor.putBoolean(KEY_IS_GUEST, false);
        }
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setGuest(boolean isGuest) {
        editor.putBoolean(KEY_IS_GUEST, isGuest);
        editor.apply();
    }

    public boolean isGuest() {
        return prefs.getBoolean(KEY_IS_GUEST, false);
    }

    public void setAdmin(boolean isAdmin) {
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.apply();
    }

    public boolean isAdmin() {
        return prefs.getBoolean(KEY_IS_ADMIN, false);
    }

    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
