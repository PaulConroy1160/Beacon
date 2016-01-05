package com.example.paulconroy.testwatchtophone.Model;

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
    private String photo;

    public User(){
        id = 0;
        fName = "";
        lName = "";
        email = "";
        password = "";
        bio = "";
        photo = "";
    }

    public User(int i, String f, String l, String u, String e, String p, String b, String pic){
        this.id = i;
        this.fName = f;
        this.lName = l;
        this.username = u;
        this.email = e;
        this.password = p;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
