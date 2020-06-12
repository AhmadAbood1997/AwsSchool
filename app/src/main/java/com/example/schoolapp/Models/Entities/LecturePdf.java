package com.example.schoolapp.Models.Entities;

public class LecturePdf {
    String LecturePdfName;
    String LecturePdfUrl;
    String LecturePdfNameCourse;

    public LecturePdf() {
    }

    public LecturePdf(String lecturePdfName, String lecturePdfUrl, String lecturePdfNameCourse) {
        LecturePdfName = lecturePdfName;
        LecturePdfUrl = lecturePdfUrl;
        LecturePdfNameCourse = lecturePdfNameCourse;
    }


    public String getLecturePdfName() {
        return LecturePdfName;
    }

    public void setLecturePdfName(String lecturePdfName) {
        LecturePdfName = lecturePdfName;
    }

    public String getLecturePdfUrl() {
        return LecturePdfUrl;
    }

    public void setLecturePdfUrl(String lecturePdfUrl) {
        LecturePdfUrl = lecturePdfUrl;
    }

    public String getLecturePdfNameCourse() {
        return LecturePdfNameCourse;
    }

    public void setLecturePdfNameCourse(String lecturePdfNameCourse) {
        LecturePdfNameCourse = lecturePdfNameCourse;
    }
}
