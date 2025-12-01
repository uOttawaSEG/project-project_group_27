package com.example.otams_project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

public class TutorAction {

    private final FirebaseAccessor accessor;

    public TutorAction() {
        accessor = FirebaseAccessor.getInstance();
    }

    public void loadTutorSlots(String tutorEmail, AvailabilitySlotsCallback callback) {
        accessor.getTutorSlots(tutorEmail, callback);
    }

    public void deleteSlot(Context context,String slotID, String tutorEmail){
        accessor.getTutorSlots(tutorEmail, new AvailabilitySlotsCallback() {
            @Override
            public void onAvailabilitySlotsFetched(List<AvailabilitySlot> availabilitySlots) {
                final AvailabilitySlot[] target=new AvailabilitySlot[1];
                for(AvailabilitySlot s : availabilitySlots){
                    if(s.getSlotID().equals(slotID)){
                        target[0]=s;
                        break;}
                }
                if(target[0]==null){
                    Toast.makeText(context,"Error: Slot not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(target[0].isBooked()){
                    Toast.makeText(context,"Cannot delete slot is booked", Toast.LENGTH_SHORT).show();
                    return;}
                else{
                    accessor.deleteSlot(target[0].getSlotID());
                    Toast.makeText(context,"Slot deleted successfully", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onError(String message) {

            }
        });
    }
    public void getStudentInfo(String studentEmail, AccountCallback callback) {
        accessor.getAccountByEmail(studentEmail, callback);
    }

    public void createAvailabilitySlot(Context context, String tutorEmail, String date, String startTime, String endTime, boolean requiresApproval , boolean booked, String courses) {

        if(!(startTime.endsWith("00") || startTime.endsWith("30"))){
            Toast.makeText(context,"Invalid timing format",Toast.LENGTH_SHORT).show();
            return;}
        if(!(endTime.endsWith("00")|| endTime.endsWith("30"))){
            Toast.makeText(context,"Invalid timing format",Toast.LENGTH_SHORT).show();
            return;}
        if(!TimeStringComparer.isNumHoursAhead(date, startTime, 0)){
            Toast.makeText(context,"Error! Slot cannot be in the past. ",Toast.LENGTH_SHORT).show();
            return;}
        accessor.getTutorSlots(tutorEmail, new AvailabilitySlotsCallback() {
            @Override
            public void onAvailabilitySlotsFetched(List<AvailabilitySlot> availabilitySlots) {
                for (AvailabilitySlot s : availabilitySlots) {
                    if (s.getDate().equals(date)) {
                        if (TimeStringComparer.timeOverlap(date, s.getDate(), startTime, endTime, s.getStartTime(), s.getEndTime())) {
                            Toast.makeText(context, "Error! Slot overlaps with another.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                AvailabilitySlot slot = new AvailabilitySlot(startTime, endTime, tutorEmail, date, requiresApproval, booked, courses);
                accessor.createAvailabilitySlot(slot);

                /*
                Will never be true in deliverable 4

                if (booked) {
                    String status = requiresApproval ? "pending" : "approved";
                    Session session = new Session(tutorEmail, "exstudent@gmail.com", date, startTime, endTime, status, courses);
                    accessor.createSession(session);
                }

                 */

                Toast.makeText(context, "Slot created.", Toast.LENGTH_SHORT).show();
            }
                public void onError(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });



    }


    public void approveSession(String sessionID, String slotID){
        accessor.updateSessionStatus(sessionID, slotID, "approved");
    }
    public void rejectSession(String sessionID, String slotID){
        accessor.updateSessionStatus(sessionID, slotID, "rejected");
    }
    public void cancelSession(String sessionID, String slotID){
        accessor.updateSessionStatus(sessionID, slotID, "cancelled");
    }


    public void loadUpcomingSessions(String tutorEmail, SessionsCallback callback) {
        accessor.getTutorSessions(tutorEmail, new SessionsCallback() {
            @Override
            public void onSessionsFetched(List<Session> sessions) {
                List<Session> upcomingSessions = filterUpcomingSessions(sessions);
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
            public void onSessionsFetched(List<Session> sessions) {
                List<Session> upcomingSessions = filterPendingSessions(sessions);
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
            public void onSessionsFetched(List<Session> sessions) {
                List<Session> upcomingSessions = filterPastSessions(sessions);
                callback.onSessionsFetched(upcomingSessions);

            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    private List<Session> filterUpcomingSessions(List<Session> sessions) {
        List<Session> upcomingSessions = new ArrayList<>();
        String today = getDate();
        for (Session session : sessions) {
            if (TimeStringComparer.isNumHoursAhead(session.getDate(), session.getStartTime(), 0) && (session.getStatus().equals("approved"))) {
                upcomingSessions.add(session);
            }
        }
        return upcomingSessions;

    }

    private List<Session> filterPastSessions(List<Session> sessions) {
        List<Session> pastSessions = new ArrayList<>();
        String today = getDate();
        for (Session session : sessions) {
            if (!TimeStringComparer.isNumHoursAhead(session.getDate(), session.getStartTime(), 0) && (session.getStatus().equals("approved"))) {
                pastSessions.add(session);
            }
        }
        return pastSessions;

    }

    private List<Session> filterPendingSessions(List<Session> sessions) {
        List<Session> pendingSessions = new ArrayList<>();
        String today = getDate();
        for (Session session : sessions) {
            if (TimeStringComparer.isNumHoursAhead(session.getDate(), session.getStartTime(), 0) && (session.getStatus().equals("pending"))) {
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



