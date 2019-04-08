package com.example.user.stillwalk.helperclasses;

import android.app.Application;

import com.parse.Parse;

public class ParseServerConf extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("7571d5826ab83297f988d8d57fccf73c6b237345")
                .clientKey("b8454250922c1f1d32aa9ecdd929dc37ad290095")
                .server("http://3.16.40.8:80/parse")
                .build());

    }
}
