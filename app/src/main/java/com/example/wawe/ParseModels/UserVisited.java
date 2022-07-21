package com.example.wawe.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserVisited")
public class UserVisited extends ParseObject {

    public static final String KEY_USER_VISITED = "user";
    public static final String KEY_VISITED_RESTAURANT = "visitedRestaurant";

    public UserVisited () {}

    public UserVisited (ParseUser user, Restaurant restaurant) {}

    public ParseUser getUserVisited(){
        return getParseUser(KEY_USER_VISITED);
    }

    public void setUserVisited (ParseUser user){
        put(KEY_USER_VISITED, user);
    }

    public ParseObject getRestaurantVisited() { return getParseObject(KEY_VISITED_RESTAURANT); }

    public void setRestaurantVisited(Restaurant restaurant) {
        put(KEY_VISITED_RESTAURANT, restaurant);
    }

}
