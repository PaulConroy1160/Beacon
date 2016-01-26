package com.example.paulconroy.testwatchtophone.Model;

/**
 * Created by paulconroy on 13/01/2016.
 */
public class Message {

    private int id;
    private String from;
    private String to;
    private String message;
    private String time;

    public Message() {
        this.id = 0;
        this.from = "";
        this.to = "";
        this.message = "";
        this.time = "";
    }

    public Message(int i, String f, String t, String m, String ti) {

        this.id = i;
        this.from = f;
        this.to = t;
        this.message = m;
        this.time = ti;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
