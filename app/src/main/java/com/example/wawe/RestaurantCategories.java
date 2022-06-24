package com.example.wawe;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RestaurantCategories {

    RestaurantCategories () {}

    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }
}
