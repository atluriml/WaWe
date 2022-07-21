package com.example.wawe.roomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.wawe.ParseModels.Groups;

@Entity
public class GroupsRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    public String groupId;

    @ColumnInfo
    public String groupName;

    @ColumnInfo
    public String groupDescription;

    @ColumnInfo
    public String groupLocation;

    GroupsRoom () {
    }

    public GroupsRoom(Groups group) {
        this.groupDescription = group.getKeyDescription();
        this.groupId = group.getObjectId();
        this.groupName = group.getKeyName();
        this.groupLocation = group.getKeyLocation();
    }

}
