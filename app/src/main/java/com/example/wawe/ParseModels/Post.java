package com.example.wawe.ParseModels;
import android.util.Log;
import com.example.wawe.roomClasses.PostRoom;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_GROUP = "groupId";
    public static final String KEY_LIKED_USERS = "likedUsers";

    public String offlineUsername;
    public String offlineProfileImage;
    public String offlinePostImageUrl;

    public Post() {}

    public Post(PostRoom postRoom) {
        setKeyTitle(postRoom.postTitle);
        setKeyDescription(postRoom.postDescription);
        if (!postRoom.postImage.equals("")){
            setOfflinePostImageUrl(postRoom.postImage);
        }
        setOfflineUsername(postRoom.userName);
        if (!postRoom.userProfileImage.equals("")){
            setOfflineProfileImage(postRoom.userProfileImage);
        }
    }

    public String getKeyTitle () {
        return getString(KEY_TITLE);
    }

    public void setKeyTitle (String title) {
        put(KEY_TITLE, title);
    }

    public String getKeyDescription (){
        return getString(KEY_DESCRIPTION);
    }

    public void setKeyDescription (String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getKeyImage (){
        return getParseFile(KEY_IMAGE);
    }

    public void setKeyImage (ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getKeyUser (){
        return getParseUser(KEY_USER);
    }

    public void setKeyUser (ParseUser user){
        put(KEY_USER, user);
    }

    public String getKeyGroupId () {
        return getString(KEY_GROUP);
    }

    public void setKeyGroupId (String groupId) {
        put(KEY_GROUP, groupId);
    }

    public long getLikesCount(JSONArray jsonArray){
        return jsonArray.length();
    }

    public JSONArray getLikedUsers() {
        return getJSONArray(KEY_LIKED_USERS);
    }

    public void setLikedUsers(JSONArray jsonArray) {
        put(KEY_LIKED_USERS, jsonArray);
    }

    public Boolean getIsLiked(JSONArray jsonArray, String id) throws JSONException {
        for (int i = 0; i < jsonArray.length(); ++i){
            if (jsonArray.getString(i).equals(id)){
                return true;
            }
        }
        return false;
    }

    public void likePost(ParseUser user){
        add(KEY_LIKED_USERS, user.getObjectId());
    }

    public int returnPosition(JSONArray array, String id) throws JSONException {
        for (int i = 0; i < array.length(); ++i){
            if (array.getString(i).equals(id)){
                return i;
            }
        }
        return -1;
    }

    public void unLikePost(ParseUser user, JSONArray array) throws JSONException {
        String id = user.getObjectId();
        int position = returnPosition(array, id);
        array.remove(position);
        setLikedUsers(array);
    }

    public static String calculateTimeAgo(Date createdAt) {
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;
        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }
        return "";
    }

    public void setOfflineUsername(String offlineUsername) {
        this.offlineUsername = offlineUsername;
    }

    public String getOfflineUsername() {
        return offlineUsername;
    }

    public void setOfflineProfileImage(String offlineProfileImage) {
        this.offlineProfileImage = offlineProfileImage;
    }

    public String getOfflineProfileImage() {
        return offlineProfileImage;
    }

    public void setOfflinePostImageUrl(String offlinePostImageUrl) {
        this.offlinePostImageUrl = offlinePostImageUrl;
    }

    public String getOfflinePostImageUrl() {
        return offlinePostImageUrl;
    }
}
