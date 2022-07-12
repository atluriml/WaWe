package com.example.wawe;

import android.app.Application;

import androidx.room.Room;

import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.ParseModels.UserFavorites;
import com.example.wawe.ParseModels.UserVisited;
import com.example.wawe.roomClasses.Database;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseAndDatabaseApplication extends Application {

    Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, Database.class, Database.NAME).fallbackToDestructiveMigration().build();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Restaurant.class);
        ParseObject.registerSubclass(UserFavorites.class);
        ParseObject.registerSubclass(UserVisited.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APPLICATION_ID)
                .clientKey(BuildConfig.PARSE_CLIENT_ID)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

    public Database getDatabase () {
        return database;
    }

}
