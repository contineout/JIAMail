package com.example.ttett.bean;

import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;

import java.util.List;
import java.util.Map;

public class MessageEvent {
    private String message;
    private int email_id;
    private Email email;
    private Map<Integer,Boolean> checkStatus;
    private String address;
    private String mFlag;
    private List<EmailMessage> emailMessages;

    public List<EmailMessage> getEmailMessages() {
        return emailMessages;
    }

    public void setEmailMessages(List<EmailMessage> emailMessages) {
        this.emailMessages = emailMessages;
    }

    public MessageEvent(String message, Email email, String mFlag, List<EmailMessage> emailMessages) {
        this.message = message;
        this.email = email;
        this.mFlag = mFlag;
        this.emailMessages = emailMessages;
    }

    public String getmFlag() {
        return mFlag;
    }

    public void setmFlag(String mFlag) {
        this.mFlag = mFlag;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public MessageEvent(String message,int email_id) {
        this.message = message;
        this.email_id = email_id;
    }

    public MessageEvent(String message, String address) {
        this.message = message;
        this.address = address;
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

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
