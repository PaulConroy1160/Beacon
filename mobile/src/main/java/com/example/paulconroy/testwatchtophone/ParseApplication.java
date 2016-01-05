package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 02/01/2016.
 */

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "JZBfNYfSR3r5VNvhHjb6i4W2o8hxdNhzXh9aTZ9J", "N7NI4lg37yNNgdGUXkHeiyDL0RzNm7ahZep2PKlV");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}