package com.example.paulconroy.testwatchtophone.Model;

import android.graphics.Bitmap;

/**
 * Created by paulconroy on 26/01/2016.
 */
public class Connection {

    private int id;
    private String userName;
    private Bitmap pic;

    public Connection() {
        this.id = 0;
        this.userName = "";
        this.pic = null;
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

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
