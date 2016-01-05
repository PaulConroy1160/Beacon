package com.example.paulconroy.testwatchtophone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by paulconroy on 30/10/2015.
 */
public class ConstructBeacon extends Activity {

    TextView beaconContents;
    private TextToSpeech tts;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contruct_beacon);
        beaconContents = (TextView) findViewById(R.id.beaconMessage);
        content = getIntent().getStringExtra("message");
        tts = new TextToSpeech(ConstructBeacon.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }

                if (status == TextToSpeech.SUCCESS) {
                    ReadOut(content);
                }
            }
        });




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

}
