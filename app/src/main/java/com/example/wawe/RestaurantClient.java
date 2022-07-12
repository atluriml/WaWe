package com.example.wawe;


import static com.example.wawe.fragments.RouletteFragment.REST_APPLICATION_ID;

import com.example.wawe.YelpClasses.YelpRestaurant;
import com.example.wawe.YelpClasses.RestaurantSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

// this class makes calls to the yelp server
public interface RestaurantClient  {

    // if the user uses all of the filters
    @GET("businesses/search")
    default Call<RestaurantSearch> searchRestaurants(@Header("Authorization") String authToken, @Query("categories") String cuisine, @Query("categories") String dietaryRestriction, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius, @Query("price") String price, @Query("limit") int numRestaurants){
        Call<RestaurantSearch> call = null;
        if (dietaryRestriction.equals("") && price.equals("0") && cuisine.equals("")){ // just radius or none
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , latitude, longitude, radius, 50);
        }
        else if (!dietaryRestriction.equals("") && price.equals("0") && cuisine.equals("")){ // radius & dr or just dr
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , dietaryRestriction, latitude, longitude, radius, 50);
        }
        else if (dietaryRestriction.equals("") && price.equals("0") && !cuisine.equals("")){ // radius & cuisine or just cuisine
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , cuisine, latitude, longitude, radius, 50);
        }
        else if (dietaryRestriction.equals("") && !price.equals("0") && cuisine.equals("")){ // radius & price or just price
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , latitude, longitude, radius, price, 50);
        }
        else if (!dietaryRestriction.equals("") && !price.equals("0") && cuisine.equals("")){ // radius, price, dr or just price & dr
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , dietaryRestriction, latitude, longitude, radius, price, 50);
        }
        else if (dietaryRestriction.equals("") && !price.equals("0") && !cuisine.equals("")){ // radius, price, cuisine or just price & cuisine
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , cuisine, latitude, longitude, radius, price, 50);
        }
        else if (!dietaryRestriction.equals("") && price.equals("0") && !cuisine.equals("")){ // radius, dr, cuisine or just dr & cuisine
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , cuisine, dietaryRestriction , latitude, longitude, radius, 50);
        }
        else if (!dietaryRestriction.equals("") && !price.equals("0") && !cuisine.equals("")){ // price, dr, cuisine or all filters
            call = searchRestaurants("Bearer " + REST_APPLICATION_ID , cuisine, dietaryRestriction, latitude, longitude, radius, price, 50);
        }
        return call;
    }


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
