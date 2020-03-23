package com.example.ttett.Entity;

public class User {
    private String account,password;
    private int user_id;
    private boolean Registerresult;

//    public boolean isExistUser() {
//        return isExistUser;
//    }
//
//    public void setExistUser(boolean existUser) {
//        isExistUser = existUser;
//    }

    public boolean isRegisterresult() {
        return Registerresult;
    }

    public void setRegisterresult(boolean registerresult) {
        Registerresult = registerresult;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
