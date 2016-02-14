package com.example.paulconroy.testwatchtophone;


/**
 * Created by paulconroy on 10/01/2016.
 */
public class Model {

    // create a static instance of com.example.paulconroy.onit.model - set to null
    private static Model instance = null;
    private String messageContent;
    private String sender;
    private String replyString;


    // synchronized locks method
    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public void setMessageContent(String s) {
        this.messageContent = s;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public String getReplyString() {
        return replyString;
    }

    public void setReplyString(String replyString) {
        this.replyString = replyString;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String s) {
        this.sender = s;
    }
}
