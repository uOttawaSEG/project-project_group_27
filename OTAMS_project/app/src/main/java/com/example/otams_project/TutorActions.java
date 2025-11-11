package com.example.otams_project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TutorActions {

    private final FirebaseAccessor accessor;

    public TutorActions() {
        accessor = FirebaseAccessor.getInstance();
    }

    public void loadTutorSlots(String tutorEmail, AvailabilitySlotsCallback callback) {
        accessor.getTutorSlots(tutorEmail, callback);
    }

    public void deleteSlot(String slotID){
        accessor.deleteSlot(slotID);
    }

    public void createAvailabilitySlot(String tutorEmail, String date, String startTime, String endTime, boolean requiresApproval , boolean booked) {
        AvailabilitySlots slot = new AvailabilitySlots(startTime, endTime, tutorEmail, date, requiresApproval , booked);
        accessor.createAvailabilitySlot(slot);


        if(booked){
            Sessions session = new Sessions(tutorEmail, "exstudentEmail@gmail.com", date, startTime, endTime, "pending");
            accessor.createSession(session);
        }
    }


    public void approveSession(String sessionID){
        accessor.updateSessionStatus(sessionID, "approved");
    }
    public void rejectSession(String sessionID){
        accessor.updateSessionStatus(sessionID, "rejected");
    }
    public void cancelSession(String sessionID){
        accessor.updateSessionStatus(sessionID, "cancelled");
    }


    public void loadUpcomingSessions(String tutorEmail, SessionsCallback callback) {
        accessor.getTutorSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Sessions> sessions) {
                List<Sessions> upcomingSessions = filterUpcomingSessions(sessions);
                callback.onSessionsFetched(upcomingSessions);

            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void loadPendingSessions(String tutorEmail, SessionsCallback callback) {
        accessor.getTutorSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Sessions> sessions) {
                List<Sessions> upcomingSessions = filterPendingSessions(sessions);
                callback.onSessionsFetched(upcomingSessions);

            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void loadPastSessions(String tutorEmail, SessionsCallback callback) {
        accessor.getTutorSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Sessions> sessions) {
                List<Sessions> upcomingSessions = filterPastSessions(sessions);
                callback.onSessionsFetched(upcomingSessions);

            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    private List<Sessions> filterUpcomingSessions(List<Sessions> sessions) {
        List<Sessions> upcomingSessions = new ArrayList<>();
        String today = getDate();
        for (Sessions session : sessions) {
            if ((session.getDate().compareTo(today) > 0) && (session.getStatus().equals("approved"))) {
                upcomingSessions.add(session);
            }
        }
        return upcomingSessions;

    }

    private List<Sessions> filterPastSessions(List<Sessions> sessions) {
        List<Sessions> pastSessions = new ArrayList<>();
        String today = getDate();
        for (Sessions session : sessions) {
            if ((session.getDate().compareTo(today) < 0) && (session.getStatus().equals("approved"))) {
                pastSessions.add(session);
            }
        }
        return pastSessions;

    }

    private List<Sessions> filterPendingSessions(List<Sessions> sessions) {
        List<Sessions> pendingSessions = new ArrayList<>();
        String today = getDate();
        for (Sessions session : sessions) {
            if ((session.getDate().compareTo(today) > 0) && (session.getStatus().equals("pending"))) {
                pendingSessions.add(session);
            }
        }
        return pendingSessions;

    }








    private String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

}



