package com.example.wawe.YelpClasses;

import com.example.wawe.ParseModels.Restaurant;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.parceler.Parcel;
import java.util.List;
import java.util.Objects;

@Parcel
public class YelpRestaurant  {

    YelpRestaurant () {}

    @SerializedName("name")
    String name;

    @SerializedName("price")
    String price;

    @SerializedName("rating")
    double rating;

    @SerializedName("distance")
    double distanceMeters;

    @SerializedName("image_url")
    String restaurantImage;

    @SerializedName("categories")
    List<RestaurantCategories> category;

    @SerializedName("location")
    RestaurantLocation location;

    @SerializedName("id")
    String id;

    @SerializedName("coordinates")
    RestaurantCoordinates coordinates;

    public YelpRestaurant (String name, String price, double rating, double distanceMeters, String restaurantImage, List<RestaurantCategories> category, RestaurantLocation location, String id, RestaurantCoordinates coordinates) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.distanceMeters = distanceMeters;
        this.restaurantImage = restaurantImage;
        this.category = category;
        this.location = location;
        this.id = id;
        this.coordinates = coordinates;
    }

    public YelpRestaurant (Restaurant restaurant) {
        this.name = restaurant.getKeyName();
        this.price = restaurant.getKeyPrice();
        this.rating = restaurant.getKeyRating();
        this.restaurantImage = restaurant.getKeyImage();
        this.id = restaurant.getKeyId();
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

    public String getId() {
        return id;
    }

    public RestaurantLocation getLocation() {
        return location;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public RestaurantCoordinates getCoordinates() {
        return coordinates;
    }

    public int getDistanceInMiles () {
        double milesPerMeter = 0.000621371;
        return (int) (distanceMeters * milesPerMeter);
    }
}
