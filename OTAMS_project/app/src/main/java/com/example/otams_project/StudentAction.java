package com.example.otams_project;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class StudentAction {
    private final FirebaseAccessor accessor;

    public StudentAction(){
        accessor=FirebaseAccessor.getInstance();
    }
    public void loadAvailableSlots(AvailabilitySlotsCallback callback){
        accessor.getUnbookedSlots(new AvailabilitySlotsCallback() {
            @Override
            public void onAvailabilitySlotsFetched(List<AvailabilitySlot> availabilitySlots) {
                List<AvailabilitySlot> available = new ArrayList<>();
                for (AvailabilitySlot s : availabilitySlots){
                    if(!s.isBooked())
                        available.add(s);
                }

                callback.onAvailabilitySlotsFetched(available);
            }

            @Override
            public void onError(String message) {
            callback.onError(message);
            }

        });}


    public void bookSlot(Context context, AvailabilitySlot slot , String studentEmail){
        accessor.getStudentSessions(studentEmail, new StudentSessionCallback() {
            @Override
            public void onSessionsFetched(List<Session> sessions) {
                List<Session> upcomingSessions;
                upcomingSessions = filterUpcomingSessions(sessions);

                for (Session s : upcomingSessions) {
                    if ("approved".equals(s.getStatus()) || "pending".equals(s.getStatus())) {
                        if (TimeStringComparer.timeOverlap(s.getDate(), slot.getDate(), s.getStartTime(), s.getEndTime(), slot.getStartTime(), slot.getEndTime())) {
                            Toast.makeText(context, "Can't book overlapping slots", Toast.LENGTH_SHORT).show();
                            Log.d("bookSlot", "Found overlap with " + slot.getSlotID());
                            return;
                        }
                    }
                }

                String status;

                if(slot.isRequiresApproval()){
                    status="pending";}
                else{
                    status="approved";}
                Session session= new Session(slot.getTutorEmail(), studentEmail, slot.getDate(), slot.getStartTime(), slot.getEndTime(),status, slot.getCourses());
                session.setSlotID(slot.getSlotID());

                accessor.createSession(session);
                slot.setBooked(true);
                Toast.makeText(context, "Slot booked", Toast.LENGTH_SHORT).show();
                accessor.bookSlot(slot.getSlotID());
                Log.d("bookSlot", "No overlap");
            }

            @Override
            public void onError(String message) {
                Log.d("bookSlot","Error checking if new book time overlaps with other book times");
            }
        });



    }

    public void cancelSession(String sessionID, String slotID){
        accessor.updateSessionStatus(sessionID, slotID,"cancelled");
    }

/*
    @Override
    public void onSessionsFetched(List<Session> sessions) {
        String today= getDate();
         pastSessions= new ArrayList<>();
         upcomingSessions= new ArrayList<>();
        for(Session s: sessions){
            if(s.getDate().compareTo(today)>=0)
                upcomingSessions.add(s);
            else
                pastSessions.add(s);

        }
        if(storedCallback!=null)
            storedCallback.onSessionsFetched(sessions);


    }

    @Override
    public void onError(String message) {


    }

 */

    public void loadMySessions(String studentEmail, boolean future, SessionsCallback callback) {
        accessor.getStudentSessions(studentEmail, new StudentSessionCallback() {
            @Override
            public void onSessionsFetched(List<Session> sessions) {
                List<Session> upcomingSessions;
                if (future) {
                    upcomingSessions = filterUpcomingSessions(sessions);
                } else {
                    upcomingSessions = filterPastSessions(sessions);
                }

                callback.onSessionsFetched(upcomingSessions);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void getTutorInfo(String tutorEmail, AccountCallback callback) {
        accessor.getAccountByEmail(tutorEmail, callback);
    }
    private List<Session> filterUpcomingSessions(List<Session> sessions) {
        List<Session> upcomingSessions = new ArrayList<>();
        for (Session session : sessions) {
            if (TimeStringComparer.isNumHoursAhead(session.getDate(), session.getStartTime(), 0)) {
                upcomingSessions.add(session);
            }
        }
        return upcomingSessions;

    }

    private List<Session> filterPastSessions(List<Session> sessions) {
        List<Session> pastSessions = new ArrayList<>();
        for (Session session : sessions) {
            if (!TimeStringComparer.isNumHoursAhead(session.getDate(), session.getStartTime(), 0)) {
                pastSessions.add(session);
            }
        }
        return pastSessions;

    }
}
