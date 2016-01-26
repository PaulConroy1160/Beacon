package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 10/01/2016.
 */
public class SignUp extends Activity {

    private EditText usernameField;
    private EditText passwordField;

    private String username;
    private String password;

    public Model model;
    public ParseQuery query;

    private ParseInstallation install;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        model = Model.getInstance();
        query = model.getQuery();

        usernameField = (EditText) findViewById(R.id.userNameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

//        Parse.initialize(this, "JZBfNYfSR3r5VNvhHjb6i4W2o8hxdNhzXh9aTZ9J", "N7NI4lg37yNNgdGUXkHeiyDL0RzNm7ahZep2PKlV");
//
        install = ParseInstallation.getCurrentInstallation();


    }

    public void submit(View v) {
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();

        findUsername(username, password);
    }

    private void findUsername(String username, String password) {
//        ParseQuery pushQuery = ParseQuery.getQuery("GameScore");
//
//        pushQuery.getInBackground("2mbkKm2uev", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if(e == null){
//                    Log.d("test","no problems");
//
//                    Log.d("name = :",""+object.get("playerName"));
//                }
//                else{
//                    Log.d("test","errors");
//                }
//            }
//
//
//        });

        //ParseUser currentUser = ParseUser.getCurrentUser();


        //Log.d("current user = :",currentUser.getUsername());

        String user = username;
        String pass = password;

        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Log.d("Successfull?", "Yes, let them on");

                    //install.put("userName", "Example");
                    //Log.d("userName = ",user.get("userName").toString());
                    //install.saveInBackground();
                    logIn();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Log.d("Successfull?", "No, BOOOO!!!");
                }
            }
        });


    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void logIn() {
        Intent i = new Intent(this, MainPage.class);
        startActivity(i);
    }

    public void registerUser() {

    }


}
