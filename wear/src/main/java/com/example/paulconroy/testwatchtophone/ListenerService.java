package com.example.paulconroy.testwatchtophone;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by paulconroy on 06/01/2016.
 */
public class ListenerService extends WearableListenerService {

    private static final String WEARABLE_DATA_PATH = "/wearable_data";

    private int notificationId = 001;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "Inside wearable service");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {


        DataMap dataMap;
        Log.d("from wearable", "data has changed");
        for (DataEvent event : dataEvents) {

            if (event.getType() == DataEvent.TYPE_CHANGED) {
                //check the data path

                String path = event.getDataItem().getUri().getPath();
                Log.d("from wearable", "path checking");

                if (path.equals(WEARABLE_DATA_PATH)) {
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    Log.d("on wearable", "Received on watch: " + dataMap);
                    //Toast.makeText(this, "hey", Toast.LENGTH_LONG).show();
                    String operation = dataMap.getString("operation");
                    String content = dataMap.getString("message");
                    String senderUserName = dataMap.getString("from");

                    if (operation.equals("greeting")) {
                        loadGreeting(content);
                    }
                    if (operation.equals("push")) {
                        sendInformation(content, senderUserName);
                    }


                    //sendNotification(content);
                }
            }

            ;
        }
    }

    public void sendNotification(String content) {

        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivityWear.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.notificationicon)
                        .setContentTitle("Beacon")
                        .setContentText(content)
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public void sendInformation(String t, String sender) {
        Intent i = new Intent(this, DisplayMessage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("information", t);
        i.putExtra("sender", sender);
        Log.d("message = ", t);
        startActivity(i);
    }

    public void loadGreeting(String t) {
        Intent i = new Intent(this, Greeting.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("information", t);
        Log.d("message = ", t);
        startActivity(i);
    }
}

