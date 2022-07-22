package com.example.wawe.ParseModels;

import com.example.wawe.RoomClasses.GroupsRoom;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Groups")
public class Groups extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION = "location";

    public Groups () {}

    public Groups(GroupsRoom groupsRoom) {
        setObjectId(groupsRoom.getGroupId());
        setKeyName(groupsRoom.getGroupName());
        setKeyLocation(groupsRoom.getGroupLocation());
        setKeyDescription(groupsRoom.getGroupDescription());
    }

    public String getKeyName () {
        return getString(KEY_NAME);
    }

    public void setKeyName (String name) {
        if (name == null){
            put(KEY_NAME, " ");
        }
        else {
            put(KEY_NAME, name);
        }
    }

    public String getKeyDescription () {
        return getString(KEY_DESCRIPTION);
    }

    public void setKeyDescription (String description) {
        if (description == null){
            put(KEY_DESCRIPTION, " ");
        }
        else {
            put(KEY_DESCRIPTION, description);
        }
    }

    public String getKeyLocation () {
        return getString(KEY_LOCATION);
    }

    public void setKeyLocation (String location) {
        if (location == null){
            put(KEY_LOCATION, " ");
        }
        else {
        put(KEY_LOCATION, location);
        }
    }

}
