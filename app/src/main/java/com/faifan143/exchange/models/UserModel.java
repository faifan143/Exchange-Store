package com.faifan143.exchange.models;

public class UserModel {
    private String name;
    private String email;
    private String number;
    private String password;
    private String fcmToken;


    public UserModel(String name, String email, String number, String password, String fcmToken) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.fcmToken = fcmToken;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFCMToken() {
        return fcmToken;
    }

    public void setFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
