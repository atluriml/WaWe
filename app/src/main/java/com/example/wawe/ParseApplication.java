package com.example.wawe;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APPLICATION_ID)
                .clientKey(BuildConfig.PARSE_CLIENT_ID)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
