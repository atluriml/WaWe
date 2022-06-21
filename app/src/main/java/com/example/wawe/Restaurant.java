package com.example.wawe;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


public class Restaurant {

    String price;
    String location;
    int radius;
    String category;
    String term;

    public Restaurant () {}

    public Restaurant (JSONObject jsonObject) throws JSONException{
        price = jsonObject.getString("price");
        location = jsonObject.getString("location");
        radius = jsonObject.getInt("radius");
        category = jsonObject.getString("categories");
        term = jsonObject.getString("term"); //TODO make sure term is always restaurants
    }

    public int getRadius() {
        return radius;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getTerm() {
        return term;
    }
}
