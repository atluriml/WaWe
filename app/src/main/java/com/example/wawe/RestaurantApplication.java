package com.example.wawe;

import android.app.Application;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Headers;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantApplication extends Application {

    public static final String BASE_URL = "https://api.yelp.com/v3";
    public static final String REST_APPLICATION_ID = BuildConfig.YELP_APPLICATION_ID;
    public static final String REST_CLIENT_ID = BuildConfig.YELP_CLIENT_ID;
  //  public static final Headers HEADER = {"Authorization": REST_APPLICATION_ID};
    public static final String TAG = "RestaurantApplication";


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
           // http.setRequestProperty("Authorization", REST_APPLICATION_ID);
            http.setRequestProperty("Authorization", "Bearer {" + REST_APPLICATION_ID + "}");
            Log.i(TAG, "Success with bearer authentication");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RestaurantClient y = retrofit.create(RestaurantClient.class);
//        y.searchRestaurants("pasta", "San Jose").enqueue(Callback<>);
    }
}
