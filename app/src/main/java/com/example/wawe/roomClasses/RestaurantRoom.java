package com.example.wawe.roomClasses;

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
    public String yelpId;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String price;
    @ColumnInfo
    public String image;
    @ColumnInfo
    public String address;
    @ColumnInfo
    public double rating;


    public RestaurantRoom() {
    }

    public RestaurantRoom(YelpRestaurant yelpRestaurant) {
        this.name = yelpRestaurant.getName();
        this.price = yelpRestaurant.getPrice();
        this.yelpId = yelpRestaurant.getId();
        this.rating = yelpRestaurant.getRating();
        this.image = yelpRestaurant.getRestaurantImage();
        this.address = yelpRestaurant.getLocation().getAddress();
    }

    public RestaurantRoom(Restaurant restaurant) {
        this.name = restaurant.getKeyName();
        this.price = restaurant.getKeyPrice();
        this.yelpId = restaurant.getKeyId();
        this.rating = restaurant.getKeyRating();
        this.image = restaurant.getKeyImage();
        this.address = restaurant.getKeyAddress();
    }


}
