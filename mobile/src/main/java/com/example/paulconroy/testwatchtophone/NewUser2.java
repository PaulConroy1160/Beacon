package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import Database.DB;

/**
 * Created by paulconroy on 25/01/2016.
 */
public class NewUser2 extends Activity {

    private EditText bio;
    private TextView title;
    private TextView counter;
    private int textRemain;
    private Button submit;
    private String fName;
    private String lName;
    private String userName;
    private String password;
    private String bioText;
    private DB mDb;
    private ParseInstallation install;
    private Handler handler;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user2);

        bio = (EditText) findViewById(R.id.bioField);
        title = (TextView) findViewById(R.id.nameText);
        counter = (TextView) findViewById(R.id.counterText);
        submit = (Button) findViewById(R.id.submit);

        install = ParseInstallation.getCurrentInstallation();

        mDb = new DB(this);

        textRemain = 100;

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        userName = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");

        Log.d("user", fName + lName + userName + password);

        //used to keep track of the character count in bio
        handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                checkCharacters();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);


    }

    public void checkCharacters() {

        if (bio.getText().length() != 0)
            textRemain = (100 - bio.getText().length());

        counter.setText(textRemain + " REMAINING");

        if (textRemain < 0) {
            counter.setTextColor(Color.parseColor("#f27878"));
            submit.setClickable(false);
            submit.setText("....");
            submit.setBackgroundColor(Color.parseColor("#fac7c7"));

        } else {
            counter.setTextColor(Color.parseColor("#a3f278"));
            submit.setClickable(true);
            submit.setText("Next");
            submit.setBackgroundColor(Color.parseColor("#f1654c"));
        }

    }

    public void submit(View v) {
        bioText = bio.getText().toString();


        ParseUser pUser = new ParseUser();
        pUser.setUsername(userName);
        pUser.setPassword(password);
        install.put("userName", userName);
        install.saveInBackground();

        pUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Problems?", "Nope!! none at all");

                    ParseObject user = new ParseObject("User");
                    user.put("fName", fName);
                    user.put("lName", lName);
                    user.put("userName", userName);
                    user.put("bio", bioText);

                    user.saveInBackground();

                    returnToLogin();

                } else {
                    Log.d("Problems?", "Sadly...");
                }
            }
        });


    }

    public void returnToLogin() {
        //Toast.makeText(this, "User created! Welcome, " + userName, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, Loading.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);


    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
    }
}
