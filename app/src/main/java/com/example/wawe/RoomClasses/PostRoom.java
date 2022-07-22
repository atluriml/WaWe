package com.example.wawe.RoomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.wawe.ParseModels.Post;
import com.parse.ParseException;
import com.parse.ParseFile;

@Entity
public class PostRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    private String postId;

    @ColumnInfo
    private String postTitle;

    @ColumnInfo
    private String postDescription;

    @ColumnInfo
    private String postImage;

    @ColumnInfo
    private String userName;

    @ColumnInfo
    private String userProfileImage;

    @ColumnInfo
    private String groupId;

    PostRoom () {}

    public PostRoom(Post post){
        try {
            ParseFile profileImage = post.getKeyUser().fetchIfNeeded().getParseFile("profileImage");
            if (profileImage != null){
                userProfileImage = profileImage.getUrl();
            }
            else {
                userProfileImage = "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.postId = post.getObjectId();
        this.postTitle = post.getKeyTitle();
        ParseFile image = post.getKeyImage();
        if(image != null){
            this.postImage = image.getUrl();
        }
        else {
            this.postImage = "";
        }
        this.userName = post.getKeyUser().getUsername();
        this.postDescription = post.getKeyDescription();
        this.groupId = post.getKeyGroupId();
    }

    @NonNull
    public String getPostId() {
        return postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
