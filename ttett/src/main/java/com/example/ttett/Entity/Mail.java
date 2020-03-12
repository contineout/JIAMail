package com.example.ttett.Entity;

import java.io.Serializable;
import java.sql.Clob;

public class Mail implements Serializable {
    private int message_id,folder_id;
    private int isRead,isHtml,isSend;
    private long SendDate;
    private String subject,from,to;
    private Clob content;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsHtml() {
        return isHtml;
    }

    public void setIsHtml(int isHtml) {
        this.isHtml = isHtml;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public long getSendDate() {
        return SendDate;
    }

    public void setSendDate(long sendDate) {
        SendDate = sendDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public Clob getContent() {
        return content;
    }

    public void setContent(Clob content) {
        this.content = content;
    }



    public String outDate(String date){
        return  ""+date.substring(4,6) + "-" + date.substring(6,8) + " " + date.substring(8,10) + ":" + date.substring(10,12)+"";
    }

    public String inDate(String date){
        return  "" + date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8)+ " " + date.substring(8,10) + ":" + date.substring(10,12)+"";
    }
}
