package com.example.paulconroy.testwatchtophone.Model;

import android.graphics.Bitmap;

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
    private List<Connection> connectionsList = new ArrayList<Connection>();
    private Bitmap profilePic;
    private Bitmap targetPlayerProfilePic;

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

    public void setUserProfile(Bitmap pic) {
        this.profilePic = pic;
    }

    public Bitmap getUserProfile() {
        return this.profilePic;
    }

    public void setTargetPlayerProfilePic(Bitmap pic) {
        this.targetPlayerProfilePic = pic;
    }

    public Bitmap getTargetPlayerProfilePic() {
        return this.targetPlayerProfilePic;
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

    public void addConnection(Connection conn) {
        this.connectionsList.add(conn);
    }

    public void emptyConnections() {
        this.connectionsList.clear();
    }

    public List<Connection> getConnections() {
        return this.connectionsList;
    }

    public List<Message> sortMessage(List<Message> messages, String user) {
        List<Message> filteredList = new ArrayList<Message>();
        for (Message message : messages) {
            if (message.getTo().equals(user)) {
                filteredList.add(message);
            }
        }

        return filteredList;
    }




}
