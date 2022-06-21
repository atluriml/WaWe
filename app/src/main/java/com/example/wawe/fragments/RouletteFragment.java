package com.example.wawe.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wawe.Activities.FavoritesActivity;
import com.example.wawe.Activities.RestaurantActivity;
import com.example.wawe.BuildConfig;
import com.example.wawe.R;
import com.example.wawe.RestaurantClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RouletteFragment extends Fragment {

    public static final String TAG = "RouletteFragment";

    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String REST_APPLICATION_ID = BuildConfig.YELP_APPLICATION_ID;
    public static final String REST_CLIENT_ID = BuildConfig.YELP_CLIENT_ID;
    private Button btnTestRest;
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    RestaurantClient restaurantClient = retrofit.create(RestaurantClient.class);


    public RouletteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roulette, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTestRest = view.findViewById(R.id.btnTestRest);

        btnTestRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               testing();
            }
        });

    }

    private void testing() {
        Call<ResponseBody> call = restaurantClient.searchRestaurants("Bearer " + REST_APPLICATION_ID , "Avocado Toast", "San Jose");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "OnResponse: " + response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "Fail: ", t);
            }
        });
    }
}