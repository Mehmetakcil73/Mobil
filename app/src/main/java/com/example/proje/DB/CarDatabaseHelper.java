package com.example.proje.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.proje.Model.Car;

import java.util.ArrayList;
import java.util.List;

public class CarDatabaseHelper extends SQLiteOpenHelper {
    //Veritabani tanimlamalari
    private static final String DATABASE_NAME = "car_database.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "cars";

    //Cars tablosundaki kolonlar icin tanimlamalar
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_USER_ID = "user_id";

    public CarDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BRAND + " TEXT, "
                + COLUMN_MODEL + " TEXT, "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_USER_ID + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_USER_ID + " TEXT");
        }
    }

    public void addCar(Car car){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND, car.getBrand());
        values.put(COLUMN_MODEL, car.getModel());
        values.put(COLUMN_YEAR, car.getYear());
        values.put(COLUMN_PRICE, car.getPrice());
        values.put(COLUMN_USER_ID, car.getUserId());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND, car.getBrand());
        values.put(COLUMN_MODEL, car.getModel());
        values.put(COLUMN_YEAR, car.getYear());
        values.put(COLUMN_PRICE, car.getPrice());
        values.put(COLUMN_USER_ID, car.getUserId());
        
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", 
                new String[]{String.valueOf(car.getId())});
        db.close();
    }

    public List<Car> getAllCars(){
        List<Car> carList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                Car car = new Car(id, brand, model, year, price, userId);
                carList.add(car);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return carList;
    }

    public List<Car> getCarsByUserId(String userId) {
        List<Car> carList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_USER_ID + " = ?",
                new String[]{userId},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                Car car = new Car(id, brand, model, year, price, userId);
                carList.add(car);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return carList;
    }

    public void deleteCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(car.getId())});
        db.close();
    }

    public void updateUserIdInCars(String oldUserId, String newUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, newUserId);
        
        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", 
                new String[]{oldUserId});
        db.close();
    }
}
