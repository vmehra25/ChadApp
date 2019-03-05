package com.example.chadapp;

public class Messages {

    String message,sender,reciever,id;

    public String getMessage() {
        return message;
    }

    public Messages() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Messages(String message, String sender, String reciever, String id) {
        this.message = message;
        this.sender = sender;
        this.reciever = reciever;
        this.id = id;
    }
}
