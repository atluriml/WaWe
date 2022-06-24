package com.example.wawe.restaurantClasses;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Restaurant {

    Restaurant () {}

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("rating")
    private double rating;

    @SerializedName("distance")
    private double distanceMeters;

    @SerializedName("image_url")
    private String restaurantImage;

    @SerializedName("categories")
    List<RestaurantCategories> category;

    @SerializedName("location")
    private RestaurantLocation location;


    public Restaurant (String name, String price, double rating, double distanceMeters, String restaurantImage, List<RestaurantCategories> category, RestaurantLocation location) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.distanceMeters = distanceMeters;
        this.restaurantImage = restaurantImage;
        this.category = category;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public List<RestaurantCategories> getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public double getRating() {
        return rating;
    }

    public RestaurantLocation getLocation() {
        return location;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public String displayDistance () {
        double milesPerMeter = 0.000621371;
        @SuppressLint("DefaultLocale") String distanceInMiles = String.format("%.02f", distanceMeters * milesPerMeter);
        return distanceInMiles + "mi";
    }
}
