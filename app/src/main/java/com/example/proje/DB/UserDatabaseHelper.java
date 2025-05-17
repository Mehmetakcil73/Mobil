package com.example.proje.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proje.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_database.db";
    private static final int DATABASE_VERSION = 2;
    private final Context context;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_IS_ADMIN = "is_admin";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_IS_ADMIN + " INTEGER DEFAULT 0)";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_IS_ADMIN + " INTEGER DEFAULT 0");
        }
    }

    public boolean insertUser(User user) {
        if (isUserExists(user.getUsername())) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    private boolean isUserExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1;
            cursor.close();
            return new User(username, password, email, isAdmin);
        }

        return null;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0);

        // Kullanıcı bilgilerini güncelle
        db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", 
                 new String[]{user.getOldUsername()});
        db.close();

        // İlanlardaki kullanıcı adını güncelle
        CarDatabaseHelper carDbHelper = new CarDatabaseHelper(context);
        carDbHelper.updateUserIdInCars(user.getOldUsername(), user.getUsername());
    }

    public LiveData<User> getUserLiveData(String username) {
        final MutableLiveData<User> userLiveData = new MutableLiveData<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1;
            cursor.close();
            userLiveData.setValue(new User(username, password, email, isAdmin));
        } else {
            userLiveData.setValue(null);
        }

        db.close();
        return userLiveData;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1;
                users.add(new User(username, password, email, isAdmin));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    private Context getContext() {
        return context;
    }
}
