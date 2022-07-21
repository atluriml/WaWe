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
    @Query("SELECT * FROM UserFavoritesRoom INNER JOIN UserRoom ON UserFavoritesRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserFavoritesRoom.restaurantId = RestaurantRoom.yelpId")
    List<UserRestaurantsList> userFavoriteItems();

    // query visited
    @Query("SELECT * FROM UserVisitedRoom INNER JOIN UserRoom ON UserVisitedRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserVisitedRoom.restaurantId = RestaurantRoom.yelpId")
    List<UserRestaurantsList> userVisitedItems();

    // query visited to delete
    @Query("SELECT * FROM UserVisitedRoom INNER JOIN UserRoom ON UserVisitedRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserVisitedRoom.restaurantId = RestaurantRoom.yelpId WHERE UserVisitedRoom.userId = :userId AND UserVisitedRoom.restaurantId = :restaurantId")
    UserVisitedRoom userVisitedDelete(String userId, String restaurantId);

    // query visited
    @Query("SELECT * FROM UserFavoritesRoom INNER JOIN UserRoom ON UserFavoritesRoom.userId = UserRoom.userId INNER JOIN RestaurantRoom ON UserFavoritesRoom.restaurantId = RestaurantRoom.yelpId WHERE UserFavoritesRoom.userId = :userId AND UserFavoritesRoom.restaurantId = :restaurantId")
    UserFavoritesRoom userFavoriteDelete(String userId, String restaurantId);

    // query groups
    @Query("SELECT * FROM GroupsRoom")
    List<GroupsRoom> groupsOffline();

    // query posts in the group
    @Query("SELECT * FROM PostRoom WHERE PostRoom.groupId = :groupId")
    List<PostRoom> groupPosts(String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserFavoritesRoom userFavoritesRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserRoom UserRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(RestaurantRoom restaurantRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(UserVisitedRoom userVisitedRooms);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(PostRoom postRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(GroupsRoom groupsRoom);

    @Delete
    void deleteModel(UserVisitedRoom userVisitedRooms);

    @Delete
    void deleteModel(UserFavoritesRoom userFavoritesRoom);

}
