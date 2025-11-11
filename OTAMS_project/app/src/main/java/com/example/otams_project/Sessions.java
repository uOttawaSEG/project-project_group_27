package com.example.otams_project;

public class Sessions {

    private String tutorEmail;
    private String studentEmail;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    private String sessionID;


    public Sessions(){}

    public Sessions(String tutorEmail, String studentEmail, String date, String startTime, String endTime, String status){
        this.tutorEmail = tutorEmail;
        this.studentEmail = studentEmail;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
