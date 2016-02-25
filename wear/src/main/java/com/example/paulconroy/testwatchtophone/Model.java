package com.example.paulconroy.testwatchtophone;


import java.util.ArrayList;

/**
 * Created by paulconroy on 10/01/2016.
 */
public class Model {

    // create a static instance of com.example.paulconroy.onit.model - set to null
    private static Model instance = null;
    private String messageContent;
    private String sender;
    private String replyString;
    private ArrayList<String> connectionsList;
    private Boolean voiceOnly;


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

    public ArrayList<String> getConnectionsList() {
        return connectionsList;
    }

    public void setConnectionsList(ArrayList<String> connectionsList) {
        this.connectionsList = connectionsList;
    }

    public Boolean getVoiceOnly() {
        return voiceOnly;
    }

    public void setVoiceOnly(Boolean voiceOnly) {
        this.voiceOnly = voiceOnly;
    }
}
