package com.example.wawe;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("W5KHcnAu6XxY685P9ItV8Pd2xG3xMwnBBtWEl9jB")
                .clientKey("jPR8epasiVdEdVfqu0ggQOjy0ey28Ede3ulKRxMR")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
