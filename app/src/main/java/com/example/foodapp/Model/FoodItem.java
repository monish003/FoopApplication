package com.example.foodapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {
    private String name;
    private double price;
    private boolean isSelected;
    private String description;

    public FoodItem(String name, double price,String description) {
        this.name = name;
        this.price = price;
        this.isSelected = false;
        this.description = description;
    }

    protected FoodItem(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        isSelected = in.readByte() != 0;
        description = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(description);
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
