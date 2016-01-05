package com.example.paulconroy.testwatchtophone;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by paulconroy on 30/10/2015.
 */
public class ListenerServiceFromWear extends WearableListenerService{

    private static final String WEARPATH = "/from-wear";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        Log.d("MESSAGE","REACHED");
        if(messageEvent.getPath().equals(WEARPATH)){
            String beacon_data = new String(messageEvent.getData());

            Log.d("MESSAGE EVENT","CONNECT!");

            Intent intent = new Intent(this,ConstructBeacon.class);
            intent.putExtra("message",beacon_data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
