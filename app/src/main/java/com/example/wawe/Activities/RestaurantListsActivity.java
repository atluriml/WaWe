package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.wawe.ParseAndDatabaseApplication;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.Adapters.RestaurantListAdapter;
import com.example.wawe.R;
import com.example.wawe.roomClasses.RestaurantListsDao;
import com.example.wawe.ParseModels.UserFavorites;
import com.example.wawe.roomClasses.RestaurantRoom;
import com.example.wawe.roomClasses.UserFavoritesRoom;
import com.example.wawe.roomClasses.UserRestaurantsList;
import com.example.wawe.ParseModels.UserVisited;
import com.example.wawe.roomClasses.UserRoom;
import com.example.wawe.roomClasses.UserVisitedRoom;
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
    private List<Restaurant> usersSavedRestaurants;
    private TextView tvListTitle;
    static RestaurantListsDao restaurantListsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_lists);

        restaurantListsDao = ( (ParseAndDatabaseApplication) getApplicationContext()).getDatabase().restaurantListsDao();
        rvFavorites = findViewById(R.id.rvFavorites);
        tvListTitle = findViewById(R.id.tvListTitle);
        usersSavedRestaurants = new ArrayList<>();
        adapter = new RestaurantListAdapter(this, usersSavedRestaurants);
        rvFavorites.setAdapter(adapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));

        if (!usersSavedRestaurants.isEmpty()){
            adapter.clear();
        }

        if (getIntent().hasExtra("visited")) {
            if (isNetworkAvailable()){
                callVisitedRestaurants();
            }
            else {
                callAsyncVisited();
            }
            tvListTitle.setText("Restaurants You Have Visited");
        }
        else {
            if (!isNetworkAvailable()) {
                callAsyncFavorites();
            }
            else {
                callFavorites();
            }
            tvListTitle.setText("Your Favorite Restaurants");
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                if (getIntent().hasExtra("visited")) {
                    if (isNetworkAvailable()){
                        callVisitedRestaurants();
                    }
                    else {
                        callAsyncVisited();
                    }
                }
                else {
                    if (!isNetworkAvailable()) {
                        callAsyncFavorites();
                    }
                    else {
                        callFavorites();
                    }
                }
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void callAsyncFavorites() {
        adapter.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<UserRestaurantsList> userFavoritesRestaurants = restaurantListsDao.userFavoriteItems();
                List<UserFavoritesRoom> favoriteRestaurantsFromDB = UserRestaurantsList.getRestaurantFavoritesList(userFavoritesRestaurants);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < favoriteRestaurantsFromDB.size(); i++){
                            Restaurant roomToParse = new Restaurant(favoriteRestaurantsFromDB.get(i).restaurant);
                            usersSavedRestaurants.add(roomToParse);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        });
    }

    public void callAsyncVisited() {
        adapter.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<UserRestaurantsList> userVisitedRestaurants = restaurantListsDao.userVisitedItems();
                List<UserVisitedRoom> visitedRestaurantsFromDB = UserRestaurantsList.getRestaurantVisitedList(userVisitedRestaurants);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < visitedRestaurantsFromDB.size(); i++){
                            Restaurant roomToParse = new Restaurant(visitedRestaurantsFromDB.get(i).restaurant);
                            usersSavedRestaurants.add(roomToParse);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
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
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            UserRoom userRoom = new UserRoom(ParseUser.getCurrentUser());
                                            restaurantListsDao.insertModel(userRoom);
                                            RestaurantRoom restaurantRoom = new RestaurantRoom(parseRestaurant);
                                            restaurantListsDao.insertModel(restaurantRoom);
                                            UserVisitedRoom userVisitedRoom = new UserVisitedRoom();
                                            userVisitedRoom.id = object.getObjectId();
                                            userVisitedRoom.userId = userRoom.getUserId();
                                            userVisitedRoom.restaurant = restaurantRoom;
                                            userVisitedRoom.restaurantId = restaurantRoom.yelpId;
                                            userVisitedRoom.user = userRoom;
                                            restaurantListsDao.insertModel(userVisitedRoom);
                                            usersSavedRestaurants.add(parseRestaurant);
                                        }
                                    });
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
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            UserRoom userRoom = new UserRoom(ParseUser.getCurrentUser());
                                            restaurantListsDao.insertModel(userRoom);
                                            RestaurantRoom restaurantRoom = new RestaurantRoom(parseRestaurant);
                                            restaurantListsDao.insertModel(restaurantRoom);
                                            UserFavoritesRoom userFavoritesRoom = new UserFavoritesRoom();
                                            userFavoritesRoom.id = object.getObjectId();
                                            userFavoritesRoom.userId = userRoom.getUserId();
                                            userFavoritesRoom.restaurant = restaurantRoom;
                                            userFavoritesRoom.restaurantId = restaurantRoom.yelpId;
                                            userFavoritesRoom.user = userRoom;
                                            restaurantListsDao.insertModel(userFavoritesRoom);
                                            usersSavedRestaurants.add(parseRestaurant);
                                        }

                                    });

                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}