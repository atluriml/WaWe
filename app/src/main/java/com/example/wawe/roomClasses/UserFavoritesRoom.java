package com.example.wawe.roomClasses;

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
    public String id;

    @Ignore
    public RestaurantRoom restaurant;

    @Ignore
    public UserRoom user;

    @ColumnInfo
    public String userId;

    @ColumnInfo
    public String restaurantId;

    public UserFavoritesRoom() {}

}