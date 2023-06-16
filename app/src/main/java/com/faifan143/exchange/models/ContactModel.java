package com.faifan143.exchange.models;

public class ContactModel {
    private String email , number,name , fcmToken , key;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFCMToken() {
        return fcmToken;
    }

    public void setFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ContactModel(String email, String number, String name, String fcmToken) {
        this.email = email;
        this.number = number;
        this.name = name;
        this.fcmToken = fcmToken;
    }

    public ContactModel() {
    }
}
