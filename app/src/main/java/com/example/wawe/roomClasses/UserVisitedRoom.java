package com.example.wawe.roomClasses;

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
    @PrimaryKey(autoGenerate = true)
    public long id;

    @Ignore
    public UserRoom user;

    @Ignore
    public RestaurantRoom restaurant;

    @ColumnInfo
    public String userId;

    @ColumnInfo
    public String restaurantId;

    public UserVisitedRoom() {
    }

}