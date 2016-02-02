package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Database.DB;

/**
 * Created by paulconroy on 02/01/2016.
 */
public class ConnectionListActivity extends Activity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        user = ParseUser.getCurrentUser();
        mModel = Model.getInstance();
        //Log.d("user is",user.get("userName").toString());

        logo = (ImageView) findViewById(R.id.logoTop);
        title = (TextView) findViewById(R.id.nameText);

        logo.setVisibility(View.GONE);
        title.setVisibility(View.GONE);


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


//        if (!messageList.isEmpty()) {
//            changeAdapter(messageList);
//            //loadAnimation();
//        } else {
//            Toast.makeText(this, "No Messages available", Toast.LENGTH_LONG).show();
//        }

        //setTestConnection();

        loadAnimation();
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

    public void loadAnimation() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_no_trans);
        logo.setAnimation(fadeIn);
        fadeIn.setStartOffset(500);
        logo.setVisibility(View.VISIBLE);

    }




}

