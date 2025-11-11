package com.example.otams_project;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class Tutor extends User {


    private String degree;
    private String courses;
    private TutorActions actions;

    public Tutor(String firstName, String lastName, String phone) {
        super(firstName, lastName, phone);
    }
    public Tutor() {
        super();
    }

    public String getDegree() {
        return degree;
    }

    public  String getCourses() {
        return courses;
    }

    @Override
    public String toFancyString() {
        return "Degree: " + degree + "\n" +
                "Courses: " + courses;
    }



    public static Account register(RegisterCallback context, String firstName, String lastName, String email , String password, String phone , String degree, String courses) {
        Tutor tutor = new Tutor(firstName, lastName, phone);
        Account account = new Account(email, password, "tutor", tutor);
        tutor.degree = degree;
        tutor.courses = courses;
        FirebaseAccessor accessor = FirebaseAccessor.getInstance();
        accessor.writeNewAccount(context, account);

        return account;
    }



    //Facade Pattern Methods
    public void initializeActions() {
        this.actions = new TutorActions();
    }

    public void loadTutorSlots(AvailabilitySlotsCallback callback) {
        actions.loadTutorSlots(this.getAccount(true).getEmail(), callback);
    }

    public void deleteSlot(String slotID){
        actions.deleteSlot(slotID);
    }

    public void createAvailabilitySlot(Context context, String date, String startTime, String endTime, boolean requiresApproval , boolean booked) {
        actions.createAvailabilitySlot(context, this.getAccount(true).getEmail(), date, startTime, endTime, requiresApproval, booked);
    }

    public void approveSession(String sessionID){
        actions.approveSession(sessionID);
    }

    public void rejectSession(String sessionID){
        actions.rejectSession(sessionID);
    }

    public void cancelSession(String sessionID){
        actions.cancelSession(sessionID);
    }

    public void loadUpcomingSessions(SessionsCallback callback) {
        actions.loadUpcomingSessions(this.getAccount(true).getEmail(), callback);
    }

    public void loadPendingSessions(SessionsCallback callback) {
        actions.loadPendingSessions(this.getAccount(true).getEmail(), callback);
    }

    public void loadPastSessions(SessionsCallback callback) {
        actions.loadPastSessions(this.getAccount(true).getEmail(), callback);
    }

}
