package com.example.wawe.RoomClasses;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {UserRoom.class, RestaurantRoom.class, UserFavoritesRoom.class, UserVisitedRoom.class, GroupsRoom.class, PostRoom.class}, version = 14)
public abstract class Database extends RoomDatabase {

    public abstract RestaurantListsDao restaurantListsDao();

    public static final String NAME = "Database";
}
