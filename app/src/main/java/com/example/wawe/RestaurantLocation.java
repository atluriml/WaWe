package com.example.wawe;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RestaurantLocation {

    RestaurantLocation () {}

    @SerializedName("address1")
    private String address;

    public String getAddress() {
        return address;
    }
}
