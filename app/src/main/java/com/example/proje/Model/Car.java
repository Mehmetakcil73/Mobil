package com.example.proje.Model;

public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String userId;

    public Car() {
    }

    public Car(int id, String brand, String model, int year, double price, String userId) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.userId = userId;
    }

    public Car(String brand, String model, int year, double price, String userId) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
