package com.example.schoolapp.Models.Entities;

public class Test {
    String TitleForTest;
    String TestNameCourse;
    String TestNameSubject;
    String TestDate;

    public Test() {
    }

    public Test(String titleForTest, String testNameCourse, String testNameSubject, String testDate) {
        TitleForTest = titleForTest;
        TestNameCourse = testNameCourse;
        TestNameSubject = testNameSubject;
        TestDate = testDate;
    }


    public String getTitleForTest() {
        return TitleForTest;
    }

    public void setTitleForTest(String titleForTest) {
        TitleForTest = titleForTest;
    }

    public String getTestNameCourse() {
        return TestNameCourse;
    }

    public void setTestNameCourse(String testNameCourse) {
        TestNameCourse = testNameCourse;
    }

    public String getTestNameSubject() {
        return TestNameSubject;
    }

    public void setTestNameSubject(String testNameSubject) {
        TestNameSubject = testNameSubject;
    }

    public String getTestDate() {
        return TestDate;
    }

    public void setTestDate(String testDate) {
        TestDate = testDate;
    }
}
