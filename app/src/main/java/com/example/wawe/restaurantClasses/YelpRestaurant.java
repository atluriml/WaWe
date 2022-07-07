package com.example.wawe.restaurantClasses;

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;
import java.util.List;
import java.util.Objects;

@Parcel
public class YelpRestaurant  {

    YelpRestaurant () {}

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
    private List<RestaurantCategories> category;

    @SerializedName("location")
    private RestaurantLocation location;

    @SerializedName("id")
    private String id;

    @SerializedName("coordinates")
    private RestaurantCoordinates coordinates;

//    @SerializedName("hours")
//    List<RestaurantHours> hours;

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

    public String displayDistance () {
        double milesPerMeter = 0.000621371;
        Log.i("dist", String.valueOf(distanceMeters));
        @SuppressLint("DefaultLocale") String distanceInMiles = String.format("%.01f", getDistanceMeters() * milesPerMeter);
        return distanceInMiles + "mi";
    }



    public RestaurantCoordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", rating=" + rating +
                ", distanceMeters=" + distanceMeters +
                ", restaurantImage='" + restaurantImage + '\'' +
                ", category=" + category +
                ", location=" + location +
                ", id='[" + id + "]" + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YelpRestaurant that = (YelpRestaurant) o;
        return Objects.equals(id, that.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}
