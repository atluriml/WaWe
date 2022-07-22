package com.example.wawe.YelpClasses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RestaurantSearch {

    RestaurantSearch () {}

    @SerializedName("businesses")
    private List<YelpRestaurant> restaurants;

    public List<YelpRestaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<YelpRestaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
