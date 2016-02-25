package com.example.paulconroy.testwatchtophone.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.example.paulconroy.testwatchtophone.Reply;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    ArrayList<String> connectionsStrings = new ArrayList<String>();
    private List<Connection> connectionsList = new ArrayList<Connection>();
    private List<Bitmap> connectionPics = new ArrayList<Bitmap>();
    private Bitmap profilePic;
    private Bitmap targetPlayerProfilePic;
    private Reply reply;
    private Boolean handsFree = false;
    private Boolean wearableFound = false;
    private Boolean onConnectionListChange = false;
    private ParseUser user = ParseUser.getCurrentUser();
    private Boolean loadOutComplete = false;
    private Boolean connectionProblem = false;

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
        filteredList.clear();
        for (Message message : messages) {
            if (message.getTo().equals(targetConnection) || message.getFrom().equals(targetConnection)) {
                filteredList.add(message);
            } else {
                Log.d("Message: ", "from: " + message.getFrom() + " to: " + message.getTo());
            }
        }

        return filteredList;
    }

    public Reply getReply() {
        return this.reply;
    }

    public void setReply(Reply r) {
        this.reply = r;
    }

    public void setHandsFree(Boolean b) {
        this.handsFree = b;
    }

    public Boolean getHandsFree() {
        return this.handsFree;
    }

    public void setWearableFound(Boolean b) {
        this.wearableFound = b;
    }

    public Boolean getWearableFound() {
        return this.wearableFound;
    }


    public List<Bitmap> getConnectionPics() {
        return connectionPics;
    }

    public void addConnectionPic(Bitmap pic) {
        this.connectionPics.add(pic);
    }

    public void removeConnection(Connection conn) {
        this.connectionsList.remove(conn);

    }

    public Boolean getOnConnectionListChange() {
        return onConnectionListChange;
    }

    public void setOnConnectionListChange(Boolean onConnectionListChange) {
        this.onConnectionListChange = onConnectionListChange;
    }

    public void setUserInformation() {
        setLoadOutComplete(false);
        Log.d("inside", "setUserInformation");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //need to change to username
        query.whereEqualTo("userName", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("inside", "setUserInformation");
                    if (!objects.isEmpty()) {
                        ParseFile pFile = (ParseFile) objects.get(0).get("pic");

                        pFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Log.d("success", "data retrieved");

                                    Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    setUserProfile(picture);
                                    searchForConnections();
                                    //connectionsActivity();
                                }
                            }
                        });
                    } else {
                        Log.d("problemo", "no image retrieved");

                    }
                } else {
                    Log.d("problemo", "no data retrieved");
                    setConnectionProblem(true);

                }
            }
        });
    }

    public void searchForConnections() {
        Log.d("inside", "searchForConnections");
        final List<String> connections = new ArrayList<String>();
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
                        Log.d("alert,", "Connections have been found!");

                        for (ParseObject object : objects) {

                            if (object.get("user1").equals(user.getUsername())) {
                                //connections.add(object.get("user2").toString());
                                Connection conn = new Connection();
                                conn.setId(-1);
                                conn.setUserName(object.get("user2").toString());
                                connectionsStrings.add(object.get("user2").toString());
                                getConnectionPic(conn, object.get("user2").toString());

                                //connectionsList.add(conn);
                            } else {
                                Connection conn = new Connection();
                                conn.setId(-1);
                                conn.setUserName(object.get("user1").toString());
                                connectionsStrings.add(object.get("user1").toString());
                                getConnectionPic(conn, object.get("user1").toString());
//                                mModel.addConnection(conn);
                                //connectionsList.add(conn);
                                //connections.add(object.get("user1").toString());
                            }
                            //Log.d("connections are", connectionsList.get(0).getUserName());
                        }

                    }


                    //changeAdapter(connectionsList);

                    setLoadOutComplete(true);


                } else {
                    Log.d("alert", "errors...");
                    setConnectionProblem(true);
                }

            }
        });
    }

    public void getConnectionPic(final Connection conn, String username) {
        Log.d("inside", "setUserInformation");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //need to change to username
        query.whereEqualTo("userName", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("inside", "setUserInformation");
                    if (!objects.isEmpty()) {
                        ParseFile pFile = (ParseFile) objects.get(0).get("pic");

                        pFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Log.d("success", "bitmap received and added");

                                    Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    conn.setPic(picture);
                                    addConnection(conn);
                                    setOnConnectionListChange(true);


                                    //connectionsActivity();
                                }
                            }
                        });
                    } else {
                        Log.d("problemo", "no image retrieved");

                    }
                } else {
                    Log.d("problemo", "no data retrieved");
                    setConnectionProblem(true);
                }
            }
        });
    }

    public ArrayList<String> getConnectionsStrings() {
        return connectionsStrings;
    }

    public void setConnectionsStrings(ArrayList<String> connectionsStrings) {
        this.connectionsStrings = connectionsStrings;
    }

    public Boolean getLoadOutComplete() {
        return loadOutComplete;
    }

    public void setLoadOutComplete(Boolean loadOutComplete) {
        this.loadOutComplete = loadOutComplete;
    }

    public Boolean getConnectionProblem() {
        return connectionProblem;
    }

    public void setConnectionProblem(Boolean connectionProblem) {
        this.connectionProblem = connectionProblem;
    }
}
