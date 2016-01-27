package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by paulconroy on 27/01/2016.
 */
public class Loading extends Activity {

    private FrameLayout overLay;
    private FrameLayout overLay2;
    private FrameLayout overLay3;
    private TextView welcomeText;
    private ParseUser user;
    private Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_success);

        user = ParseUser.getCurrentUser();
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        overLay = (FrameLayout) findViewById(R.id.overLay);
        overLay2 = (FrameLayout) findViewById(R.id.overLay2);
        overLay3 = (FrameLayout) findViewById(R.id.overLay3);

        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setVisibility(View.INVISIBLE);
        welcomeText.setTypeface(typeFace);

        welcomeText.setText("Welcome, " + user.getUsername() + "!");

        loadAnimation();

    }

    public void loadAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_delay);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_delay);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_delay);
        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        overLay.setAnimation(animation);
        overLay2.setAnimation(animation2);
        overLay3.setAnimation(animation3);
        welcomeText.setAnimation(fadeAnimation);

        animation3.setStartOffset(500);
        animation2.setStartOffset(600);
        animation.setStartOffset(700);
        fadeAnimation.setStartOffset(1000);
        welcomeText.setVisibility(View.VISIBLE);


        fadeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchNewMessages();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //loadOut();

    }

    public void loadOut() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left_delay);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_out_left_delay);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.slide_out_left_delay);
        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        overLay.clearAnimation();
        overLay.setAnimation(animation);
        overLay2.clearAnimation();
        overLay2.setAnimation(animation2);
        overLay3.clearAnimation();
        overLay3.setAnimation(animation3);
        welcomeText.clearAnimation();
        welcomeText.setAnimation(fadeAnimation);

        fadeAnimation.setStartOffset(1500);
        animation.setStartOffset(2000);
        animation2.setStartOffset(2100);
        animation3.setStartOffset(2200);


        welcomeText.setVisibility(View.GONE);
        overLay.setVisibility(View.GONE);
        overLay2.setVisibility(View.GONE);
        overLay3.setVisibility(View.GONE);

        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                connectionsActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void searchNewMessages() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("receiver", user.getUsername());
        query.whereEqualTo("seen", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("alert", "unseen messages objects found");
                    loadOut();
                }
            }
        });
    }

    public void connectionsActivity() {
        Intent i = new Intent(this, ConnectionListActivity.class);
        startActivity(i);

        this.finish();


    }


}
