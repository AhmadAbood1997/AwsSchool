package com.example.schoolapp.Models.Entities;

public class WelcomeItem {
    String  Description;
    int Image;

    public WelcomeItem( String description, int image) {
        Description = description;
        Image = image;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
