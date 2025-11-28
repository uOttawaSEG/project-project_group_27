package com.example.otams_project;

import java.util.List;

public interface StudentSessionCallback {
    void onSessionsFetched(List<Sessions> sessions);
    void onError(String message);
}
