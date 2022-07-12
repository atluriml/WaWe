package com.example.wawe.roomClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RestaurantListsDao {

    // query favorites
    @Query("SELECT * FROM UserFavoritesRoom INNER JOIN UserRoom ON UserFavoritesRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserFavoritesRoom.restaurantId = RestaurantRoom.yelpId WHERE :userId=UserRoom.userId")
    List<UserRestaurantsList> userFavoriteItems(String userId);

    // query visited
    @Query("SELECT * FROM UserVisitedRoom INNER JOIN UserRoom ON UserVisitedRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserVisitedRoom.restaurantId = RestaurantRoom.yelpId WHERE :userId=UserRoom.userId")
    List<UserRestaurantsList> userVisitedItems(String userId);

    // query favorite to be deleted
    @Query("SELECT * FROM UserFavoritesRoom INNER JOIN UserRoom ON UserFavoritesRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserFavoritesRoom.restaurantId = RestaurantRoom.yelpId WHERE :userId=UserRoom.userId AND :restaurantId=RestaurantRoom.yelpId")
    UserFavoritesRoom userFavoriteToDelete(String userId, String restaurantId);

    // query visited to be deleted
    @Query("SELECT * FROM UserVisitedRoom INNER JOIN UserRoom ON UserVisitedRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserVisitedRoom.restaurantId = RestaurantRoom.yelpId WHERE :userId=UserRoom.userId AND :restaurantId=RestaurantRoom.yelpId")
    UserVisitedRoom userVisitedToDelete(String userId, String restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserFavoritesRoom userFavoritesRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserRoom UserRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(RestaurantRoom restaurantRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserVisitedRoom userVisitedRooms);

    @Delete
    void deleteModel(UserFavoritesRoom userFavoritesRoom);

    @Delete
    void deleteModel(UserRoom UserRoom);

    @Delete
    void deleteModel(RestaurantRoom restaurantRoom);

    @Delete
    void deleteModel(UserVisitedRoom userVisitedRooms);

    @Delete
    public void deleteAll(UserRoom... userRooms);

    @Delete
    public void deleteAll(RestaurantRoom... restaurantRooms);

    @Delete
    public void deleteAll(UserFavoritesRoom... userRooms);

    @Delete
    public void deleteAll(UserVisitedRoom... userVisitedRooms);

    @Query("DELETE FROM UserRoom")
    void deleteTable();

    @Query("DELETE FROM UserVisitedRoom")
    void deleteTable2();

    @Query("DELETE FROM UserFavoritesRoom")
    void deleteTable4();

    @Query("DELETE FROM RestaurantRoom")
    void deleteTable5();

}
