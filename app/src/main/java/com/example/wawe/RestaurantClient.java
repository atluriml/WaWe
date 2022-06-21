package com.example.wawe;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface RestaurantClient  {


    @GET("authorize")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);

    @GET("businesses/search")
    Call<ResponseBody> searchRestaurants(@Header("Authorization") String authToken, @Query("term") String keyword, @Query("location") String location);

}
