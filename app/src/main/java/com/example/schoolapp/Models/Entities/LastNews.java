package com.example.schoolapp.Models.Entities;

public class LastNews
{
    private String title;

    private String contain;

    private String date;

    public LastNews() {

    }

    public LastNews(String title, String contain, String date) {
        this.title = title;
        this.contain = contain;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
