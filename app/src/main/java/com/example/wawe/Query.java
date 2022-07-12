package com.example.wawe;

import android.graphics.Color;
import android.util.Log;

import com.airbnb.lottie.L;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.ParseModels.UserFavorites;
import com.example.wawe.ParseModels.UserVisited;
import com.example.wawe.YelpClasses.YelpRestaurant;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Query {


    //parse query favorites
    public static List<Restaurant> callFavorites () {
        List<Restaurant> usersSavedRestaurants = new ArrayList<>();
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
                                    usersSavedRestaurants.add(parseRestaurant);
                                }
                                else {
                                    Log.e("TAG", e + "rip");

                                }
                            }
                        });
                    }

                }
            }
        });
        return usersSavedRestaurants;
    }

    //parse query visited
    public static List<Restaurant> callVisitedRestaurants() {
        List<Restaurant> usersSavedRestaurants = new ArrayList<>();
        ParseQuery<Restaurant> queryRestaurants = ParseQuery.getQuery(Restaurant.class).include(Restaurant.KEY_OBJECT_ID);
        queryRestaurants.findInBackground(new FindCallback<Restaurant>() {
            @Override
            public void done(List<Restaurant> objects, ParseException e) { // list of the restaurant objects
                if (e == null)
                {
                    for (Restaurant object : objects) { //all the restaurants in the parseDatabase
                        Restaurant parseRestaurant = object;
                        com.parse.ParseQuery<UserVisited> queryVisitedRestaurants = com.parse.ParseQuery.getQuery(UserVisited.class);
                        queryVisitedRestaurants.whereEqualTo(UserVisited.KEY_VISITED_RESTAURANT, parseRestaurant).whereEqualTo(UserVisited.KEY_USER_VISITED, ParseUser.getCurrentUser());
                        queryVisitedRestaurants.getFirstInBackground(new GetCallback<UserVisited>() {
                            @Override
                            public void done(UserVisited object, ParseException e) {
                                if(e == null) {
                                    usersSavedRestaurants.add(parseRestaurant);
                                }
                            }
                        });
                    }
                }
            }
        });
        return usersSavedRestaurants;
    }

    public boolean doesRestaurantExist (YelpRestaurant restaurant) {
        ParseQuery<Restaurant> queryRestaurant = ParseQuery.getQuery(Restaurant.class);
        queryRestaurant.whereEqualTo(Restaurant.KEY_ID, restaurant.getId());
        boolean exists = false;
        queryRestaurant.getFirstInBackground(new GetCallback<Restaurant>() {
            @Override
            public void done(Restaurant object, ParseException e) {
                if (e == null) // restaurant exists in parseDatabase
                {
                    //exists = true;
                }
            }
        });
        return exists;
    }

//    com.parse.ParseQuery<Restaurant> queryRestaurant = com.parse.ParseQuery.getQuery(Restaurant.class);
//        queryRestaurant.whereEqualTo(Restaurant.KEY_ID, restaurant.getId());
//        queryRestaurant.getFirstInBackground(new GetCallback<Restaurant>()
//    {
//        @Override
//        public void done(Restaurant object, ParseException e) {
//        if(e == null) // restaurant exists in parseDatabase
//        {
//            objectId = object.getObjectId();
//            parseRestaurant = object;
//            // checks to see if the user has liked the restaurant
//            com.parse.ParseQuery<UserFavorites> queryUserFavorites = com.parse.ParseQuery.getQuery(UserFavorites.class);
//            queryUserFavorites.whereEqualTo(UserFavorites.KEY_FAVORITED_RESTAURANT, parseRestaurant).whereEqualTo(UserFavorites.KEY_USER, ParseUser.getCurrentUser());
//            queryUserFavorites.getFirstInBackground(new GetCallback<UserFavorites>()
//            {
//                @Override
//                public void done(UserFavorites object, ParseException e) {
//                    if(e == null) {
//                        btnLiked.setImageResource(R.drawable.ic_vector_heart);
//                        btnLiked.setColorFilter(Color.parseColor("#92c7d6"));
//                        liked = true;
//
//                    }
//                    else {
//                        btnLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
//                        btnLiked.setColorFilter(Color.parseColor("#000000"));
//                        liked = false;
//                    }
//                }
//            });
//            //check to see if the user has visited the restaurant
//            com.parse.ParseQuery<UserVisited> queryUserVisited = com.parse.ParseQuery.getQuery(UserVisited.class);
//            queryUserVisited.whereEqualTo(UserVisited.KEY_VISITED_RESTAURANT, parseRestaurant).whereEqualTo(UserVisited.KEY_USER_VISITED, ParseUser.getCurrentUser());
//            queryUserVisited.getFirstInBackground(new GetCallback<UserVisited>() {
//                @Override
//                public void done(UserVisited object, ParseException e) {
//                    if(e == null) {
//                        btnClickIfVisited.setChecked(true);
//                    }
//                    else {
//                        btnClickIfVisited.setChecked(false);
//                    }
//                }
//            });
//        }
//        else
//        {
//            parseRestaurant = new Restaurant(restaurant);
//            btnLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
//            btnLiked.setColorFilter(Color.parseColor("#000000"));
//            liked = false;
//            btnClickIfVisited.setChecked(false);
//        }
//    }
//    });

    // parse check if restaurant exists

}
