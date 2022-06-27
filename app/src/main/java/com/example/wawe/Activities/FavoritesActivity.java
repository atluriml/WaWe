package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.example.wawe.BuildConfig;
import com.example.wawe.FavoritesAdapter;
import com.example.wawe.R;
import com.example.wawe.RestaurantClient;
import com.example.wawe.User;
import com.example.wawe.restaurantClasses.Restaurant;
import com.example.wawe.restaurantClasses.RestaurantSearch;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesActivity extends AppCompatActivity {

    public static final String TAG = "FavoritesActivity";
    private static final String REST_APPLICATION_ID = BuildConfig.YELP_APPLICATION_ID;
    public static final String BASE_URL = "https://api.yelp.com/v3/";

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvFavorites;
    private FavoritesAdapter adapter;
    private List<Restaurant> allFavoriteRestaurants;
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    RestaurantClient restaurantClient = retrofit.create(RestaurantClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        rvFavorites = findViewById(R.id.rvFavorites);

        // initialize the array that will hold posts and create a PostsAdapter
        allFavoriteRestaurants = new ArrayList<>();
        adapter = new FavoritesAdapter(this, allFavoriteRestaurants);

        // set the adapter on the recycler view
        rvFavorites.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));

        callYelp();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                callYelp();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void callYelp() {
        User currentUser = new User(ParseUser.getCurrentUser());
        for (int i = 0; i < currentUser.getFavorites().length(); i++) {
            String id = null;
            try {
                id = currentUser.getFavorites().get(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<Restaurant> call = restaurantClient.searchRestaurants("Bearer " + REST_APPLICATION_ID, id);
            Log.i(TAG, "the id is " + id);
            call.enqueue(new Callback<Restaurant>() {
                @Override
                public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                    Log.i(TAG, "" + response);
                    Restaurant body = response.body();
                    if (body == null) {
                        return;
                    }
                    allFavoriteRestaurants.add(body);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<Restaurant> call, Throwable t) {
                    Log.i(TAG, "Fail: ", t);
                }
            });
        }
    }
}