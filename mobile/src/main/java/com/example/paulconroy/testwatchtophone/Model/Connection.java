package com.example.paulconroy.testwatchtophone.Model;

/**
 * Created by paulconroy on 26/01/2016.
 */
public class Connection {

    private int id;
    private String userName;

    public Connection() {
        this.id = 0;
        this.userName = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
