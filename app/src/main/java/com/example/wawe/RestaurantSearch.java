package com.example.wawe;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RestaurantSearch {

    RestaurantSearch () {}

    @SerializedName("businesses")
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
