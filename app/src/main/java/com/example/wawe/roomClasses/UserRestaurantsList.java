package com.example.wawe.roomClasses;

import androidx.room.Embedded;

import com.example.wawe.roomClasses.RestaurantRoom;
import com.example.wawe.roomClasses.UserRoom;
import com.example.wawe.roomClasses.UserFavoritesRoom;

import java.util.ArrayList;
import java.util.List;

public class UserRestaurantsList {

    @Embedded
    UserRoom userRoom;

    @Embedded
    RestaurantRoom restaurantRoom;

    public static List<UserFavoritesRoom> getRestaurantFavoritesList(List<UserRestaurantsList> userFavoritesRestaurants) {
        List<UserFavoritesRoom> userFavoritesRooms = new ArrayList<>();
        for (int i = 0; i < userFavoritesRestaurants.size(); i++){
            UserFavoritesRoom userFavoritesRoom = new UserFavoritesRoom();
            userFavoritesRoom.user = userFavoritesRestaurants.get(i).userRoom;
            userFavoritesRoom.restaurant = userFavoritesRestaurants.get(i).restaurantRoom;
            userFavoritesRooms.add(userFavoritesRoom);
        }
        return userFavoritesRooms;
    }

    public static List<UserVisitedRoom> getRestaurantVisitedList(List<UserRestaurantsList> userFavoritesRestaurants) {
        List<UserVisitedRoom> userVisitedRooms = new ArrayList<>();
        for (int i = 0; i < userFavoritesRestaurants.size(); i++){
            UserVisitedRoom userVisitedRoom = new UserVisitedRoom();
            userVisitedRoom.user = userFavoritesRestaurants.get(i).userRoom;
            userVisitedRoom.restaurant = userFavoritesRestaurants.get(i).restaurantRoom;
            userVisitedRooms.add(userVisitedRoom);
        }
        return userVisitedRooms;
    }
}
