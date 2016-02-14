package com.example.paulconroy.testwatchtophone;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addDrawerItems();
        setupDrawer();
        setActionBar();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i);
                }

                if (position == 1) {
                    Intent i = new Intent(getApplicationContext(), ConnectionListActivity.class);
                    startActivity(i);
                }
            }
        });




        user = ParseUser.getCurrentUser();
        mModel = Model.getInstance();
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

        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Connection connection = (Connection) connectionList.getItemAtPosition(position);


                Log.d("list item is:", connection.getUserName());
                mModel.setTargetConnection(connection.getUserName());
                startChat();
            }
        });

    }

    private void changeAdapter(List<Connection> connections) {

        adapter = new ConnectionListAdapter(this,
                R.id.connectionList, connections);
        connectionList.setAdapter(adapter);
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
    }


    private void addDrawerItems() {
        String[] osArray = {"My Profile", "Connections", "Search", "Settings"};
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
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Toast.makeText(this,"item"+id,Toast.LENGTH_LONG).show();
            return true;
        }

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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252339")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_ab);
        getSupportActionBar().setElevation(0);
    }


}



