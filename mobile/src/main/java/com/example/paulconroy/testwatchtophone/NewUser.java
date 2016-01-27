package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 25/01/2016.
 */
public class NewUser extends Activity {

    Typeface typeFace;
    EditText fNameField;
    EditText lNameField;
    EditText userNameField;
    EditText passwordField;
    private ArrayList<EditText> editTextList;
    private TextView title;
    private Boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        title = (TextView) findViewById(R.id.nameText);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        fNameField = (EditText) findViewById(R.id.fNameField);
        lNameField = (EditText) findViewById(R.id.lNameField);
        userNameField = (EditText) findViewById(R.id.userNameField);
        passwordField = (EditText) findViewById(R.id.passwordField);


        editTextList = new ArrayList<EditText>();

        editTextList.add(fNameField);
        editTextList.add(lNameField);
        editTextList.add(passwordField);

        for (final EditText editText : editTextList) {
            editText.setTypeface(typeFace);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String text = editText.getText().toString().trim();

                        //Log.d("text id",""+editText.getId());
                        if (text.length() != 0) {
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.accept_val, 0);
                            valid = false;
                        } else {
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            valid = true;
                        }
                    }
                }
            });
        }

        userNameField.setTypeface(typeFace);

        userNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = userNameField.getText().toString().trim();

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", text);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, com.parse.ParseException e) {
                            Log.d("there is: ", "" + objects.size() + " found");

                            if (!objects.isEmpty()) {
                                userNameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
                                valid = false;
                            } else {
                                userNameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.accept_val, 0);
                                valid = true;
                            }
                        }
                    });
                }
            }
        });






    }

    public void next(View v) {
        String fName = fNameField.getText().toString().trim();
        String lName = lNameField.getText().toString().trim();
        String userName = userNameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();


        validate(fName, lName, userName, password);


    }

    public void validate(String fName, String lName, String userName, String password) {
        //fName.trim();
        //Boolean valid = true;
        if (fName.length() == 0) {

//            fNameField.setHintTextColor(Color.parseColor("#f27878"));
            fNameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
            valid = false;

        }
        if (lName.length() == 0) {
            lNameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
            valid = false;
        }
        if (userName.length() == 0) {
            userNameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
            valid = false;
        }
        if (password.length() == 0) {
            passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.decline_val, 0);
            valid = false;
        }
        if (valid) {
            Intent i = new Intent(this, NewUser2.class);
            i.putExtra("fName", fName);
            i.putExtra("lName", lName);
            i.putExtra("userName", userName);
            i.putExtra("password", password);


            startActivity(i);
            overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
        }
    }


}
