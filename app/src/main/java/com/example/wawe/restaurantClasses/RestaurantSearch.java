package com.example.wawe.restaurantClasses;

import com.example.wawe.restaurantClasses.YelpRestaurant;
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

    @Override
    public String toString() {
        return "RestaurantSearch{" +
                "restaurants=" + restaurants +
                '}';
    }
}
