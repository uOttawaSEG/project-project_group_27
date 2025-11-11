package com.example.otams_project;

import java.util.List;

public interface SessionsCallback {

    public void onSessionsFetched(List<Sessions> sessions);

    public void onError(String message);
}
