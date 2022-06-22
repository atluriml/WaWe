package com.example.wawe;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface RestaurantClient  {

    @GET("businesses/categories")
    Call<RestaurantSearch> getCategories(@Header("Authorization") String authToken, @Query("categories") String categories);

    // if the user only uses one term and one location
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("categories") String dietaryRestriction, @Query("location") String location, @Query("price") String price);

    // if the user uses all of the filters
    @GET("businesses/search")
    Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("location") String location, @Query("price") String price);



}
