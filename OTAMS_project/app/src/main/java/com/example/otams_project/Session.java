package com.example.otams_project;

public class Session {

    private String tutorEmail;
    private String studentEmail;
    private String date;
    private String startTime;
    private String endTime;
    private String status;
    private String courses;

    private String sessionID;

    private String slotID;

    private boolean tutorRated;


    public Session(){}

    public Session(String tutorEmail, String studentEmail, String date, String startTime, String endTime, String status, String courses){
        this.tutorEmail = tutorEmail;
        this.studentEmail = studentEmail;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.courses = courses;
        this.tutorRated = false;
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
    public String getCourses() {return courses;}
    public boolean isTutorRated() {return tutorRated;}
    public void rateTutor() {tutorRated = true;}

    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
