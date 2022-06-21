package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.wawe.BuildConfig;
import com.example.wawe.R;
import com.example.wawe.RestaurantClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivity extends AppCompatActivity {


    public static final String BASE_URL = "https://api.yelp.com/v3";
    public static final String REST_APPLICATION_ID = BuildConfig.YELP_APPLICATION_ID;
    public static final String REST_CLIENT_ID = BuildConfig.YELP_CLIENT_ID;
    //  public static final Headers HEADER = {"Authorization": REST_APPLICATION_ID};
    public static final String TAG = "RestaurantActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RestaurantClient y = retrofit.create(RestaurantClient.class);
    }
}