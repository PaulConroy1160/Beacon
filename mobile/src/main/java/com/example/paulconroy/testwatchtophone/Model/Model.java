package com.example.paulconroy.testwatchtophone.Model;

import com.parse.ParseInstallation;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulconroy on 10/01/2016.
 */
public class Model {

    // create a static instance of com.example.paulconroy.onit.model - set to null
    private static Model instance = null;
    private String targetConnection = null;

    // synchronized locks method
    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    private ParseQuery query;
    private List<String> friendId = new ArrayList<String>();

    public void setQuery(ParseQuery q) {
        this.query = q;
    }

    public ParseQuery getQuery() {
        return this.query;
    }

    public void addFriendId(String id) {
        this.friendId.add(id);
    }

    public List<String> getFriendIds() {
        return this.friendId;
    }

    public void setTargetConnection(String conn) {
        this.targetConnection = conn;
    }

    public String getTargetConnection() {
        return this.targetConnection;
    }




}
