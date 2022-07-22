package com.example.wawe.YelpClasses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RestaurantCoordinates {

    RestaurantCoordinates () {}

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
