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
    public CarViewModel(@NonNull Application application) {
        super(application);
        carRepository = new CarRepository(application);
        loadCars();
    }

    public LiveData<List<Car>> getCars(){
        return carsLiveData;
    }

    public void addCar(Car car){
        carRepository.insertCar(car);
        loadCars();
    }

    public void deleteCar(Car car){
        carRepository.deleteCar(car);
        loadCars();
    }

    private void loadCars(){
        List<Car> carList = carRepository.getAllCars();
        carsLiveData.setValue(carList);
    }
}
