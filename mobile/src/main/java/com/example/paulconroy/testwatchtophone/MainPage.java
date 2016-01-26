package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by paulconroy on 10/01/2016.
 */
public class MainPage extends Activity {


    private EditText message;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        currentUser = ParseUser.getCurrentUser();


        message = (EditText) findViewById(R.id.messageField);


    }

    public void sendMessage(View v) {
        Log.d("inside sendmessage", "in here");
        String messageText = message.getText().toString();
        ParseQuery query = ParseInstallation.getQuery();
        query.whereEqualTo("userName", "brendan");
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(messageText);

        setMessageObject(messageText);

        push.sendInBackground();
    }

    public void logOut(View v) {
        ParseUser.logOut();

        currentUser = ParseUser.getCurrentUser();

        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
    }

    public void setMessageObject(String messageText) {
        ParseObject messageObj = new ParseObject("Messages");
        messageObj.put("receiver", "brendan");
        messageObj.put("sender", currentUser.getUsername());
        messageObj.put("messageText", messageText);
        messageObj.put("seen", false);


        messageObj.saveInBackground();
    }
}
