package com.example.ttett.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Folder implements Parcelable {
    private int folder_id;
    private int email_id;
    private String folder_name;
    private String message_number;
    private String datetime;

    public Folder() {
    }

    protected Folder(Parcel in) {
        folder_id = in.readInt();
        email_id = in.readInt();
        folder_name = in.readString();
        message_number = in.readString();
        datetime = in.readString();
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getMessage_number() {
        return message_number;
    }

    public void setMessage_number(String message_number) {
        this.message_number = message_number;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(folder_id);
        dest.writeInt(email_id);
        dest.writeString(folder_name);
        dest.writeString(message_number);
        dest.writeString(datetime);
    }
}
