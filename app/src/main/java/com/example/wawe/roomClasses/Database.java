package com.example.wawe.roomClasses;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {UserRoom.class, RestaurantRoom.class, UserFavoritesRoom.class, UserVisitedRoom.class}, version = 7)
public abstract class Database extends RoomDatabase {

    public abstract RestaurantListsDao restaurantListsDao();

    public static final String NAME = "Database";
}
