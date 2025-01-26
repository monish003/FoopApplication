package com.example.foodapp.Model;

import java.io.Serializable;

public class kitchen implements Serializable {
    private String name;
    private String address;
    private int id;
    private int price;
    private boolean isExpanded;
    private double lunchPrice;
    private double dinnerPrice;
    private String date;


    public kitchen(String name, String address, int id, int price, double lunchPrice, double dinnerPrice, String date) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.price = price;
        this.lunchPrice = lunchPrice;
        this.dinnerPrice = dinnerPrice;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public double getLunchPrice() {
        return lunchPrice;
    }

    public void setLunchPrice(double lunchPrice) {
        this.lunchPrice = lunchPrice;
    }

    public double getDinnerPrice() {
        return dinnerPrice;
    }

    public void setDinnerPrice(double dinnerPrice) {
        this.dinnerPrice = dinnerPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
