package com.example.ttett.bean;

import com.example.ttett.Entity.Email;

import java.util.Map;

public class MessageEvent {
    private String message;
    private int user_id;
    private Email email;
    private Map<Integer,Boolean> checkStatus;
//    private List<Integer> message_ids;
//
//    public List<Integer> getMessage_ids() {
//        return message_ids;
//    }
//
//    public void setMessage_ids(List<Integer> message_ids) {
//        this.message_ids = message_ids;
//    }

    public Map<Integer, Boolean> getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Map<Integer, Boolean> checkStatus) {
        this.checkStatus = checkStatus;
    }

    public MessageEvent(String message, Email email) {
        this.message = message;
        this.email = email;
    }

    public MessageEvent(String message,int user_id) {
        this.message = message;
        this.user_id = user_id;
    }

    /**
     * 收件箱选择信息
     * @param message
     * @param checkStatus
     */
    public MessageEvent(String message,Map<Integer,Boolean> checkStatus) {
        this.message = message;
        this.checkStatus = checkStatus;
    }

    public MessageEvent(String message) {
        this.message = message;
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
