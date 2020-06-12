package com.example.schoolapp.Models.Entities;

public class LectureVideo {
    String LectureVideoName;
    String LectureVideoUrl;
    String LectureVideoNameCourse;

    public LectureVideo() {
    }

    public LectureVideo(String lectureVideoName, String lectureVideoUrl, String lectureVideoNameCourse) {
        LectureVideoName = lectureVideoName;
        LectureVideoUrl = lectureVideoUrl;
        LectureVideoNameCourse = lectureVideoNameCourse;
    }

    public String getLectureVideoName() {
        return LectureVideoName;
    }

    public void setLectureVideoName(String lectureVideoName) {
        LectureVideoName = lectureVideoName;
    }

    public String getLectureVideoUrl() {
        return LectureVideoUrl;
    }

    public void setLectureVideoUrl(String lectureVideoUrl) {
        LectureVideoUrl = lectureVideoUrl;
    }

    public String getLectureVideoNameCourse() {
        return LectureVideoNameCourse;
    }

    public void setLectureVideoNameCourse(String lectureVideoNameCourse) {
        LectureVideoNameCourse = lectureVideoNameCourse;
    }
}
