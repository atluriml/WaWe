package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wawe.Restaurant;
import com.example.wawe.RestaurantListAdapter;
import com.example.wawe.R;
import com.example.wawe.UserFavorites;
import com.example.wawe.UserVisited;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvFavorites;
    private RestaurantListAdapter adapter;
    private List<Restaurant> allFavoriteRestaurants;
    private TextView tvListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_lists);

        rvFavorites = findViewById(R.id.rvFavorites);
        tvListTitle = findViewById(R.id.tvListTitle);
        allFavoriteRestaurants = new ArrayList<>();
        adapter = new RestaurantListAdapter(this, allFavoriteRestaurants);
        rvFavorites.setAdapter(adapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra("visited")) {
            callVisitedRestaurants();
            tvListTitle.setText("Restaurants You Have Visited");
        }
        else {
            callFavorites();
            tvListTitle.setText("Your Favorite Restaurants");
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                if (getIntent().hasExtra("visited")) {
                    callVisitedRestaurants();
                }
                else {
                    callFavorites();
                }
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void callVisitedRestaurants() {
        ParseQuery<Restaurant> queryRestaurants = ParseQuery.getQuery(Restaurant.class).include(Restaurant.KEY_OBJECT_ID);
        queryRestaurants.findInBackground(new FindCallback<Restaurant>() {
            @Override
            public void done(List<Restaurant> objects, ParseException e) { // list of the restaurant objects
                if (e == null)
                {
                    for (Restaurant object : objects) { //all the restaurants in the parseDatabase
                        Restaurant parseRestaurant = object;
                        ParseQuery<UserVisited> queryVisitedRestaurants = ParseQuery.getQuery(UserVisited.class);
                        queryVisitedRestaurants.whereEqualTo(UserVisited.KEY_VISITED_RESTAURANT, parseRestaurant).whereEqualTo(UserVisited.KEY_USER_VISITED, ParseUser.getCurrentUser());
                        queryVisitedRestaurants.getFirstInBackground(new GetCallback<UserVisited>() {
                            @Override
                            public void done(UserVisited object, ParseException e) {
                                if(e == null) {
                                    allFavoriteRestaurants.add(parseRestaurant);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void callFavorites () {
        ParseQuery<Restaurant> queryRestaurants = ParseQuery.getQuery(Restaurant.class).include(Restaurant.KEY_OBJECT_ID);
        queryRestaurants.findInBackground(new FindCallback<Restaurant>() {
            @Override
            public void done(List<Restaurant> objects, ParseException e) { // list of the restaurant objects
                if (e == null)
                {
                    for (Restaurant object : objects) { //all the restaurants in the parseDatabase
                        Restaurant parseRestaurant = object;
                        ParseQuery<UserFavorites> queryFavoritedRestaurants = ParseQuery.getQuery(UserFavorites.class);
                        queryFavoritedRestaurants.whereEqualTo(UserFavorites.KEY_FAVORITED_RESTAURANT, parseRestaurant).whereEqualTo(UserFavorites.KEY_USER, ParseUser.getCurrentUser());
                        queryFavoritedRestaurants.getFirstInBackground(new GetCallback<UserFavorites>() {
                            @Override
                            public void done(UserFavorites object, ParseException e) {
                                if(e == null) {
                                    allFavoriteRestaurants.add(parseRestaurant);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                }
            }
        });
    }

}