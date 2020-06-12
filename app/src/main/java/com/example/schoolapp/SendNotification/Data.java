package com.example.schoolapp.SendNotification;

public class Data {
    private String Title;
    private String Message;
    private String user;


    public Data(String title, String message, String user) {
        Title = title;
        Message = message;
        this.user = user;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
