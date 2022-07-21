package com.example.wawe.ParseModels;

import com.example.wawe.roomClasses.GroupsRoom;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Groups")
public class Groups extends ParseObject {


    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION = "location";

    public Groups () {}

    public Groups(GroupsRoom groupsRoom) {
        setObjectId(groupsRoom.groupId);
        setKeyName(groupsRoom.groupName);
        setKeyLocation(groupsRoom.groupLocation);
        setKeyDescription(groupsRoom.groupDescription);
    }

    public String getKeyName () {
        return getString(KEY_NAME);
    }

    public void setKeyName (String name) {
        put(KEY_NAME, name);
    }

    public String getKeyDescription () {
        return getString(KEY_DESCRIPTION);
    }

    public void setKeyDescription (String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getKeyLocation () {
        return getString(KEY_LOCATION);
    }

    public void setKeyLocation (String location) {
        put(KEY_LOCATION, location);
    }

}
