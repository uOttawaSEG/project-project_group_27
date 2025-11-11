package com.example.otams_project;

import java.util.List;

public interface AvailabilitySlotsCallback {

    public void onAvailabilitySlotsFetched(List<AvailabilitySlots> availabilitySlots);

    public void onError(String message);
}
