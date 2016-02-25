package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Connection;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 02/02/2016.
 */
public class Profile extends AppCompatActivity {

    private ImageView profilePic;
    private Bitmap pic;
    private Bitmap imageBitmap;
    private Model mModel;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private DrawerLayout mDrawerLayout;
    private Button connectBTN;
    private ParseUser user;
    private TextView bioView;
    private TextView fullName;
    private TextView usernameField;
    private String bioText;
    private String fullNameText;
    private String userNameText;
    private Boolean isConnected = false;
    private Typeface typeFace;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String btnFunction = "";
    private Handler handler;
    private String compareText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mModel = Model.getInstance();

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        user = ParseUser.getCurrentUser();

        addDrawerItems();
        setupDrawer();
        setActionBar();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Profile.this.finish();
                }
            }
        });


        profilePic = (ImageView) findViewById(R.id.profilePic);
        connectBTN = (Button) findViewById(R.id.connectBTN);
        connectBTN.setTypeface(typeFace);
        fullName = (TextView) findViewById(R.id.fullname);
        fullName.setTypeface(typeFace);
        usernameField = (TextView) findViewById(R.id.username);
        usernameField.setTypeface(typeFace);


        bioView = (TextView) findViewById(R.id.bioText);
        bioView.setTypeface(typeFace);


        if (getIntent().getStringExtra("username") != null) {
            if (getIntent().getStringExtra("username").equals(ParseUser.getCurrentUser().getUsername())) {
                String username = ParseUser.getCurrentUser().getUsername();
                getUserBio(username);
                pic = mModel.getUserProfile();
                profilePic.setImageBitmap(pic);
                connectBTN.setText("EDIT PHOTO");
                btnFunction = "edit";

            } else {
                String username = getIntent().getStringExtra("username");
                btnFunction = "connect";
                mModel.setTargetConnection(username);
                getUserBio(username);
                getTargetPic(username);
                searchForConnections(username);
            }

        } else {
            String username = ParseUser.getCurrentUser().getUsername();
            getUserBio(username);
            pic = mModel.getUserProfile();
            profilePic.setImageBitmap(pic);
            connectBTN.setText("EDIT PHOTO");
            btnFunction = "edit";

        }

        connectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFunction.equals("connect")) {
                    setConnection();
                }
                if (btnFunction.equals("save")) {
                    updatePhoto(imageBitmap);
                }
                if (btnFunction.equals("edit")) {
                    dispatchTakePictureIntent();
                }
            }
        });


    }

    private void addDrawerItems() {
        String[] osArray = {"Connections"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }

    private void checkUser() {
        //place check here
        //hide connect button if it is current user
    }

    public void getUserBio(String username) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //need to change to username
        query.whereEqualTo("userName", username);
        //query.whereEqualTo("seen",false);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("alert", "inside done method");
                    for (ParseObject user : objects) {
                        Log.d("alert", "Bio not found");
                        bioText = user.get("bio").toString();
                        String fName = user.get("fName").toString();
                        String lName = user.get("lName").toString();
                        String username = user.get("userName").toString();
                        userNameText = username;

                        fullNameText = fName + " " + lName;
                        bioView.setText(bioText);
                        usernameField.setText(username);
                        fullName.setText(fullNameText);
                        bioView.setText(bioText);
                        Log.d("messContents", bioText);

                    }
                }
            }
        });
    }

    public void getTargetPic(String username) {

        Log.d("inside", "getTargetInformation");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //need to change to username
        query.whereEqualTo("userName", mModel.getTargetConnection());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("inside", "getTargetInformation");
                    if (!objects.isEmpty()) {
                        ParseFile pFile = (ParseFile) objects.get(0).get("pic");

                        pFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Log.d("success", "data retrieved");

                                    Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    mModel.setTargetPlayerProfilePic(picture);

                                    profilePic.setImageBitmap(picture);
                                }
                            }
                        });
                    } else {
                        Log.d("problemo", "no image retrieved");

                    }
                } else {
                    Log.d("problemo", "no data retrieved");
                }
            }
        });
    }

    public void searchForConnections(final String targetUsername) {
        Log.d("inside", "searchForConnections");
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Connections");
        //need to change to username
        query1.whereEqualTo("user1", user.getUsername());

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Connections");
        query2.whereEqualTo("user2", user.getUsername());

        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.isEmpty()) {
                        Log.d("alert", "no connections found...");

                    } else {

                        for (ParseObject object : objects) {
                            if (object.get("user1").equals(user.getUsername()) && object.get("user2").equals(targetUsername)) {
                                connectBTN.setClickable(false);
                                connectBTN.setVisibility(View.INVISIBLE);
                            } else if (object.get("user2").equals(user.getUsername()) && object.get("user1").equals(targetUsername)) {
                                connectBTN.setClickable(false);
                                connectBTN.setVisibility(View.INVISIBLE);
                            }

                        }


                        //changeAdapter(connectionsList);

                    }
                } else {
                    Log.d("alert", "errors...");
                }

            }
        });
    }

    public void setConnection() {
        connectBTN.setClickable(false);
        final ParseObject connection = new ParseObject("Connections");
        connection.put("user1", user.getUsername());
        connection.put("user2", userNameText);
        connection.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.d("connections", "saved");
                    Connection conn = new Connection();
                    conn.setId(-1);
                    conn.setUserName(userNameText);
                    mModel.getConnectionPic(conn, userNameText);
                    connectBTN.setText("CONNECTED!");
                    connectBTN.setClickable(false);
                    connectBTN.setBackgroundColor(Color.parseColor("#1abc9c"));
                    Animation anim = AnimationUtils.loadAnimation(Profile.this, R.anim.fade_out);
                    connectBTN.setAnimation(anim);
                    anim.start();
                    connectBTN.setVisibility(View.INVISIBLE);
                } else {
                    Log.d("connections", "not saved, BOOO!!!");
                    connectBTN.setVisibility(View.VISIBLE);
                }
            }
        });
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
            profilePic.setImageBitmap(imageBitmap);
            connectBTN.setText("SAVE PHOTO?");
            connectBTN.setBackgroundColor(Color.parseColor("#1abc9c"));
            btnFunction = "save";
        }
    }

    private void updatePhoto(final Bitmap b) {
        connectBTN.setText("SAVING...");
        connectBTN.setClickable(false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final ParseFile pFile = new ParseFile("ProfileImage.png", stream.toByteArray());

        try {
            pFile.save();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("problem", "can't save pic file");
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //need to change to username
        query.whereEqualTo("userName", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (!objects.isEmpty()) {
                        objects.get(0).put("pic", pFile);

                        objects.get(0).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("alert", "image has been updated");
                                mModel.setUserProfile(b);
                                btnFunction = "edit";
                                connectBTN.setText("EDIT PROFILE");
                                connectBTN.setBackgroundColor(Color.parseColor("#f1654c"));
                                connectBTN.setClickable(true);
                            }
                        });
                    }
                }
            }
        });


    }

    private void setActionBar() {
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252339")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.ab_layout);
    }

//    public void connectDialog() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.custom_dialog);
//
//
//        final TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
//        title.setTypeface(typeFace);
//
//        final Button viewProfile = (Button) dialog.findViewById(R.id.dialog_view_btn);
//        viewProfile.setTypeface(typeFace);
//        viewProfile.setVisibility(View.INVISIBLE);
//
//        final EditText connectionUser = (EditText) dialog.findViewById(R.id.text_dialog);
//        connectionUser.setTypeface(typeFace);
//        handler = new Handler();
//
//        Runnable r = new Runnable() {
//            public void run() {
//                final String text = connectionUser.getText().toString().trim();
//
//
//                ParseQuery<ParseUser> query = ParseUser.getQuery();
//                query.whereEqualTo("username", text);
//
//                query.findInBackground(new FindCallback<ParseUser>() {
//                    @Override
//                    public void done(List<ParseUser> objects, com.parse.ParseException e) {
//                        Log.d("there is: ", "" + objects.size() + " found");
//
//                        compareText = text;
//
//                        if (!objects.isEmpty()) {
//                            Log.d("user name", "found!");
//                            title.setText("USER FOUND");
//                            title.setBackgroundColor(Color.parseColor("#1abc9c"));
//                            title.setTextColor(Color.parseColor("#ffffff"));
//                            viewProfile.setVisibility(View.VISIBLE);
//                            viewProfile.setClickable(true);
//                        } else {
//                            Log.d("user name", "not found...");
//                            title.setText("FIND USER");
//                            title.setBackgroundColor(Color.parseColor("#252339"));
//                            title.setTextColor(Color.parseColor("#f1654c"));
//                            viewProfile.setVisibility(View.INVISIBLE);
//                            viewProfile.setClickable(false);
//                        }
//                    }
//                });
//                handler.postDelayed(this, 300);
//            }
//        };
//
//        handler.postDelayed(r, 300);
//
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_view_btn);
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                final String username = connectionUser.getText().toString().trim();
//                userProfile(username);
//                dialog.dismiss();
//                // }
//
//            }
//
//        });
//
//
//
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                handler.removeCallbacksAndMessages(null);
//                Log.d("handler has stopped","dialog dismissed");
//            }
//        });
//        dialog.show();
//    }

//    private void userProfile(String user){
//        Intent i = new Intent(this, Profile.class);
//        i.putExtra("username",user);
//        startActivity(i);
//    }

}
