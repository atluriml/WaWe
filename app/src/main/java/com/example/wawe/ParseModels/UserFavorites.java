package com.example.wawe.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserFavorites")
public class UserFavorites extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_FAVORITED_RESTAURANT = "favoritedRestaurant";

    public UserFavorites () {}

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user){
        put(KEY_USER, user);
    }

    public ParseObject getRestaurantFavorite() { return getParseObject(KEY_FAVORITED_RESTAURANT); }

    public void setRestaurantFavorite(Restaurant restaurant) {
        put(KEY_FAVORITED_RESTAURANT, restaurant);
    }

    public String getUserFavoritesId() {
        return KEY_OBJECT_ID;
    }

    public void setId(String objectId) {
        put(KEY_OBJECT_ID, objectId);
    }
}
