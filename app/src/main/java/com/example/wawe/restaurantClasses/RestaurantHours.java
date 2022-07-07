package com.example.wawe.restaurantClasses;

import com.google.gson.annotations.SerializedName;

public class RestaurantHours {

    RestaurantHours () {}

    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }

}
