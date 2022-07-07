package com.example.wawe;

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
    public void setRestaurantFavorite(ParseObject restaurant) { put(KEY_FAVORITED_RESTAURANT, restaurant); }

}
