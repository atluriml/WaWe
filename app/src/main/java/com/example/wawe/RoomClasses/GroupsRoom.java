package com.example.wawe.RoomClasses;

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
    private String groupId;

    @ColumnInfo
    private String groupName;

    @ColumnInfo
    private String groupDescription;

    @ColumnInfo
    private String groupLocation;

    GroupsRoom () {
    }

    public GroupsRoom(Groups group) {
        this.groupDescription = group.getKeyDescription();
        this.groupId = group.getObjectId();
        this.groupName = group.getKeyName();
        this.groupLocation = group.getKeyLocation();
    }

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getGroupLocation() {
        return groupLocation;
    }

    public void setGroupId(@NonNull String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void setGroupLocation(String groupLocation) {
        this.groupLocation = groupLocation;
    }
}
