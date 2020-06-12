package com.example.schoolapp.Models.Entities;

public class Appointment {
    int appointmentID;
    String nameDay;
    String timeStart;
    String timeEnd;
    String nameCourseAppointment;

    public Appointment(String nameDay, String timeStart, String timeEnd,String nameCourseAppointment) {
        this.nameDay = nameDay;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.nameCourseAppointment = nameCourseAppointment;

    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getNameDay() {
        return nameDay;
    }

    public void setNameDay(String nameDay) {
        this.nameDay = nameDay;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getNameCourseAppointment() {
        return nameCourseAppointment;
    }

    public void setNameCourseAppointment(String nameCourseAppointment) {
        this.nameCourseAppointment = nameCourseAppointment;
    }
}

