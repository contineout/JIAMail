package com.example.ttett.bean;

import com.example.ttett.Entity.Email;

public class MessageEvent {
    private String message;
    private int user_id;
    private Email email;

    public MessageEvent(String message,Email email) {
        this.message = message;
        this.email = email;
    }

    public MessageEvent(String message,int user_id) {
        this.message = message;
        this.user_id = user_id;
    }

    public MessageEvent() {
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
