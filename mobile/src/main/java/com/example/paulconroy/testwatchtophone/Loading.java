package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.paulconroy.testwatchtophone.Model.Connection;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 27/01/2016.
 */
public class Loading extends Activity {

    private Model mModel;

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

        mModel = Model.getInstance();


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
                //searchNewMessages();
                setUserInformation();

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

    public void searchForConnections() {
        Log.d("inside", "searchForConnections");
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Connections");
        //need to change to username
        query1.whereEqualTo("user1", user.getUsername());

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Connections");
        query2.whereEqualTo("user2", user.getUsername());

        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.isEmpty()) {
                        Log.d("alert", "no connections found...");

                    } else {
                        Log.d("alert,", "Connections have been found!");

                        for (ParseObject object : objects) {

                            if (object.get("user1").equals(user.getUsername())) {
                                //connections.add(object.get("user2").toString());
                                Connection conn = new Connection();
                                conn.setId(-1);
                                conn.setUserName(object.get("user2").toString());
                                mModel.addConnection(conn);
                                //connectionsList.add(conn);
                            } else {
                                Connection conn = new Connection();
                                conn.setId(-1);
                                conn.setUserName(object.get("user1").toString());
                                mModel.addConnection(conn);
                                //connectionsList.add(conn);
                                //connections.add(object.get("user1").toString());
                            }
                            //Log.d("connections are", connectionsList.get(0).getUserName());
                        }

                    }


                    //changeAdapter(connectionsList);
                    loadOut();
                    pushToWatch();

                } else {
                    Log.d("alert", "errors...");
                }

            }
        });
    }

    public void connectionsActivity() {
        Intent i = new Intent(this, ConnectionListActivity.class);
        startActivity(i);

        this.finish();


    }

    public void setUserInformation() {
        Log.d("inside", "setUserInformation");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //need to change to username
        query.whereEqualTo("userName", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("inside", "setUserInformation");
                    if (!objects.isEmpty()) {
                        ParseFile pFile = (ParseFile) objects.get(0).get("pic");

                        pFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Log.d("success", "data retrieved");

                                    Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    mModel.setUserProfile(picture);
                                    searchForConnections();
                                    //connectionsActivity();
                                }
                            }
                        });
                    } else {
                        Log.d("problemo", "no image retrieved");

                    }
                } else {
                    Log.d("problemo", "no data retrieved");
                }
            }
        });
    }

    public void pushToWatch() {
        Log.d("watch", "sending to watch");


        String userName = user.getUsername();

        //final String notificationTitle = json.getString("title").toString();
        final String userNameContent = userName;
        //final String uri = json.getString("uri");

        //Log.d("data1",notificationTitle);
        //Log.d("data2", notificationContent);

        Intent i = new Intent(this, PushToWearable.class);

        i.putExtra("operation", "greeting");

        i.putExtra("content", userNameContent);
        this.startService(i);


    }


}
