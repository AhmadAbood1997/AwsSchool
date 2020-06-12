package com.example.schoolapp.Models.Entities;

public class LectureMp3 {
    String LectureMp3Name;
    String LectureMp3Url;
    String LectureMp3NameCourse;

    public LectureMp3() {
    }

    public LectureMp3(String lectureMp3Name, String lectureMp3Url, String lectureMp3NameCourse) {
        LectureMp3Name = lectureMp3Name;
        LectureMp3Url = lectureMp3Url;
        LectureMp3NameCourse = lectureMp3NameCourse;
    }


    public String getLectureMp3Name() {
        return LectureMp3Name;
    }

    public void setLectureMp3Name(String lectureMp3Name) {
        LectureMp3Name = lectureMp3Name;
    }

    public String getLectureMp3Url() {
        return LectureMp3Url;
    }

    public void setLectureMp3Url(String lectureMp3Url) {
        LectureMp3Url = lectureMp3Url;
    }

    public String getLectureMp3NameCourse() {
        return LectureMp3NameCourse;
    }

    public void setLectureMp3NameCourse(String lectureMp3NameCourse) {
        LectureMp3NameCourse = lectureMp3NameCourse;
    }
}
