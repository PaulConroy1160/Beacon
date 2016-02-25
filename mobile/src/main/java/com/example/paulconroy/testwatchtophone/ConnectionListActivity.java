package com.example.paulconroy.testwatchtophone;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Connection;
import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.google.android.gms.nearby.connection.Connections;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Database.DB;


/**
 * Created by paulconroy on 02/01/2016.
 */
public class ConnectionListActivity extends AppCompatActivity {

    private Model m;
    //private List connectionList;
    private int compareSize;
    private List<Connection> connectionsList;
    private ListView connectionList;
    private DB db;
    private ParseUser user;
    private ImageView logo;
    private TextView title;
    private Handler handler;
    private Handler handler2;
    private String compareText;
    //private List<ParseObject> connections;
    private EditText messageField;
    private Button sendBTN;
    private ListAdapter adapter;
    private Calendar c = Calendar.getInstance();
    private Model mModel;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private DrawerLayout mDrawerLayout;
    private Typeface typeFace;
    private String[] menuItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        mModel = Model.getInstance();

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        db = new DB(this);



        addDrawerItems();
        setupDrawer();
        setActionBar();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
                }
                if (position == 1) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    connectDialog();
                }
                if (position == 2) {
                    //enable handsfree
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    handsFreeToggle();
                }
                if (position == 3) {
                    logOut();
                }
            }
        });


        user = ParseUser.getCurrentUser();

        //Log.d("user is",user.get("userName").toString());

        title = (TextView) findViewById(R.id.nameText);



        connectionsList = new ArrayList<Connection>();


        m = Model.getInstance();
        db = new DB(this);

        //searchForConnections();
        //messageList = new ArrayList<Message>();
        compareSize = 0;

        //REMOVE WHEN TESTING
        //messageList = db.getAllMessages();


        connectionList = (ListView) findViewById(R.id.connectionList);


        connectionsList = mModel.getConnections();

        changeAdapter(connectionsList);

//        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Connection connection = (Connection) connectionList.getItemAtPosition(position);
//
//
//                Log.d("list item is:", connection.getUserName());
//                mModel.setTargetConnection(connection.getUserName());
//                startChat();
//
//
//            }
//        });


        handler2 = new Handler();

        Runnable r2 = new Runnable() {
            public void run() {

                if (mModel.getOnConnectionListChange() == true) {
                    Log.d("inside", "if r2 runnable");
                    mModel.setOnConnectionListChange(false);
                    changeAdapter(mModel.getConnections());

                }
                connectionList.deferNotifyDataSetChanged();
                handler2.postDelayed(this, 300);
            }
        };

        handler2.postDelayed(r2, 300);

    }

    private void changeAdapter(List<Connection> connections) {
        Log.d("inside", "changeAdapter");
        adapter = new ConnectionListAdapter(this,
                R.id.connectionList, connections);
        connectionList.setAdapter(adapter);
        connectionList.deferNotifyDataSetChanged();
    }


    public void logOut(View v) {
        ParseUser.logOut();
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        this.finish();
    }

    public void setTestConnection() {
        ParseObject connection = new ParseObject("Connections");
        connection.put("user1", user.getUsername());
        connection.put("user2", "hix");
        connection.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.d("connections", "saved");
                    Toast.makeText(getApplication(), "CONNECTED TO HIX!", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("connections", "not saved, BOOO!!!");
                }
            }
        });
    }

    public void searchForConnections() {
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
                                connectionsList.add(conn);
                            } else {
                                Connection conn = new Connection();
                                conn.setId(-1);
                                conn.setUserName(object.get("user1").toString());
                                connectionsList.add(conn);
                                //connections.add(object.get("user1").toString());
                            }
                            Log.d("connections are", connectionsList.get(0).getUserName());
                        }
                    }


                    changeAdapter(connectionsList);
                } else {
                    Log.d("alert", "errors...");
                }
            }
        });
    }

    public void startChat() {
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
    }


    private void addDrawerItems() {
        String voiceEnable;

        if (!mModel.getWearableFound()) {
            voiceEnable = "OFF";
        } else {
            voiceEnable = "ON";
        }
        String[] osArray = {"My Profile", "Search", "WEARABLE [" + voiceEnable + "]", "Logout"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }

    private void setActionBar() {
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252339")));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.logo_ab);
//        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252339")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.ab_layout);
    }

    private void handsFreeToggle() {
        if (!mModel.getWearableFound()) {
            Toast.makeText(this, "NO WEARABLE DISCOVERED", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "WEARABLE DISCOVERED", Toast.LENGTH_LONG).show();
        }
    }

    public void connectDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);


        final TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        title.setTypeface(typeFace);

        final Button viewProfile = (Button) dialog.findViewById(R.id.dialog_view_btn);
        viewProfile.setTypeface(typeFace);
        viewProfile.setVisibility(View.INVISIBLE);

        final EditText connectionUser = (EditText) dialog.findViewById(R.id.text_dialog);
        connectionUser.setTypeface(typeFace);
        handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                final String text = connectionUser.getText().toString().trim();


                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", text);

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, com.parse.ParseException e) {
                        Log.d("there is: ", "" + objects.size() + " found");

                        compareText = text;

                        if (!objects.isEmpty()) {
                            Log.d("user name", "found!");
                            title.setText("USER FOUND");
                            title.setBackgroundColor(Color.parseColor("#1abc9c"));
                            title.setTextColor(Color.parseColor("#ffffff"));
                            viewProfile.setVisibility(View.VISIBLE);
                            viewProfile.setClickable(true);
                        } else {
                            Log.d("user name", "not found...");
                            title.setText("FIND USER");
                            title.setBackgroundColor(Color.parseColor("#252339"));
                            title.setTextColor(Color.parseColor("#f1654c"));
                            viewProfile.setVisibility(View.INVISIBLE);
                            viewProfile.setClickable(false);
                        }
                    }
                });
                handler.postDelayed(this, 300);
            }
        };

        handler.postDelayed(r, 300);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_view_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String username = connectionUser.getText().toString().trim();
                userProfile(username);
                dialog.dismiss();
                // }

            }

        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacksAndMessages(null);
                Log.d("handler has stopped", "dialog dismissed");
            }
        });
        dialog.show();
    }

    private void userProfile(String user) {
        Intent i = new Intent(this, Profile.class);
        i.putExtra("username", user);
        startActivity(i);
    }

    public void logOut() {
        ParseUser.logOut();
        this.finish();
    }


}





