package com.example.proje.Repository;

import android.content.Context;

import com.example.proje.DB.CarDatabaseHelper;
import com.example.proje.Model.Car;

import java.util.List;

public class CarRepository {
    private CarDatabaseHelper dbHelper;

    public CarRepository(Context context) {
        dbHelper = new CarDatabaseHelper(context);
    }

    public void insertCar(Car car){
        dbHelper.addCar(car);
    }

    public void updateCar(Car car) {
        dbHelper.updateCar(car);
    }

    public List<Car> getAllCars(){
        return dbHelper.getAllCars();
    }

    public List<Car> getCarsByUserId(String userId) {
        return dbHelper.getCarsByUserId(userId);
    }

    public void deleteCar(Car car){
        dbHelper.deleteCar(car);
    }
}
