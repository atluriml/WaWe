package com.example.wawe.roomClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantListsDao {

    // query favorites
    @Query("SELECT * FROM UserFavoritesRoom INNER JOIN UserRoom ON UserFavoritesRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserFavoritesRoom.restaurantId = RestaurantRoom.yelpId")
    List<UserRestaurantsList> userFavoriteItems();

    // query visited
    @Query("SELECT * FROM UserVisitedRoom INNER JOIN UserRoom ON UserVisitedRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserVisitedRoom.restaurantId = RestaurantRoom.yelpId")
    List<UserRestaurantsList> userVisitedItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserFavoritesRoom userFavoritesRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserRoom UserRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(RestaurantRoom restaurantRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserVisitedRoom userVisitedRooms);

    @Query("DELETE FROM UserRoom")
    void deleteTableUser();

    @Query("DELETE FROM UserVisitedRoom")
    void deleteTableVisited();

    @Query("DELETE FROM UserFavoritesRoom")
    void deleteTableFavorites();

    @Query("DELETE FROM RestaurantRoom")
    void deleteTableRestaurant();

}
