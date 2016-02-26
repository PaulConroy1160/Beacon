package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.List;

import DataBase.DB;

/**
 * Created by paulconroy on 13/02/2016.
 */
public class Messages extends Activity {

    private DB db;
    private List<Connection> connections;
    private ListView connectionList;
    private ListAdapter adapter;
    private Model mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        db = new DB(this);

        connections = db.getAllConnections();
        mModel = Model.getInstance();

        connectionList = (ListView) findViewById(R.id.connectionList);
        connectionList.setDivider(null);

        connectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Connection connection = (Connection) connectionList.getItemAtPosition(position);


                Log.d("list item is:", connection.getUsername());


                mModel.setSender(connection.getUsername());

                sendMessage();


            }
        });


        changeAdapter(connections);

    }

    private void changeAdapter(List<Connection> connections) {

        adapter = new ConnectionListAdapter(this,
                R.id.connectionList, connections);
        connectionList.setAdapter(adapter);
    }

    private void sendMessage() {
        Intent i = new Intent(this, SendMessage.class);
        startActivity(i);
    }

}
