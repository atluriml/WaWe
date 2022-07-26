package com.example.wawe.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wawe.Activities.MainActivity;
import com.example.wawe.Activities.SettingsActivity;
import com.example.wawe.Adapters.FavoritesAdapter;
import com.example.wawe.Adapters.VisitedAdapter;
import com.example.wawe.ParseAndDatabaseApplication;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.ParseModels.UserFavorites;
import com.example.wawe.ParseModels.UserVisited;
import com.example.wawe.R;
import com.example.wawe.RoomClasses.RestaurantListsDao;
import com.example.wawe.RoomClasses.RestaurantRoom;
import com.example.wawe.RoomClasses.UserFavoritesRoom;
import com.example.wawe.RoomClasses.UserRestaurantsList;
import com.example.wawe.RoomClasses.UserRoom;
import com.example.wawe.RoomClasses.UserVisitedRoom;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvProfileUsername;
    public static ImageView ivProfileImage;
    private static RestaurantListsDao restaurantListsDao;
    private TextView tvProfileLocation;
    public FavoritesAdapter favoritesAdapter;
    public VisitedAdapter visitedAdapter;
    private ImageButton btnSettings;
    public List<Restaurant> visitedList = new ArrayList<>();
    public List<Restaurant> favoritesList = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    private RecyclerView visitedRecyclerView;
    private RecyclerView favoritesRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        tvProfileLocation = view.findViewById(R.id.tvLocation);
        restaurantListsDao = ( (ParseAndDatabaseApplication) getContext().getApplicationContext()).getDatabase().restaurantListsDao();
        tvProfileUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnSettings = view.findViewById(R.id.btnSettings);

        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profileImage");

        if (profileImage == null){
            Glide.with(this).load(R.drawable.instagram_user_outline_24).into(ivProfileImage);
        }
        else  {
            Glide.with(this).load(profileImage.getUrl()).into(ivProfileImage);
        }

        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvProfileLocation.setText(ParseUser.getCurrentUser().getString("location"));

        // favorites RecyclerView
        favoritesAdapter = new FavoritesAdapter(getContext(), favoritesList);
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerview);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));

        // visited RecyclerView
        visitedAdapter = new VisitedAdapter(getContext(), visitedList);
        visitedRecyclerView = view.findViewById(R.id.visitedRecyclerView);
        visitedRecyclerView.setAdapter(visitedAdapter);
        visitedRecyclerView.setHasFixedSize(true);
        visitedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public void callAsyncFavorites() {
        favoritesAdapter.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<UserRestaurantsList> userFavoritesRestaurants = restaurantListsDao.userFavoriteItems();
                List<UserFavoritesRoom> favoriteRestaurantsFromDB = UserRestaurantsList.getRestaurantFavoritesList(userFavoritesRestaurants);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < favoriteRestaurantsFromDB.size(); i++){
                            Restaurant roomToParse = new Restaurant(favoriteRestaurantsFromDB.get(i).getRestaurant());
                            favoritesList.add(roomToParse);
                        }
                        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(getContext(), favoritesList);
                        favoritesRecyclerView.setAdapter(favoritesAdapter);
                    }
                });

            }
        });
    }

    public void callAsyncVisited() {
        visitedAdapter.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<UserRestaurantsList> userVisitedRestaurants = restaurantListsDao.userVisitedItems();
                List<UserVisitedRoom> visitedRestaurantsFromDB = UserRestaurantsList.getRestaurantVisitedList(userVisitedRestaurants);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < visitedRestaurantsFromDB.size(); i++){
                            Restaurant roomToParse = new Restaurant(visitedRestaurantsFromDB.get(i).getRestaurant());
                            visitedList.add(roomToParse);
                        }
                        VisitedAdapter visitedAdapter = new VisitedAdapter(getContext(), visitedList);
                        visitedRecyclerView.setAdapter(visitedAdapter);
                    }

                });
            }
        });
    }

    public void callVisitedRestaurants() {
        visitedAdapter.clear();
        ParseQuery<Restaurant> queryRestaurants = ParseQuery.getQuery(Restaurant.class).include(Restaurant.KEY_OBJECT_ID);
        queryRestaurants.addDescendingOrder("createdAt");
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
                                            userVisitedRoom.setId(object.getObjectId());
                                            userVisitedRoom.setUserId(userRoom.getUserId());
                                            userVisitedRoom.setRestaurant(restaurantRoom);
                                            userVisitedRoom.setRestaurantId(restaurantRoom.getYelpId());
                                            userVisitedRoom.setUser(userRoom);
                                            restaurantListsDao.insertModel(userVisitedRoom);
                                            visitedList.add(parseRestaurant);
                                        }
                                    });
                                    visitedAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void callFavorites () {
        favoritesAdapter.clear();
        ParseQuery<Restaurant> queryRestaurants = ParseQuery.getQuery(Restaurant.class).include(Restaurant.KEY_OBJECT_ID);
        queryRestaurants.addDescendingOrder("createdAt");
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
                                            userFavoritesRoom.setId(object.getObjectId());
                                            userFavoritesRoom.setUserId(userRoom.getUserId());
                                            userFavoritesRoom.setRestaurant(restaurantRoom);
                                            userFavoritesRoom.setRestaurantId(restaurantRoom.getYelpId());
                                            userFavoritesRoom.setUser(userRoom);
                                            restaurantListsDao.insertModel(userFavoritesRoom);
                                            if (!favoritesList.contains(parseRestaurant)) {
                                                favoritesList.add(parseRestaurant);
                                            }
                                        }
                                    });
                                    favoritesAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    public static ImageView getIvProfileImage() {
        return ivProfileImage;
    }

    @Override
    public void onResume(){
        super.onResume();
        favoritesAdapter.clear();
        visitedAdapter.clear();
        if (MainActivity.isOnline(getContext())) {
            callFavorites();
            callVisitedRestaurants();
        }
        else {
            callAsyncFavorites();
            callAsyncVisited();
        }
    }
}