package com.example.ttett.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class EmailMessage implements Parcelable {
    private int id;
    private String message_id;
    private int email_id;
    private int user_id;
    private int folder_id;
    private int isRead,isDelete,isSend,isStar,isAttachment;
    private String message_text;
    private String SendDate;
    private String subject,from,to,cc,bcc;
    private String content;
    private String attachment;

    public int getIsAttachment() {
        return isAttachment;
    }

    public void setIsAttachment(int isAttachment) {
        this.isAttachment = isAttachment;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public EmailMessage(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected EmailMessage(Parcel in) {
        id = in.readInt();
        message_id = in.readString();
        email_id = in.readInt();
        user_id = in.readInt();
        folder_id = in.readInt();
        isRead = in.readInt();
        isDelete = in.readInt();
        isSend = in.readInt();
        isStar = in.readInt();
        message_text = in.readString();
        SendDate = in.readString();
        subject = in.readString();
        from = in.readString();
        to = in.readString();
        cc = in.readString();
        bcc = in.readString();
        content = in.readString();
        isAttachment = in.readInt();
        attachment = in.readString();
    }

    public static final Creator<EmailMessage> CREATOR = new Creator<EmailMessage>() {
        @Override
        public EmailMessage createFromParcel(Parcel in) {
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.id = in.readInt();
            emailMessage.message_id = in.readString();
            emailMessage.email_id = in.readInt();
            emailMessage.user_id = in.readInt();
            emailMessage.folder_id = in.readInt();
            emailMessage.isRead = in.readInt();
            emailMessage.isDelete = in.readInt();
            emailMessage.isSend = in.readInt();
            emailMessage.isStar = in.readInt();
            emailMessage.message_text = in.readString();
            emailMessage.SendDate = in.readString();
            emailMessage.subject = in.readString();
            emailMessage.from = in.readString();
            emailMessage.to = in.readString();
            emailMessage.cc = in.readString();
            emailMessage.bcc = in.readString();
            emailMessage.content = in.readString();
            emailMessage.attachment = in.readString();
            emailMessage.isAttachment = in.readInt();
            return emailMessage;
        }

        @Override
        public EmailMessage[] newArray(int size) {
            return new EmailMessage[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
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

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(message_id);
        dest.writeInt(email_id);
        dest.writeInt(user_id);
        dest.writeInt(folder_id);
        dest.writeInt(isRead);
        dest.writeInt(isDelete);
        dest.writeInt(isSend);
        dest.writeInt(isStar);
        dest.writeString(message_text);
        dest.writeString(SendDate);
        dest.writeString(subject);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(cc);
        dest.writeString(bcc);
        dest.writeString(content);
        dest.writeInt(isAttachment);
        dest.writeString(attachment);
    }

    public EmailMessage(int id,String message_id, int email_id, int user_id, int folder_id,int isStar,int isRead, int isDelete, int isSend,int isAttachment, String message_text, String sendDate, String subject, String from, String to, String cc, String bcc, String content,String attachment) {
        this.id = id;
        this.message_id = message_id;
        this.email_id = email_id;
        this.user_id = user_id;
        this.folder_id = folder_id;
        this.isRead = isRead;
        this.isDelete = isDelete;
        this.isSend = isSend;
        this.isStar = isStar;
        this.message_text = message_text;
        SendDate = sendDate;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
        this.isAttachment = isAttachment;
        this.attachment = attachment;
    }
}
