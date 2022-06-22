package com.example.wawe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

//    @SerializedName("location")
//    private String location;

//    @SerializedName("radius")
//    private int radius;

    @SerializedName("categories")
    private List<RestaurantCategories> category;


    public Restaurant (String name, String price, List<RestaurantCategories> category) {
        this.name = name;
        this.price = price;
//        this.location = location;
//        this.radius = radius;
        this.category = category;
    }

//    public int getRadius() {
//        return radius;
//    }

    public List<RestaurantCategories> getCategory() {
        return category;
    }

//    public String getLocation() {
//        return location;
//    }

    public String getPrice() {
        return price;
    }


}
