package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.text.ParseException;

/**
 * Created by paulconroy on 01/11/2015.
 */
public class TimeLine extends Activity {


    TextView logo;
    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        logo = (TextView) findViewById(R.id.logo);
        logo.setTypeface(typeFace);

        Parse.initialize(this, "JZBfNYfSR3r5VNvhHjb6i4W2o8hxdNhzXh9aTZ9J", "N7NI4lg37yNNgdGUXkHeiyDL0RzNm7ahZep2PKlV");

        ParseInstallation install = ParseInstallation.getCurrentInstallation();

//        install.put("userName","PaulyC");
        install.saveInBackground();
        setUpParseInstall();

        sendPush();
    }

    public void setUpParseInstall() {
//        final ParseUser parseUser = ParseUser.getCurrentUser();
//        parseUser.setUsername("John");
//        parseUser.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(com.parse.ParseException e) {
//                if (e == null) {
//                    // report about success
//                    Log.d("success",parseUser.getUsername());
//
//                } else {
//                    Log.d("error", e.getMessage());
//                }
//            }
//        });

        Log.d("username", ParseUser.getCurrentUser().getUsername());

    }

    public void sendPush(){

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("userName", "PaulyC");
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage("My first notification");
        push.sendInBackground();
    }
}

