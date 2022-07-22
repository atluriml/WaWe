package com.example.wawe.RoomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.YelpClasses.YelpRestaurant;


@Entity
public class RestaurantRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    private String yelpId;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String price;
    @ColumnInfo
    private String image;
    @ColumnInfo
    private String address;
    @ColumnInfo
    private double rating;


    public RestaurantRoom() {
    }

    public RestaurantRoom(YelpRestaurant yelpRestaurant) {
        this.yelpId = yelpRestaurant.getId();
        this.name = yelpRestaurant.getName();
        this.price = yelpRestaurant.getPrice();
        this.image = yelpRestaurant.getRestaurantImage();
        this.address = yelpRestaurant.getLocation().getAddress();
        this.rating = yelpRestaurant.getRating();
    }

    public RestaurantRoom(Restaurant restaurant) {
        this.yelpId = restaurant.getKeyId();
        this.name = restaurant.getKeyName();
        this.price = restaurant.getKeyPrice();
        this.image = restaurant.getKeyImage();
        this.address = restaurant.getKeyAddress();
        this.rating = restaurant.getKeyRating();
    }

    @NonNull
    public String getYelpId() {
        return yelpId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public void setYelpId(@NonNull String yelpId) {
        this.yelpId = yelpId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
