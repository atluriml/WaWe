package com.example.wawe.RoomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.parse.ParseUser;

@Entity
public class UserRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    private String userId;

    public UserRoom () {}

    public UserRoom (ParseUser user) {
        userId = user.getObjectId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
