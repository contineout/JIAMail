package com.example.ttett.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private int email_id;
    private int contact_id;
    private String name,remark,birthday,company,department,position,email,iphone,address;
    private String avatar_color;
    private String last_date;
    private int mType;

    public Contact(String name, int type) {
        this.name = name;
        mType = type;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getAvatar_color() {
        return avatar_color;
    }

    public void setAvatar_color(String avatar_color) {
        this.avatar_color = avatar_color;
    }

    public Contact(Parcel in) {
        email_id = in.readInt();
        contact_id = in.readInt();
        name = in.readString();
        remark = in.readString();
        birthday = in.readString();
        company = in.readString();
        department = in.readString();
        position = in.readString();
        email = in.readString();
        iphone = in.readString();
        address = in.readString();
        avatar_color = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Contact() {

    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(email_id);
        dest.writeInt(contact_id);
        dest.writeString(name);
        dest.writeString(remark);
        dest.writeString(birthday);
        dest.writeString(company);
        dest.writeString(department);
        dest.writeString(position);
        dest.writeString(email);
        dest.writeString(iphone);
        dest.writeString(address);
        dest.writeString(avatar_color);
    }
}
