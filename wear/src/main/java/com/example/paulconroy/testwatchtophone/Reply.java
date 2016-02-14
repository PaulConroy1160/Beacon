package com.example.paulconroy.testwatchtophone;

import java.io.Serializable;

/**
 * Created by paulconroy on 03/02/2016.
 */
public class Reply implements Serializable {

    private String receiver;
    private String message;

    public Reply() {
        this.receiver = "";
        this.message = "";
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
