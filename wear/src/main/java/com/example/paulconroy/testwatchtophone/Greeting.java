package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 28/01/2016.
 */
public class Greeting extends Activity {

    private FrameLayout overLay;
    private FrameLayout overLay2;
    private FrameLayout overLay3;
    private TextView welcomeText;
    private Typeface typeFace;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_wear);


        userName = getIntent().getStringExtra("information");


        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        overLay = (FrameLayout) findViewById(R.id.overLay);
        overLay2 = (FrameLayout) findViewById(R.id.overLay2);
        overLay3 = (FrameLayout) findViewById(R.id.overLay3);

        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setVisibility(View.INVISIBLE);
        welcomeText.setTypeface(typeFace);

        if (userName == null) {
            welcomeText.setText("Welcome, " + "Paul" + "!");
        } else {
            welcomeText.setText("Welcome, " + userName + "!");
        }


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        vibrate();
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
                //searchNewMessages();

                loadOut();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //

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


    public void connectionsActivity() {
//        Intent i = new Intent(this, MainActivityWear.class);
//        startActivity(i);

        this.finish();
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 500, 50, 300};
        //-1 to not repeat
        final int indexInPatternToRepeat = -1;
        vibrator.vibrate(pattern, indexInPatternToRepeat);
    }
}
