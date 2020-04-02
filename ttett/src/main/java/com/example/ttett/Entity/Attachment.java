package com.example.ttett.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Attachment implements Parcelable {
    private int email_id,attachment_id;
    private String name,type,size,saveDate;
    private String message_id;
    private String path;

    protected Attachment(Parcel in) {
        email_id = in.readInt();
        attachment_id = in.readInt();
        name = in.readString();
        type = in.readString();
        size = in.readString();
        saveDate = in.readString();
        message_id = in.readString();
        path = in.readString();
    }

    public Attachment(){

    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(email_id);
        dest.writeInt(attachment_id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(size);
        dest.writeString(saveDate);
        dest.writeString(message_id);
        dest.writeString(path);
    }
}
