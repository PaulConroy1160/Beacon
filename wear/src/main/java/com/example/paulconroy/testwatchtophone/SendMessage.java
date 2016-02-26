package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import adapter.FragmentAdapter;


public class SendMessage extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Node mNode;
    private GoogleApiClient mGoogleApiClient;
    private static final String WEAR_PATH = "/from-wear";
    public static final String WEARABLE_MAIN = "wearableMain";
    private ViewPager mViewPager;
    private static final int SPEECH_REQUEST_CODE = 1002;
    private FragmentAdapter adapter;
    private String messageText;
    private Model mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pager);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mModel = Model.getInstance();

        messageText = getIntent().getStringExtra("message");
        //sender = getIntent().getStringExtra("sender");

        mModel.setMessageContent(messageText);


        mViewPager = (ViewPager) findViewById(R.id.pager);


        displaySpeech();


    }

    public void commitFunction(int position) throws IOException {
        if (position == 0) {
            //Toast.makeText(this, "Reply Function", Toast.LENGTH_LONG).show();
            sendMessage(mModel.getReplyString());
            //passMessage();
            mViewPager.setCurrentItem(1);


        } else if (position == 2) {
            //Toast.makeText(this, "Decline Function", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, Messages.class);
            startActivity(i);
            this.finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String message = results.get(0);
            Log.d("message is", message);

            mModel.setReplyString(message);

            setFragments();

            mViewPager.setCurrentItem(1);
        }
    }

    private void displaySpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }


    public void setFragments() {
        adapter = new FragmentAdapter(
                getFragmentManager());
        adapter.addFragment(new AcceptFragment());
        adapter.addFragment(new ReplyFragment());
        adapter.addFragment(new DeclineFragment());

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                try {
                    commitFunction(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                        for (Node node : nodes.getNodes()) {
                            if (node != null && node.isNearby()) {
                                mNode = node;
                                Log.d(WEARABLE_MAIN, "CONNECTED TO: " + node.getDisplayName());
                            }
                        }
                        if (mNode == null) {
                            Log.d(WEARABLE_MAIN, "NOT CONNECTED");
                        }
                    }

                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "unable to reach phone", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    public void sendMessage(String message) throws IOException {
        //String beacon = message;

        Reply reply = new Reply();
        reply.setReceiver(mModel.getSender());
        reply.setMessage(message);

        Log.d("messages being sent now are:", message);

        byte[] data = SerializationUtils.serialize(reply);


        if (mNode != null && mGoogleApiClient != null) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient,
                    mNode.getId(), WEAR_PATH, data)
                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.d("Message not sent", "" + sendMessageResult.getStatus().getStatusCode());
                                endActivity();
                            } else {
                                Log.d(WEARABLE_MAIN, "Message succeeded");
                                endActivity();
                            }
                        }
                    });
        }
    }

    public static byte[] serialize(Object r) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(r);
        return out.toByteArray();
    }

    private void endActivity() {
        Intent i = new Intent(this, DashBoard.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
        this.finish();
    }
}
