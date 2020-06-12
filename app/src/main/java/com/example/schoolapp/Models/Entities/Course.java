package com.example.schoolapp.Models.Entities;

public class Course {
    int CourseID;
    String CourseName;
    String TeacherName;
    String SubjectCourseName;


    public Course() {
    }

    public Course(String CourseName, String SubjectCourseName, String TeacherName) {
        this.CourseName = CourseName;
        this.TeacherName = TeacherName;
        this.SubjectCourseName = SubjectCourseName;

    }

    public int getId() {
        return CourseID;
    }

    public void setId(int id) {
        this.CourseID = id;
    }

    public String getNameCourse() {
        return CourseName;
    }

    public void setNameCourse(String nameCourse) {
        this.CourseName = nameCourse;
    }

    public String getNameSubject() {
        return SubjectCourseName;
    }

    public void setNameSubject(String nameSubject) {
        this.SubjectCourseName = nameSubject;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }
}




