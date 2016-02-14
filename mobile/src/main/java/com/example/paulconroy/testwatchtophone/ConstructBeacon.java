package com.example.paulconroy.testwatchtophone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import Database.DB;

/**
 * Created by paulconroy on 30/10/2015.
 */
public class ConstructBeacon extends Activity {

    TextView beaconContents;
    private TextToSpeech tts;
    String content;
    private Model mModel;
    private DB db;
    private ParseUser user;
    private String targetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contruct_beacon);

        mModel = Model.getInstance();
        user = ParseUser.getCurrentUser();
        beaconContents = (TextView) findViewById(R.id.beaconMessage);
        //content = getIntent().getStringExtra("message");
        content = mModel.getReply().getMessage();
        db = new DB(this);

        targetConnection = mModel.getReply().getReceiver();
        tts = new TextToSpeech(ConstructBeacon.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }

                if (status == TextToSpeech.SUCCESS) {
                    //Used to read the mesage aloud - used only for handsfree
                    //ReadOut(content);
                }
            }
        });


        sendMessage(content);
        beaconContents.setText(content);
    }

    @Override
    protected void onPause() {
        if(tts==null){
            tts.stop();
            tts.shutdown();
        }

        super.onPause();
    }

    public void ReadOut(String c){
        String text = c;
        Log.i("text", "inside ReadOut method");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }

    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
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
