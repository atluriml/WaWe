package com.example.wawe.YelpClasses;

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

    @Override
    public String toString() {
        return "RestaurantCategories{" +
                "title='" + title + '\'' +
                '}';
    }
}
