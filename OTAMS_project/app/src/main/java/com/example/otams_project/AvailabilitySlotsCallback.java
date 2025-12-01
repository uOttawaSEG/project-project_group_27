package com.example.otams_project;

import java.util.List;

public interface AvailabilitySlotsCallback {

    void onAvailabilitySlotsFetched(List<AvailabilitySlot> availabilitySlots);

    void onError(String message);
}
