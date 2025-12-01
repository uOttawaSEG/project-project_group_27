package com.example.otams_project;

public class AvailabilitySlot {
    private String startTime;
    private String endTime;

    private String tutorEmail;

    private String date;

    private String courses;

    private boolean requiresApproval;

    private String slotID;

    private boolean booked;

    public AvailabilitySlot(){}
    public AvailabilitySlot(String startTime, String endTime, String tutorEmail, String date, boolean requiresApproval, boolean booked, String courses) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiresApproval = requiresApproval;
        this.tutorEmail = tutorEmail;
        this.date = date;
        this.booked = booked;
        this.courses = courses;

    }
    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public String getDate() {
        return date;
    }

    public String getCourses() {
        return courses;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }
    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }


}
