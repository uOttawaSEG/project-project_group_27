package com.example.otams_project;

import java.util.List;

public interface SessionsCallback {

    void onSessionsFetched(List<Session> sessions);

    void onError(String message);
}
