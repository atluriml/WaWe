package com.example.wawe.RoomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = UserRoom.class, parentColumns = "userId", childColumns = "userId"),
        @ForeignKey(entity = RestaurantRoom.class, parentColumns = "yelpId", childColumns = "restaurantId")})
public class UserFavoritesRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    private String id;

    @Ignore
    private RestaurantRoom restaurant;

    @Ignore
    private UserRoom user;

    @ColumnInfo
    private String userId;

    @ColumnInfo
    private String restaurantId;

    public UserFavoritesRoom() {}

    @NonNull
    public String getId() {
        return id;
    }

    public RestaurantRoom getRestaurant() {
        return restaurant;
    }

    public UserRoom getUser() {
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setRestaurant(RestaurantRoom restaurant) {
        this.restaurant = restaurant;
    }

    public void setUser(UserRoom user) {
        this.user = user;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}