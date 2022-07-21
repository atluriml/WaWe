package com.example.wawe.roomClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.wawe.ParseModels.Post;
import com.parse.ParseException;
import com.parse.ParseFile;

@Entity
public class PostRoom {

    @NonNull
    @ColumnInfo
    @PrimaryKey
    public String postId;

    @ColumnInfo
    public String postTitle;

    @ColumnInfo
    public String postImage;

    @ColumnInfo
    public String userName;

    @ColumnInfo
    public String userProfileImage;

    @ColumnInfo
    public String postDescription;

    @ColumnInfo
    public String groupId;

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
        };
//        ParseFile profileImage = post.getKeyUser().getParseFile("profileImage");
//        if (profileImage != null){
//            userProfileImage = profileImage.getUrl();
//        }
//        else {
//            userProfileImage = "";
//        }
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

}
