package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 01/11/2015.
 */
public class Login extends Activity {


    Typeface typeFace;
    private Model model;
    private ParseInstallation install;
    private ParseUser currentUser = null;
    private Button submit;
    private Button signup;
    private List<EditText> editTextList;
    private EditText username;
    private EditText password;
    private String usernameText;
    private String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = Model.getInstance();

        model.emptyConnections();

//        username = (EditText) findViewById(R.id.userNameField);
//        password = (EditText) findViewById(R.id.passwordField);

        //submit = (Button) findViewById(R.id.submit);
        signup = (Button) findViewById(R.id.signup);

        editTextList = new ArrayList<EditText>();

//        editTextList.add(username);
//        editTextList.add(password);

//        for (final EditText editText : editTextList) {
//
//            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    }
//                }
//            });
//
//        }


        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");


        install = ParseInstallation.getCurrentInstallation();


        isLoggedIn();

    }


    public void isLoggedIn() {
        currentUser = ParseUser.getCurrentUser();


        String id = currentUser.getObjectId();


        if (id == null) {
            //Toast.makeText(this,"No user found",Toast.LENGTH_LONG).show();

        } else {
            searchNewMessages();
            Intent i = new Intent(this, Loading.class);
            startActivity(i);
            overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
            this.finish();
        }
    }


    public void findAllUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", "Brendan");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                Log.d("there is: ", "" + objects.size() + " found");
            }
        });


    }

    public void submit(View v) {
        Boolean valid = true;

        usernameText = username.getText().toString().trim();
        passwordText = password.getText().toString().trim();

        if (usernameText.length() == 0) {
            valid = false;
            username.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
        }
        if (passwordText.length() == 0) {
            valid = false;
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
        }

        if (valid) {
            username.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            findUsername(usernameText, passwordText);
        }


    }

    private void findUsername(String username, String password) {

        disableBTN();
//
        String user = username;
        String pass = password;

        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Log.d("Successfull?", "Yes, let them on");

                    logIn();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Log.d("Successfull?", "No, BOOOO!!!");
                    enableBTN();
                }
            }
        });


    }

    public void disableBTN() {
        submit.setClickable(false);
        submit.setText("...");
        signup.setClickable(false);
    }

    public void enableBTN() {
        submit.setClickable(true);
        submit.setText("Log In");
        signup.setClickable(true);
    }

    public void logIn() {

        searchNewMessages();
        Intent i = new Intent(this, Loading.class);
        //Intent i = new Intent(this, ConnectionListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
        this.finish();
    }

    public void signUp(View v) {
        Intent i = new Intent(this, NewUser.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
    }

    public void searchNewMessages() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("receiver", currentUser.getUsername());
        query.whereEqualTo("seen", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("alert", "unseen messages objects found");
                }
            }
        });
    }


}

