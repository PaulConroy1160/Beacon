package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityWear extends Activity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int SPEECH_REQUEST_CODE = 1002;
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private TextView mTextView;

    private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;

    private Node mNode;
    private GoogleApiClient mGoogleApiClient;
    private static final String WEAR_PATH = "/from-wear";

    public static final String WEARABLE_MAIN = "wearableMain";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_wear);

        startService(new Intent(this, ListenerService.class));

        mTextView = (TextView) findViewById(R.id.text);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Obtain the DismissOverlayView element
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText("Long Press to dismiss");
        mDismissOverlay.showIntroIfNecessary();

        // Configure a gesture detector
        mDetector = new GestureDetector(MainActivityWear.this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                mDismissOverlay.show();
            }
        });

        displaySpeech();



    }





    private void displaySpeech(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK){
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String message = results.get(0);

            if (message.length() >= 12) {
                String command = message.substring(message.length() - 12, message.length());
                if (command.equalsIgnoreCase("send message")) {
                    String stripMessage = message.substring(0, message.length() - 12);
                    sendMessage(stripMessage);
                }
            } else {
                Toast.makeText(this,"drop this message",Toast.LENGTH_LONG).show();
            }


            mTextView.setText(message);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                        for(Node node:nodes.getNodes()){
                            if(node != null && node.isNearby()){
                                mNode = node;
                                Log.d(WEARABLE_MAIN,"CONNECTED TO: "+node.getDisplayName());
                            }
                        }
                        if(mNode == null){
                            Log.d(WEARABLE_MAIN,"NOT CONNECTED");
                        }
                    }

                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void sendMessage(String message){
        String beacon = message;
        if(mNode != null && mGoogleApiClient != null){
            Wearable.MessageApi.sendMessage(mGoogleApiClient,
                    mNode.getId(),WEAR_PATH,beacon.getBytes())
                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if(!sendMessageResult.getStatus().isSuccess()){
                                Log.d("Message not sent", ""+ sendMessageResult.getStatus().getStatusCode());
                            }
                            else{
                                Log.d(WEARABLE_MAIN,"Message succeeded");
                            }
                        }
                    });
        }
    }
}
