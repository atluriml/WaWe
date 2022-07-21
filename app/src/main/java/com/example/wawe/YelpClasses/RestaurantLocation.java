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

    public void setAddress(String address) {
        this.address = address;
    }
}
