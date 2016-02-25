package com.example.paulconroy.testwatchtophone;

import android.content.Intent;
import android.util.Log;

import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import Database.DB;

/**
 * Created by paulconroy on 30/10/2015.
 */
public class ListenerServiceFromWear extends WearableListenerService {

    private static final String WEARPATH = "/from-wear";
    private Model mModel;
    private Boolean handsFree;
    private DB db;
    private ParseUser user;
    private String targetConnection;
    private String content;


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        mModel = Model.getInstance();
        user = ParseUser.getCurrentUser();

        Log.d("MESSAGE", "REACHED");
        if (messageEvent.getPath().equals(WEARPATH)) {
            //String beacon_data = new String(messageEvent.getData());


            Reply reply = SerializationUtils.deserialize(messageEvent.getData());

            mModel.setReply(reply);

            content = mModel.getReply().getMessage();
            db = new DB(this);

            targetConnection = mModel.getReply().getReceiver();

            handsFree = mModel.getHandsFree();

            if (!handsFree.equals(false)) {
                Intent intent = new Intent(this, ConstructBeacon.class);
                //intent.putExtra("message",reply);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                sendMessage(content);
            }


            Log.d("MESSAGE EVENT", "CONNECT!");
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    private void storeMessage(String message) {
        Message chatMessage = new Message();

        chatMessage.setId(-1);
        chatMessage.setTo(targetConnection);
        chatMessage.setFrom(user.getUsername());
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
        messageObj.put("sender", user.getUsername());
        messageObj.put("receiver", targetConnection);
        messageObj.put("messageText", messageText);
        messageObj.setACL(acl);

        //time goes here
        messageObj.put("seen", false);


        messageObj.saveInBackground();
    }

    public void sendMessage(String content) {


        Log.d("inside sendmessage", "in here");
        String messageText = content;
        ParseQuery query = ParseInstallation.getQuery();
        query.whereEqualTo("userName", targetConnection);

        JSONObject data = new JSONObject();
        try {
            data.put("alert", messageText);
            data.put("from", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }


        ParsePush push = new ParsePush();
        push.setQuery(query);
        //push.setMessage(messageText);
        push.setData(data);

        push.sendInBackground();
        setMessageObject(messageText);

        storeMessage(messageText);
        //chatList.setSelection(adapter.getCount() - 1);

    }
}
