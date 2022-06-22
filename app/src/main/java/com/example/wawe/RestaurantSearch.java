package com.example.wawe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantSearch {

    @SerializedName("businesses")
    public List<Restaurant> restaurants;

}
