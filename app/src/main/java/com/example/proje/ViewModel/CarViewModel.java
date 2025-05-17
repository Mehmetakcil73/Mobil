package com.example.proje.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proje.Model.Car;
import com.example.proje.Repository.CarRepository;

import java.util.List;

public class CarViewModel extends AndroidViewModel {
    private final CarRepository carRepository;
    private final MutableLiveData<List<Car>> carsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Car>> userCarsLiveData = new MutableLiveData<>();
    
    public CarViewModel(@NonNull Application application) {
        super(application);
        carRepository = new CarRepository(application);
        loadCars();
    }

    public LiveData<List<Car>> getCars(){
        return carsLiveData;
    }

    public LiveData<List<Car>> getUserCars(String userId) {
        List<Car> userCarList = carRepository.getCarsByUserId(userId);
        userCarsLiveData.setValue(userCarList);
        return userCarsLiveData;
    }

    public void addCar(Car car){
        carRepository.insertCar(car);
        loadCars();
        loadUserCars(car.getUserId());
    }

    public void updateCar(Car car) {
        carRepository.updateCar(car);
        loadCars();
        loadUserCars(car.getUserId());
    }

    public void deleteCar(Car car){
        carRepository.deleteCar(car);
        loadCars();
        loadUserCars(car.getUserId());
    }

    private void loadCars(){
        List<Car> carList = carRepository.getAllCars();
        carsLiveData.setValue(carList);
    }

    private void loadUserCars(String userId) {
        List<Car> userCarList = carRepository.getCarsByUserId(userId);
        userCarsLiveData.setValue(userCarList);
    }
}
