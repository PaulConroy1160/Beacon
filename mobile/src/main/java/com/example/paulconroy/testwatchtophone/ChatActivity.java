package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.google.android.gms.gcm.Task;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
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

import android.os.Handler;

import Database.DB;

/**
 * Created by paulconroy on 02/01/2016.
 */
public class ChatActivity extends Activity {

    private Model mModel;
    private List messageList;
    private int compareSize;
    private ListView chatList;
    private DB db;
    private ParseUser user;
    private EditText messageField;
    private Button sendBTN;
    private ListAdapter adapter;
    private String targetConnection;
    private Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = ParseUser.getCurrentUser();
        //Log.d("user is",user.get("userName").toString());
        messageField = (EditText) findViewById(R.id.messageField);
        sendBTN = (Button) findViewById(R.id.sendBTN);

        checkMissedMessages();


        mModel = Model.getInstance();
        targetConnection = mModel.getTargetConnection();
        db = new DB(this);


        messageList = new ArrayList<Message>();
        compareSize = 0;

        //REMOVE WHEN TESTING
        messageList = db.getAllMessages();


        chatList = (ListView) findViewById(R.id.chatList);
        sendBTN.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (!messageList.isEmpty()) {
            changeAdapter(messageList);
            //loadAnimation();
        } else {
            Toast.makeText(this, "No Messages available", Toast.LENGTH_LONG).show();
        }

        checkUpdates();


    }

    private void changeAdapter(List<Message> messageList) {

        adapter = new ChatListAdapter(this,
                R.id.chatList, messageList);
        chatList.setAdapter(adapter);
        chatList.setSelection(adapter.getCount() - 1);
    }

    public void sendMessage(View v) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        Log.d("inside sendmessage", "in here");
        String messageText = messageField.getText().toString();
        ParseQuery query = ParseInstallation.getQuery();
        query.whereEqualTo("userName", targetConnection);
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(messageText);

        push.sendInBackground();
        setMessageObject(messageText);

        storeMessage(messageText);
        messageList = db.getAllMessages();
        changeAdapter(messageList);
        messageField.setText("");
        //chatList.setSelection(adapter.getCount() - 1);

    }

    private void storeMessage(String message) {
        Message chatMessage = new Message();

        chatMessage.setId(-1);
        chatMessage.setTo(targetConnection);
        chatMessage.setFrom("PaulConroy");
        chatMessage.setMessage(message);
        chatMessage.setTime("XXXX");

        db.addMessage(chatMessage);

    }

    public void setMessageObject(String messageText) {
        //required to set the read and write access to users
        ParseACL acl = new ParseACL();
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);


        ParseObject messageObj = new ParseObject("Messages");
        //messageObj.put("sender", user.getUsername());
        //need to change to username
        messageObj.put("sender", "brenan");
        messageObj.put("receiver", targetConnection);
        messageObj.put("messageText", messageText);
        messageObj.setACL(acl);

        //time goes here
        messageObj.put("seen", false);


        messageObj.saveInBackground();
    }

    public void checkUpdates() {
        //FIX THIS, RUNS AFTER ACTIVITY IS CLOSED - USE ONSTOP METHOD REMOVE CALLBACKS.
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                searchNewMessages();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(r, 3000);
    }

    public void searchNewMessages() {
        List<Message> compareList = db.getAllMessages();
        if (messageList.size() != compareList.size()) {

            messageList = compareList;
            changeAdapter(messageList);
        }
    }

    public void checkMissedMessages() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        //need to change to username
        query.whereEqualTo("receiver", "brendan");
        //query.whereEqualTo("seen",false);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("alert", "inside done method");
                    for (ParseObject message : objects) {
                        Log.d("alert", "unseen messages objects found");
                        String text = message.get("messageText").toString();
                        storeMessage(text);
                        messageList = db.getAllMessages();
                        changeAdapter(messageList);
                        Log.d("messContents", text);

                        message.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("alert", "message deleted");
                            }
                        });
                    }
                }
            }
        });
    }

    public void logOut(View v) {
        //ParseUser.logOut();
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                closeAndDestroyUser();
            }
        });

    }

    public void setTestConnection(View v) {
        ParseObject connection = new ParseObject("Connections");
        connection.put("user1", "ronan");
        connection.put("user2", "brendan");
        connection.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.d("connections", "saved");
                } else {
                    Log.d("connections", "not saved, BOOO!!!");
                }
            }
        });
    }

    public void closeAndDestroyUser() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        this.finish();
    }





}

