package com.example.wawe.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wawe.BuildConfig;
import com.example.wawe.R;
import com.example.wawe.Restaurant;
import com.example.wawe.RestaurantClient;
import com.example.wawe.RestaurantSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    RestaurantClient restaurantClient = retrofit.create(RestaurantClient.class);

    ArrayList<Restaurant> restaurants = new ArrayList<>();
    private Spinner spPrice;
    private Spinner spDietaryRestriction;
    private Spinner spRadius;
    private Button btnGenerateRestaurant;
    private EditText etCuisine;

    public RouletteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roulette, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //TODO cuisine
        //TODO try with json array and see if you can somehow obtain from Yelp
        etCuisine = view.findViewById(R.id.etCuisine);

        // dietary restriction filter
        spDietaryRestriction = view.findViewById(R.id.spDietaryRestriction);
        String[] vegOption = new String[]{"", "vegetarian", "vegan", "gluten-free"};
        ArrayAdapter<String> adapterVeg = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, vegOption);
        spDietaryRestriction.setAdapter(adapterVeg);

        // radius filter
        spRadius = view.findViewById(R.id.spRadius);
        String[] possibleRadius = new String[]{"", "5", "10", "15", "25", "35", "50", "50+"};
        ArrayAdapter<String> adapterRadius = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, possibleRadius);
        spRadius.setAdapter(adapterRadius);

        // price filter
        spPrice = view.findViewById(R.id.spPrice);
        String[] priceOptions = new String[]{"","$", "$$", "$$$", "$$$$"};
        ArrayAdapter<String> adapterPrice = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, priceOptions);
        spPrice.setAdapter(adapterPrice);

        // when user is ready to see recommended restaurant
        btnGenerateRestaurant = view.findViewById(R.id.btnGenerateRestaurant);
        btnGenerateRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cuisine = etCuisine.getText().toString();
                String rad = spRadius.getSelectedItem().toString();
                int maxDistance = 0;
                if (!rad.equals("")){
                    maxDistance = Integer.parseInt(rad);
                }
                String price = spPrice.getSelectedItem().toString();
                String dietaryRestriction = spDietaryRestriction.getSelectedItem().toString();
                Toast.makeText(getContext(), "the selected price is " + price + " the are this type of DR " + dietaryRestriction + "max rad " + maxDistance, Toast.LENGTH_SHORT).show();
                testing(cuisine, dietaryRestriction, maxDistance, String.valueOf(price.length()));
            }
        });

    }

    public void testing(String cuisine, String dietaryRestriction, int radius, String price) {
        Call<RestaurantSearch> call = restaurantClient.searchRestaurants("Bearer " + REST_APPLICATION_ID , cuisine, dietaryRestriction, "St. Louis", price);
        call.enqueue(new Callback<RestaurantSearch>() {
            @Override
            public void onResponse(Call<RestaurantSearch> call, Response<RestaurantSearch> response) {
                Log.i(TAG, "OnResponse: " + response);
                Log.i(TAG, "cuisine: " + cuisine);
                RestaurantSearch body = response.body();
                if (body == null){
                    return;
                }
                restaurants.addAll(body.restaurants);
            }

            @Override
            public void onFailure(Call<RestaurantSearch> call, Throwable t) {
                Log.i(TAG, "Fail: ", t);
            }
        });
    }

    //TODO Check if visited --- use array that is in parse data base
    public List<Restaurant> checkIfVisited (JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++){
            if (restaurants.contains(jsonArray.get(i))){
                Object object = jsonArray.get(i);
                restaurants.remove(object);
            }

        }
        return restaurants;
    }


    //TODO select random
}