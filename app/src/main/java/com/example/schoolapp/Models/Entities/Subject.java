package com.example.schoolapp.Models.Entities;

public class Subject {

    private int SubjectID;

    private String SubjectName;

    private boolean expanded;

    public Subject() {
    }

    public Subject(String SubjectName) {
        this.SubjectName = SubjectName;
       this. expanded = false;
    }

    public String getName() {
        return SubjectName;
    }

    public void setName(String name) {
        this.SubjectName = name;
    }

    public int getId() {
        return SubjectID;
    }

    public void setId(int id) {
        this.SubjectID = id;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
