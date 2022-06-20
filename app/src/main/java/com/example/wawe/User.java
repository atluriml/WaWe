package com.example.wawe;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

    public static final String KEY_LOCATION = "location";
    public static final String KEY_FAVORITES = "favorites";
    public static final String KEY_VISITED = "visited";

    public String getLocation(){
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location){
        put(KEY_LOCATION, location);
    }

}
