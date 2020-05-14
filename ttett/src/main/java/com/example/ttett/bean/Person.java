package com.example.ttett.bean;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    private String name;
    private String email;
    private List<String> emailList;

    public Person(String name, List<String> emailList) {
        this.name = name;
        this.emailList = emailList;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public Person() { }

    public Person(String n, String e) { name = n; email = e; }

    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() { return name; }
}
