package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulconroy.testwatchtophone.Model.Message;
import com.example.paulconroy.testwatchtophone.Model.Model;
import com.google.android.gms.gcm.Task;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Handler;

import org.json.JSONObject;

import Database.DB;

/**
 * Created by paulconroy on 02/01/2016.
 */
public class ChatActivity extends AppCompatActivity {

    private Model mModel;
    private List messageList;
    private int compareSize;
    private String compareText;
    private ListView chatList;
    private DB db;
    private ParseUser user;
    private EditText messageField;
    private Button sendBTN;
    private ListAdapter adapter;
    private String targetConnection;
    private Calendar c = Calendar.getInstance();
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private DrawerLayout mDrawerLayout;
    private TextView nameText;
    private Typeface typeFace;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        addDrawerItems();
        setupDrawer();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i);
                }

                if (position == 1) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    ChatActivity.this.finish();
                }

                if (position == 2) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    connectDialog();
                }
            }
        });

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252339")));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.logo_ab);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252339")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setCustomView(R.layout.ab_layout);

        user = ParseUser.getCurrentUser();
        //Log.d("user is",user.get("userName").toString());
        messageField = (EditText) findViewById(R.id.messageField);
        messageField.setTypeface(typeFace);
        sendBTN = (Button) findViewById(R.id.sendBTN);
        sendBTN.setTypeface(typeFace);
        sendBTN.setText("....");
        sendBTN.getBackground().setAlpha(128);
        sendBTN.setClickable(false);


        //checkMissedMessages();


        mModel = Model.getInstance();
        targetConnection = mModel.getTargetConnection();
        db = new DB(this);

        nameText = (TextView) findViewById(R.id.nameText);

        nameText.setText(targetConnection);
        nameText.setTypeface(typeFace);


        messageList = new ArrayList<Message>();
        compareSize = 0;

        //REMOVE WHEN TESTING
        messageList = db.getAllMessages();

        chatList = (ListView) findViewById(R.id.chatList);
        chatList.setDivider(null);
        //sendBTN.setImeOptions(EditorInfo.IME_ACTION_DONE);

        getTargetInformation();

        submitValidation();

        checkUpdates();


    }

    private void changeAdapter(List<Message> messageList) {
        messageList = mModel.sortMessage(messageList, user.getUsername());
        adapter = new ChatListAdapter(this,
                R.id.chatList, messageList);
        chatList.setAdapter(adapter);
        chatList.setSelection(adapter.getCount() - 1);
    }

    public void sendMessage(View v) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        Log.d("inside sendmessage", "in here");
        String messageText = messageField.getText().toString();
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
        push.setData(data);

        push.sendInBackground();
        setMessageObject(messageText);

        storeMessage(messageText);
        messageList = db.getAllMessages();

        changeAdapter(messageList);
        messageField.setText("");

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
        //need to change to username
        messageObj.put("sender", user.getUsername());
        messageObj.put("receiver", targetConnection);
        messageObj.put("messageText", messageText);
        messageObj.setACL(acl);

        //time goes here
        messageObj.put("seen", false);


        messageObj.saveInBackground();
    }

    public void checkUpdates() {
        //FIX THIS, RUNS AFTER ACTIVITY IS CLOSED - USE ONSTOP METHOD REMOVE CALLBACKS.
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                searchNewMessages();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(r, 3000);
    }

    public void searchNewMessages() {
        List<Message> compareList = db.getAllMessages();
        if (messageList.size() != compareList.size()) {

            messageList = compareList;
            //messageList = mModel.sortMessage(messageList,user.getUsername());
            changeAdapter(messageList);
        }
    }

    public void checkMissedMessages() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        //need to change to username
        query.whereEqualTo("receiver", user.getUsername());
        //query.whereEqualTo("seen",false);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("alert", "inside done method");
                    for (ParseObject message : objects) {
                        Log.d("alert", "unseen messages objects found");
                        String text = message.get("messageText").toString();
                        storeMessage(text);
                        messageList = db.getAllMessages();
                        //messageList = mModel.sortMessage(messageList,user.getUsername());
                        changeAdapter(messageList);
                        Log.d("messContents", text);

                        message.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("alert", "message deleted");
                            }
                        });
                    }
                }
            }
        });
    }

    public void logOut(View v) {
        //ParseUser.logOut();
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                closeAndDestroyUser();
            }
        });

    }

    public void setTestConnection(View v) {
        ParseObject connection = new ParseObject("Connections");
        connection.put("user1", "ronan");
        connection.put("user2", "brendan");
        connection.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.d("connections", "saved");
                } else {
                    Log.d("connections", "not saved, BOOO!!!");
                }
            }
        });
    }

    public void closeAndDestroyUser() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        this.finish();
    }

    public void getTargetInformation() {

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
                                    loadChatMessages();
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

    public void loadChatMessages() {
        if (!messageList.isEmpty()) {
            //messageList = mModel.sortMessage(messageList,user.getUsername());
            changeAdapter(messageList);
            //loadAnimation();
        } else {
            Toast.makeText(this, "No Messages available", Toast.LENGTH_LONG).show();
        }
    }

    private void addDrawerItems() {
        String[] osArray = {"My Profile", "Connections", "Search"};
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

    public void submitValidation() {
        //FIX THIS, RUNS AFTER ACTIVITY IS CLOSED - USE ONSTOP METHOD REMOVE CALLBACKS.
        final Handler submitHandler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                checkText();
                submitHandler.postDelayed(this, 500);
            }
        };

        submitHandler.postDelayed(r, 500);
    }

    public void checkText() {

        String text = messageField.getText().toString().trim();

        if (text.length() != 0) {
            sendBTN.setText("SEND");
            sendBTN.setBackgroundColor(Color.parseColor("#f1654c"));
            sendBTN.setClickable(true);
            sendBTN.getBackground().setAlpha(255);
        } else {
            sendBTN.setText("....");
            sendBTN.getBackground().setAlpha(128);
            sendBTN.setClickable(false);

        }

    }

    public void connectDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);


        final TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        title.setTypeface(typeFace);

        final Button viewProfile = (Button) dialog.findViewById(R.id.dialog_view_btn);
        viewProfile.setTypeface(typeFace);
        viewProfile.setVisibility(View.INVISIBLE);

        final EditText connectionUser = (EditText) dialog.findViewById(R.id.text_dialog);
        connectionUser.setTypeface(typeFace);

        handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                final String text = connectionUser.getText().toString().trim();


                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", text);

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, com.parse.ParseException e) {
                        Log.d("there is: ", "" + objects.size() + " found");

                        compareText = text;

                        if (!objects.isEmpty()) {
                            Log.d("user name", "found!");
                            title.setText("USER FOUND");
                            title.setBackgroundColor(Color.parseColor("#1abc9c"));
                            title.setTextColor(Color.parseColor("#ffffff"));
                            viewProfile.setVisibility(View.VISIBLE);
                            viewProfile.setClickable(true);
                        } else {
                            Log.d("user name", "not found...");
                            title.setText("FIND USER");
                            title.setBackgroundColor(Color.parseColor("#252339"));
                            title.setTextColor(Color.parseColor("#f1654c"));
                            viewProfile.setVisibility(View.INVISIBLE);
                            viewProfile.setClickable(false);
                        }
                    }
                });
                handler.postDelayed(this, 300);
            }
        };

        handler.postDelayed(r, 300);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_view_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String username = connectionUser.getText().toString().trim();
                userProfile(username);
                dialog.dismiss();
                // }

            }

        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacksAndMessages(null);
                Log.d("handler has stopped", "dialog dismissed");
            }
        });
        dialog.show();


    }

    private void userProfile(String user) {
        Intent i = new Intent(this, Profile.class);
        i.putExtra("username", user);
        startActivity(i);
    }

}

