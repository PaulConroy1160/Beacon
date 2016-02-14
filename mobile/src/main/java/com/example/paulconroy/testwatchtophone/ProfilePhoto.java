package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by paulconroy on 28/01/2016.
 */
public class ProfilePhoto extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mProfilePic;
    private Bitmap imageBitmap;
    private ParseFile pFile;
    private String fName;
    private String lName;
    private String userName;
    private String password;
    private String bioText;
    private ParseInstallation install;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        mProfilePic = (ImageView) findViewById(R.id.profilepic);
        Intent i = getIntent();

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        userName = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        bioText = getIntent().getStringExtra("bioText");


        submit = (Button) findViewById(R.id.submit);

        install = ParseInstallation.getCurrentInstallation();

        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mProfilePic.setImageBitmap(imageBitmap);
        }
    }


    public void submit(View v) {
        submit.setText("....");
        submit.setClickable(false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        pFile = new ParseFile("ProfileImage.png", stream.toByteArray());

        try {
            pFile.save();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("problem", "can't save pic file");
        }

        ParseUser pUser = new ParseUser();
        pUser.setUsername(userName);
        pUser.setPassword(password);
        install.put("userName", userName);
        install.saveInBackground();

        pUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Problems?", "Nope!! none at all");

                    ParseObject user = new ParseObject("User");
                    user.put("fName", fName);
                    user.put("lName", lName);
                    user.put("userName", userName);
                    user.put("bio", bioText);
                    user.put("pic", pFile);

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Log.d("Success!!", "User and Profile pic added!");
                                returnToLogin();

                            }
                        }
                    });

                } else {
                    Log.d("Problems?", "Sadly...");
                }
            }
        });


    }

    public void returnToLogin() {
        //Toast.makeText(this, "User created! Welcome, " + userName, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, Loading.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
        submit.setText("Done!");
        submit.setClickable(true);


    }
}
