package com.example.wawe;

import com.example.wawe.restaurantClasses.Restaurant;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

public class User {

    public static final String KEY_LOCATION = "location";
    public static final String KEY_FAVORITES = "favorites";
    public static final String KEY_VISITED = "visited";

    ParseUser user;

    public User(ParseUser currUser) {
        user = currUser;
    }

    public ParseUser getUser() {
        return user;
    }

    public String getLocation(){
        return user.getString(KEY_LOCATION);
    }

    public void setLocation(String location){
        user.put(KEY_LOCATION, location);
    }

    public JSONArray getFavorites(){
        return user.getJSONArray(KEY_FAVORITES);
    }

    public void setFavorites(JSONArray jsonArray){
        user.put(KEY_FAVORITES, jsonArray);
    }

    public void likeRestaurant(Restaurant restaurant){
        user.add(KEY_FAVORITES, restaurant.getId());
    }

    public int returnPosition(JSONArray array, Restaurant restaurant) throws JSONException {
        String id = restaurant.getId();
        for (int i = 0; i < array.length(); ++i){
            if (array.getString(i).equals(id)){
                return i;
            }
        }
        return -1;
    }

    public void unLikeRestaurant(Restaurant restaurant, JSONArray array) throws JSONException {
        int position = returnPosition(array, restaurant);
        array.remove(position);
        setFavorites(array);
    }

    public Boolean getIsFavorited(JSONArray jsonArray, Restaurant restaurant) throws JSONException {
        String id = restaurant.getId();
        for (int i = 0; i < jsonArray.length(); ++i){
            if (jsonArray.getString(i).equals(id)){
                return true;
            }
        }
        return false;
    }

    public JSONArray getVisited(){
        return user.getJSONArray(KEY_VISITED);
    }

    public void setVisited(JSONArray jsonArray){
        user.put(KEY_VISITED, jsonArray);
    }

    public void visitedRestaurant(Restaurant restaurant){
        user.add(KEY_VISITED, restaurant.getId());
    }

    public void unVisitRestaurant(Restaurant restaurant, JSONArray array) throws JSONException {
        int position = returnPosition(array, restaurant);
        array.remove(position);
        setVisited(array);
    }

    public Boolean getHaveVisited(JSONArray jsonArray, Restaurant restaurant) throws JSONException {
        String id = restaurant.getId();
        for (int i = 0; i < jsonArray.length(); ++i){
            if (jsonArray.getString(i).equals(id)){
                return true;
            }
        }
        return false;
    }

}
