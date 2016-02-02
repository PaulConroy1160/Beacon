package com.example.paulconroy.testwatchtophone.Model;

import android.graphics.Bitmap;

/**
 * Created by paulconroy on 02/01/2016.
 */
public class User {

    private int id;
    private String fName;
    private String lName;
    private String username;
    private String email;
    private String password;
    private String bio;
    private Bitmap photo;

    public User(){
        id = 0;
        fName = "";
        lName = "";
        bio = "";
        photo = null;
    }

    public User(int i, String f, String l, String u, String b, Bitmap pic) {
        this.id = i;
        this.fName = f;
        this.lName = l;
        this.username = u;
        this.bio = b;
        this.photo = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
