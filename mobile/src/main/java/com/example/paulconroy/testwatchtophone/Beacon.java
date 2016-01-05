package com.example.paulconroy.testwatchtophone;

/**
 * Created by paulconroy on 01/11/2015.
 */
public class Beacon {

    private int id;
    private String sender;
    private String message;
    private String location;
    private String date;
    private String likes;

    public Beacon(){
        this.sender = null;
        this.message = null;
        this.location = null;
        this.date = null;
        this.likes = null;
    }


    public Beacon(int i, String s, String m, String l, String d, String li){
        this.id = i;
        this.sender = s;
        this.message = m;
        this.location = l;
        this.date = d;
        this.likes = li;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int i){
        this.id = i;
    }
}
