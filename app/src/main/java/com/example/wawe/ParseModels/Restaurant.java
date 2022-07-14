package com.example.wawe.ParseModels;

import android.util.Log;

import com.example.wawe.YelpClasses.YelpRestaurant;
import com.example.wawe.roomClasses.RestaurantRoom;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.json.JSONArray;
import java.util.List;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ID = "restID";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_RATING = "rating";
    public static final String KEY_CATEGORIES = "categories";

    public Restaurant () {}

    public Restaurant(YelpRestaurant yelpRestaurant) {
        setKeyName(yelpRestaurant.getName());
        setKeyId(yelpRestaurant.getId());
        setKeyImage(yelpRestaurant.getRestaurantImage());
        setKeyPrice(yelpRestaurant.getPrice());
        setKeyAddress(yelpRestaurant.getLocation().getAddress());
        setKeyRating(yelpRestaurant.getRating());
        for (int i = 0; i < yelpRestaurant.getCategory().size(); i++){
            add(yelpRestaurant.getCategory().get(i).getTitle());
        }
    }

    public Restaurant(RestaurantRoom restaurantRoom) {
        setKeyName(restaurantRoom.name);
        setKeyId(restaurantRoom.yelpId);
        setKeyImage(restaurantRoom.image);
        setKeyPrice(restaurantRoom.price);
        setKeyAddress(restaurantRoom.address);
        setKeyRating(restaurantRoom.rating);
    }

    public String getKeyName() {
        return getString(KEY_NAME);
    }

    public String getKeyImage() {
        return getString(KEY_IMAGE);
    }

    public String getKeyPrice () {
        return getString(KEY_PRICE);
    }

    public String getKeyId () {
        return getString(KEY_ID);
    }

    public String getKeyAddress () {
        return getString(KEY_ADDRESS);
    }

    public double getKeyRating () {
        return getDouble(KEY_RATING);
    }

    public void add(String category) {
        add(KEY_CATEGORIES, category);
    }

    public JSONArray getKeyCategories () { return getJSONArray(KEY_CATEGORIES);
    }

    public void setKeyName (String name) {
        put(KEY_NAME, name);
    }

    public void setKeyId (String id) {
        put(KEY_ID, id);
    }

    public void setKeyPrice (String price) {
        if (price == null) {
            return;
        }
        put(KEY_PRICE, price);
    }

    public void setKeyImage (String restaurantImage) {
        put(KEY_IMAGE, restaurantImage);
    }

    public void setKeyAddress (String address) {
        put(KEY_ADDRESS, address);
    }

    public void setKeyRating (double rating) {
        put(KEY_RATING, rating);
    }

    public void setKeyCategories (JSONArray jsonArray){
        put(KEY_CATEGORIES, jsonArray);
    }

    public static void likeRestaurant (Restaurant restaurant){
        UserFavorites userFavorites = new UserFavorites();
        userFavorites.setUser(ParseUser.getCurrentUser());
        userFavorites.setRestaurantFavorite(restaurant);
        userFavorites.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    return;
                }
            }
        });
    }

    public static void unFavoriteRestaurant (Restaurant restaurant) {
        ParseQuery<UserFavorites> query = ParseQuery.getQuery(UserFavorites.class);
        query.whereEqualTo(UserFavorites.KEY_FAVORITED_RESTAURANT, restaurant);
        query.findInBackground(new FindCallback<UserFavorites>() {
            @Override
            public void done(List<UserFavorites> objects, ParseException e) {
                if (e != null) {
                    return;
                }
                for (ParseObject object : objects) {
                    object.deleteInBackground();
                }
            }
        });
    }

    public static void markRestaurantVisited (Restaurant restaurant){
        UserVisited userVisited = new UserVisited();
        userVisited.setUserVisited(ParseUser.getCurrentUser());
        userVisited.setRestaurantVisited(restaurant);
        userVisited.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    return;
                }
            }
        });
    }

    public static void markAsNotVisited (Restaurant restaurant) {
        ParseQuery<UserVisited> query = ParseQuery.getQuery(UserVisited.class);
        query.whereEqualTo(UserVisited.KEY_VISITED_RESTAURANT, restaurant);
        query.findInBackground(new FindCallback<UserVisited>() {
            @Override
            public void done(List<UserVisited> objects, ParseException e) {
                if (e != null) {
                    return;
                }
                for (ParseObject object : objects) {
                    object.deleteInBackground();
                }
            }
        });
    }

}
