package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 14/02/2016.
 */
public class Connection {

    private int id;
    private String username;

    public Connection() {
        this.id = 0;
        this.username = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
