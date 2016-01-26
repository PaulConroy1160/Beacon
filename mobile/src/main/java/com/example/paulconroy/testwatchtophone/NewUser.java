package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        editTextList.add(userNameField);
        editTextList.add(passwordField);

        for (EditText editText : editTextList) {
            editText.setTypeface(typeFace);
        }


    }

    public void next(View v) {
        String fName = fNameField.getText().toString();
        String lName = lNameField.getText().toString();
        String userName = userNameField.getText().toString();
        String password = passwordField.getText().toString();

        Intent i = new Intent(this, NewUser2.class);
        i.putExtra("fName", fName);
        i.putExtra("lName", lName);
        i.putExtra("userName", userName);
        i.putExtra("password", password);


        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
    }


}
