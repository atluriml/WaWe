package com.example.wawe.RoomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = UserRoom.class, parentColumns = "userId", childColumns = "userId"),
        @ForeignKey(entity = RestaurantRoom.class, parentColumns = "yelpId", childColumns = "restaurantId")})
public class UserVisitedRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    private String id;

    @Ignore
    private UserRoom user;

    @Ignore
    private RestaurantRoom restaurant;

    @ColumnInfo
    private String userId;

    @ColumnInfo
    private String restaurantId;

    public UserVisitedRoom() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public UserRoom getUser() {
        return user;
    }

    public RestaurantRoom getRestaurant() {
        return restaurant;
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

    public void setUser(UserRoom user) {
        this.user = user;
    }

    public void setRestaurant(RestaurantRoom restaurant) {
        this.restaurant = restaurant;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}