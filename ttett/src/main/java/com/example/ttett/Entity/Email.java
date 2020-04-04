package com.example.ttett.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Email implements Parcelable {
    private int email_id;
    private int user_id;
    private String AuthorizationCode;
    private String type;
    private String address;
    private String name;
    private int message_count;

    public Email() {
    }

    protected Email(Parcel in) {
        email_id = in.readInt();
        user_id = in.readInt();
        AuthorizationCode = in.readString();
        type = in.readString();
        address = in.readString();
        name = in.readString();
        message_count = in.readInt();
    }

    public static final Creator<Email> CREATOR = new Creator<Email>() {
        @Override
        public Email createFromParcel(Parcel in) {
            return new Email(in);
        }

        @Override
        public Email[] newArray(int size) {
            return new Email[size];
        }
    };

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

    public String getAuthorizationCode() {
        return AuthorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        AuthorizationCode = authorizationCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMessage_count() {
        return message_count;
    }

    public void setMessage_count(int message_count) {
        this.message_count = message_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(email_id);
        dest.writeInt(user_id);
        dest.writeString(AuthorizationCode);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeInt(message_count);
    }
}
