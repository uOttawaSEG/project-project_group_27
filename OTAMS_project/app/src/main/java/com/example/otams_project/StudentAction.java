package com.example.otams_project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.widget.Toast;
public class StudentAction {
    private final FirebaseAccessor accessor;
    public StudentAction(){
        accessor=FirebaseAccessor.getInstance();
    }
    public void loadAvailableSlots(String tutorEmail, AvailabilitySlotsCallback callback){
        accessor.getTutorSlots(tutorEmail, new AvailabilitySlotsCallback() {
            @Override
            public void onAvailabilitySlotsFetched(List<AvailabilitySlots> availabilitySlots) {
                List<AvailabilitySlots> available = new ArrayList<>();
                for (AvailabilitySlots s : availabilitySlots){
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
        public void bookSlot(Context context, AvailabilitySlots slot ,String studentEmail){
            String status;
            if(slot.isRequiresApproval()){
                status="pending";}
            else{
                status="approved";}
            Sessions session= new Sessions(slot.getTutorEmail(), studentEmail, slot.getDate(), slot.getStartTime(), slot.getEndTime(),status);

            accessor.createSession(session);
            slot.setBooked(true);
            accessor.bookSlot(slot.getSlotID());

        }

}
