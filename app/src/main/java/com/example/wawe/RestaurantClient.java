package com.example.wawe;


import com.example.wawe.restaurantClasses.Restaurant;
import com.example.wawe.restaurantClasses.RestaurantSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestaurantClient  {

    // if the user only uses one term and one location
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("categories") String dietaryRestriction, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("price") String price, @Query("limit") int numRestaurants);

    // if the user uses all of the filters
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("location") String location, @Query("price") String price);

    // used for visited and favorites activity
    @GET("businesses/{id}")
    Call<Restaurant> searchRestaurants(@Header("Authorization") String authToken, @Path("id") String id);





}
