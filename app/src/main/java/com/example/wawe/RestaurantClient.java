package com.example.wawe;


import com.example.wawe.restaurantClasses.YelpRestaurant;
import com.example.wawe.restaurantClasses.RestaurantSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

// this class makes calls to the yelp server
public interface RestaurantClient  {

    // if the user uses all of the filters
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("categories") String dietaryRestriction, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("price") String price, @Query("limit") int numRestaurants);

    // if the user only applies three filters
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("categories") String dietaryRestriction, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("limit") int numRestaurants);
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisineOrDietaryRestriction, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("price") String price, @Query("limit") int numRestaurants);

    // if the user only applies two filters
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisineOrDietaryRestriction, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("limit") int numRestaurants);
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("price") String price, @Query("limit") int numRestaurants);

    // one filter only
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("limit") int numRestaurants);

    // used for restaurant list activity
    @GET("businesses/{id}")
    Call<YelpRestaurant> searchRestaurants(@Header("Authorization") String authToken, @Path("id") String id);

}
