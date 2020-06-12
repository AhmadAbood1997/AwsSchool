package com.example.schoolapp.Models.Entities;

public class User {
    String uid;
    String name;
    String status;
    String image;
    String device_token;

    public User() {

    }

    public User(String name, String status, String image, String device_token,String uid) {

        this.name = name;
        this.status = status;
        this.image = image;
        this.device_token = device_token;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
