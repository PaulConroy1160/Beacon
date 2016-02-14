package com.example.paulconroy.testwatchtophone;

import android.content.Intent;
import android.util.Log;

import com.example.paulconroy.testwatchtophone.Model.Model;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by paulconroy on 30/10/2015.
 */
public class ListenerServiceFromWear extends WearableListenerService{

    private static final String WEARPATH = "/from-wear";
    private Model mModel;


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        mModel = Model.getInstance();

        Log.d("MESSAGE","REACHED");
        if(messageEvent.getPath().equals(WEARPATH)){
            //String beacon_data = new String(messageEvent.getData());


            Reply reply = SerializationUtils.deserialize(messageEvent.getData());

            mModel.setReply(reply);


            Log.d("MESSAGE EVENT","CONNECT!");

            Intent intent = new Intent(this,ConstructBeacon.class);
            //intent.putExtra("message",reply);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
