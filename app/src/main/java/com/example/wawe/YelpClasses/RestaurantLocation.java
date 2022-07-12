package com.example.wawe.YelpClasses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RestaurantLocation {

    RestaurantLocation () {}

    @SerializedName("address1")
    private String address;

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "RestaurantLocation{" +
                "address='" + address + '\'' +
                '}';
    }
}
