package com.example.wawe.RoomClasses;

import androidx.room.Embedded;

import java.util.ArrayList;
import java.util.List;

public class UserRestaurantsList {

    @Embedded
    private UserRoom userRoom;

    @Embedded
    private RestaurantRoom restaurantRoom;

    public static List<UserFavoritesRoom> getRestaurantFavoritesList(List<UserRestaurantsList> userFavoritesRestaurants) {
        List<UserFavoritesRoom> userFavoritesRooms = new ArrayList<>();
        for (int i = 0; i < userFavoritesRestaurants.size(); i++){
            UserFavoritesRoom userFavoritesRoom = new UserFavoritesRoom();
            userFavoritesRoom.setUser(userFavoritesRestaurants.get(i).userRoom);
            userFavoritesRoom.setRestaurant(userFavoritesRestaurants.get(i).restaurantRoom);
            userFavoritesRooms.add(userFavoritesRoom);
        }
        return userFavoritesRooms;
    }

    public static List<UserVisitedRoom> getRestaurantVisitedList(List<UserRestaurantsList> userFavoritesRestaurants) {
        List<UserVisitedRoom> userVisitedRooms = new ArrayList<>();
        for (int i = 0; i < userFavoritesRestaurants.size(); i++){
            UserVisitedRoom userVisitedRoom = new UserVisitedRoom();
            userVisitedRoom.setUser(userFavoritesRestaurants.get(i).userRoom);
            userVisitedRoom.setRestaurant(userFavoritesRestaurants.get(i).restaurantRoom);
            userVisitedRooms.add(userVisitedRoom);
        }
        return userVisitedRooms;
    }

    public UserRoom getUserRoom() {
        return userRoom;
    }

    public RestaurantRoom getRestaurantRoom() {
        return restaurantRoom;
    }

    public void setUserRoom(UserRoom userRoom) {
        this.userRoom = userRoom;
    }

    public void setRestaurantRoom(RestaurantRoom restaurantRoom) {
        this.restaurantRoom = restaurantRoom;
    }
}
